package techguns.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.TGConfig;
import techguns.*;
import techguns.api.machines.IMachineType;
import techguns.blocks.GenericBlock;
import techguns.blocks.GenericItemBlockMeta;
import techguns.blocks.GenericItemBlockMetaMachineBlock;
import techguns.blocks.machines.multiblocks.MultiBlockRegister;
import techguns.events.TechgunsGuiHandler;
import techguns.tileentities.*;
import techguns.util.TextUtil;

/**
 * A Machine that can be rendered with TESR and has no properties besides type, 16 types per block.
 *
 * @param <T> Enum of all Machine Types
 */
public class BasicMachine<T extends Enum<T> & IStringSerializable & IMachineType> extends GenericBlock {
	protected Class<T> clazz;
	protected BlockStateContainer blockStateOverride;
	
	public PropertyEnum<T> MACHINE_TYPE;
	
	protected GenericItemBlockMeta itemblock;
	
	public BasicMachine(String name, Class<T> clazz) {
		super(name, Material.IRON);
		this.setSoundType(SoundType.METAL);
		setHardness(4.0f);
		this.clazz=clazz;
		MACHINE_TYPE = PropertyEnum.create("machinetype",clazz);
		this.blockStateOverride = new BlockStateContainer.Builder(this).add(MACHINE_TYPE).build();
		this.setDefaultState(this.getBlockState().getBaseState());
	}

	public String getNameSuffix(int meta) {
		IBlockState state = this.getStateFromMeta(meta);
		T t = state.getValue(MACHINE_TYPE);
		return t.toString().toLowerCase();
	}
	
	
	@Override
	public @NotNull BlockStateContainer getBlockState() {
		return this.blockStateOverride;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(getDefaultState().withProperty(MACHINE_TYPE, state.getValue(MACHINE_TYPE)));
	}
	
	@Override
	public void getSubBlocks(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
		for (T t : clazz.getEnumConstants()) {
			if( (!t.debugOnly() || TGConfig.general.debug) && !(t.hideInCreative())) {
				items.add(new ItemStack(this,1,this.getMetaFromState(getDefaultState().withProperty(MACHINE_TYPE, t))));
			}
		}
	}
	
	@Override
	public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof BasicInventoryTileEnt) {
			((BasicInventoryTileEnt) tile).onBlockBreak();
		} else if (tile instanceof MultiBlockMachineTileEntSlave) {
			((MultiBlockMachineTileEntSlave) tile).onBlockBreak();
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	
	@Override
	public void neighborChanged(@NotNull IBlockState state, World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof BasicRedstoneTileEnt){
			((BasicRedstoneTileEnt)tile).onNeighborBlockChange();		
		}
	}

	@Override
	public boolean shouldCheckWeakPower(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
				TileEntity tile = world.getTileEntity(pos);
				if (!world.isRemote && tile instanceof BasicInventoryTileEnt) {
					BasicInventoryTileEnt tileent = (BasicInventoryTileEnt) tile;
					
					if (tileent.isUseableByPlayer(player)) {
						
						if(tile instanceof MultiBlockMachineTileEntMaster) {
							MultiBlockMachineTileEntMaster master = (MultiBlockMachineTileEntMaster) tile;
							if (!master.isFormed() && !player.isSneaking()) {
								//System.out.println("UNFORMED MASTER TRY FORM");
								if(MultiBlockRegister.canFormFromSide(tileent, facing)) {
									if (MultiBlockRegister.canForm(master, player, facing)) {
										if (MultiBlockRegister.form(tileent, player, facing)) {
											master.needUpdate();
										}
									}
									return true;
								}
							} 
						}
					
						ItemStack helditem = player.getHeldItem(hand);
						if (!helditem.isEmpty() && helditem.getItem().getToolClasses(helditem).contains("wrench")) {
							
							if (player.isSneaking() && tileent.canBeWrenchDismantled()) {
								
								NBTTagCompound tileEntTags =new NBTTagCompound();
								tileent.writeNBTforDismantling(tileEntTags);
								ItemStack item = new ItemStack(this,1,this.damageDropped(state));
								NBTTagCompound itemnbt = item.getTagCompound();
								if (itemnbt==null){
									itemnbt=new NBTTagCompound();
									item.setTagCompound(itemnbt);
								}
								itemnbt.setTag("TileEntityData", tileEntTags);
								tileent.emptyContent();
								world.setBlockToAir(pos);
								world.spawnEntity(new EntityItem(world,pos.getX()+0.5d, pos.getY()+0.5d, pos.getZ()+0.5d, item));
								
							} else if (tileent.canBeWrenchRotated()) {
								if(tileent.hasRotation()) {
									tileent.rotateTile(facing);
								} else {
									this.rotateBlock(world, pos, EnumFacing.UP);
								}
							}
							
						} else if (!helditem.isEmpty() && hasBucketInteraction(state) && helditem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) ) { 
							
							IFluidHandlerItem fluidhandler = helditem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
							
							boolean interacted = tileent.onFluidContainerInteract(player, hand, fluidhandler, helditem);
							
							if(interacted) {
								world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);

								return true;
							} else {
								if (tile instanceof MultiBlockMachineTileEntMaster) {
									MultiBlockMachineTileEntMaster masterEnt = (MultiBlockMachineTileEntMaster) tile;
									if (masterEnt.isFormed()) TechgunsGuiHandler.openGuiForPlayer(player, tile);
								} else TechgunsGuiHandler.openGuiForPlayer(player, tile);
							}
							
						} else {
							if (tile instanceof MultiBlockMachineTileEntMaster) {
								MultiBlockMachineTileEntMaster masterEnt = (MultiBlockMachineTileEntMaster) tile;
								if (masterEnt.isFormed()) TechgunsGuiHandler.openGuiForPlayer(player, tile);
							} else TechgunsGuiHandler.openGuiForPlayer(player, tile);
						}
					
					} else {
						player.sendStatusMessage(new TextComponentString(TextUtil.trans("techguns.container.security.denied")), true);
					}

				} else if (tile instanceof MultiBlockMachineTileEntSlave) {
					
					MultiBlockMachineTileEntSlave slave = (MultiBlockMachineTileEntSlave) tile;
					if(slave.hasMaster()) {
						if(!world.isRemote) {
							TileEntity master = world.getTileEntity(slave.getMasterPos());
							if (master instanceof MultiBlockMachineTileEntMaster) {
								TechgunsGuiHandler.openGuiForPlayer(player, master);
							}
						}
						
					} else {
						return false;
					}	
				}
			return true;
		//}
  	
		//return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}	
	
	protected boolean hasBucketInteraction(IBlockState state) {
		return state.getValue(MACHINE_TYPE) == EnumMachineType.CHEM_LAB;
	}
	
	@Override
	public TileEntity createTileEntity(@NotNull World world, IBlockState state) {
		return state.getValue(MACHINE_TYPE).getTile();
	}
	
	@Override
	public boolean hasTileEntity(@NotNull IBlockState state) {
		return true;
	}

	@Override
	public ItemBlock createItemBlock() {
		GenericItemBlockMeta itemblock =  new GenericItemBlockMetaMachineBlock(this);
		this.itemblock=itemblock;
		return itemblock;
	}

	@Override
	public void registerBlock(Register<Block> event) {
		super.registerBlock(event);
		for (T t : clazz.getEnumConstants()) {
			if(TileEntity.getKey(t.getTileClass())==null) {
				GameRegistry.registerTileEntity(t.getTileClass(), new ResourceLocation(Tags.MOD_ID,t.getName()));
			}
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(MACHINE_TYPE).getIndex();
	}

	@Override
	public @NotNull IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(MACHINE_TYPE, clazz.getEnumConstants()[meta]);
    }

	@Override
	public @NotNull EnumBlockRenderType getRenderType(IBlockState state) {
		T t = state.getValue(MACHINE_TYPE);
		return t.getRenderType();
	}

	@Override
	public boolean isFullCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(@NotNull IBlockState state) {
		return false;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, @NotNull BlockRenderLayer layer) {
		T t = state.getValue(MACHINE_TYPE);
		return t.getBlockRenderLayer()==layer;
	}

	public void onBlockPlacedByExtended(World world, BlockPos pos, EntityLivingBase placer, ItemStack stack, EnumFacing sideHit) {
		
		TileEntity tile = world.getTileEntity(pos);
		
		if (placer instanceof EntityPlayer && tile instanceof BasicOwnedTileEnt){
			((BasicOwnedTileEnt)tile).setOwner((EntityPlayer)placer);
		}
		
		if(tile instanceof BasicInventoryTileEnt) {
			BasicInventoryTileEnt invtile = (BasicInventoryTileEnt) tile;
			if (invtile.hasRotation()) {
				
				int dir = MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
				invtile.rotation = (byte) (dir%4);
				
			}
			
			if (stack.getTagCompound()!=null && stack.getTagCompound().hasKey("TileEntityData")) {
				invtile.readNBTfromStackTag(stack.getTagCompound().getCompoundTag("TileEntityData"));
			}
		}
		
		if(tile instanceof TurretTileEnt){
			TurretTileEnt turret = (TurretTileEnt) tile;
			if(sideHit==EnumFacing.DOWN) {
				turret.setFacing(EnumFacing.DOWN);
			} else {
				turret.setFacing(EnumFacing.UP);
			}
        	
        	if(!turret.turretDeath){
        		turret.spawnTurret(world);
        	}
		}
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public GenericItemBlockMeta getItemblock() {
		return itemblock;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemBlockModels() {
		for(int i = 0; i< clazz.getEnumConstants().length;i++) {
			IBlockState state = getDefaultState().withProperty(MACHINE_TYPE, clazz.getEnumConstants()[i]);
			if(clazz.getEnumConstants()[i].getRenderType()==EnumBlockRenderType.MODEL) {
				//ModelLoader.setCustomModelResourceLocation(itemblock, i, new ModelResourceLocation(this.getRegistryName(),BlockUtils.getBlockStateVariantString(state)));
				ModelLoader.setCustomModelResourceLocation(itemblock, i, new ModelResourceLocation(new ResourceLocation(Tags.MOD_ID,clazz.getEnumConstants()[i].name().toLowerCase()),"inventory"));
			} else {
				ForgeHooksClient.registerTESRItemStack(itemblock, this.getMetaFromState(state), state.getValue(MACHINE_TYPE).getTileClass());
				ModelLoader.setCustomModelResourceLocation(itemblock, i, new ModelResourceLocation(itemblock.getRegistryName(),"inventory"));
			}
		}
	}

	@Override
	public boolean hasCustomBreakingProgress(@NotNull IBlockState state) {
		return true;
	}

	@Override
	public boolean rotateBlock(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing axis) {
		
		if (axis == EnumFacing.DOWN || axis == EnumFacing.UP) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof BasicInventoryTileEnt) {
				BasicInventoryTileEnt invtile = (BasicInventoryTileEnt) tile;
				if (invtile.hasRotation()) {
					invtile.rotateTile();
					return true;
				}
			}
		
		}
		return false;
	}

	@Override
	public @NotNull SoundType getSoundType(IBlockState state, @NotNull World world, @NotNull BlockPos pos, Entity entity) {
		return state.getValue(MACHINE_TYPE).getSoundType();
	}

}
