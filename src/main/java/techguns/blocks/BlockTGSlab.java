package techguns.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.util.BlockUtils;

public class BlockTGSlab extends GenericBlock {

    protected GenericItemBlockMeta itemblock;
    protected int numVariants = 8;
    public BlockTGDoubleSlab doubleSlab;
    protected String[] textures = new String[8];
    protected String[] sideTextures = new String[8];

    public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 7);

    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockTGSlab(String name, Material mat, SoundType sound) {
        super(name, mat);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM).withProperty(VARIANT, 0));
        this.setSoundType(sound);
        this.useNeighborBrightness = true;
    }

    public BlockTGSlab setTextures(String... tex) {
        System.arraycopy(tex, 0, this.textures, 0, Math.min(tex.length, 8));
        numVariants = Math.min(tex.length, 8);
        return this;
    }

    public String[] getTextures() {
        return textures;
    }

    public BlockTGSlab setSideTexture(int index, String tex) {
        if (index >= 0 && index < this.sideTextures.length) {
            this.sideTextures[index] = tex;
        }
        return this;
    }

    public String[] getSideTextures() {
        return sideTextures;
    }

    public BlockTGSlab setDoubleSlab(BlockTGDoubleSlab doubleSlab) {
        this.doubleSlab = doubleSlab;
        return this;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
    }

    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        if (face == EnumFacing.UP && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            return BlockFaceShape.SOLID;
        } else if (face == EnumFacing.DOWN && state.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
            return BlockFaceShape.SOLID;
        }
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
    }

    @Override
    public void getSubBlocks(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        for (int i = 0; i < this.numVariants; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public @NotNull IBlockState getStateForPlacement(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @NotNull EntityLivingBase placer) {
        IBlockState iblockstate = this.getStateFromMeta(meta).withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP);
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(VARIANT, meta & 7)
                .withProperty(HALF, (meta & 8) > 0 ? BlockSlab.EnumBlockHalf.TOP : BlockSlab.EnumBlockHalf.BOTTOM);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(VARIANT);
        if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }
        return i;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF, VARIANT);
    }

    @Override
    public boolean doesSideBlockRendering(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        if (ForgeModContainer.disableStairSlabCulling) {
            return super.doesSideBlockRendering(state, world, pos, face);
        }

        if (state.isOpaqueCube()) {
            return true;
        }

        BlockSlab.EnumBlockHalf half = state.getValue(HALF);
        if (face == EnumFacing.UP) return half == BlockSlab.EnumBlockHalf.TOP;
        if (face == EnumFacing.DOWN) return half == BlockSlab.EnumBlockHalf.BOTTOM;
        return false;
    }

    @Override
    public ItemBlock createItemBlock() {
        this.itemblock = new ItemBlockTGSlab(this, this.doubleSlab);
        return itemblock;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemBlockModels() {
        for (int i = 0; i < this.numVariants; i++) {
            IBlockState state = this.getDefaultState().withProperty(VARIANT, i);
            ModelLoader.setCustomModelResourceLocation(this.itemblock, i, new ModelResourceLocation(getRegistryName(), BlockUtils.getBlockStateVariantString(state)));
        }
    }
}