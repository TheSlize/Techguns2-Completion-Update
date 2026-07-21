package techguns.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.util.BlockUtils;

import java.util.Random;

public class BlockTGDoubleSlab extends GenericBlock {

    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 7);
    protected BlockTGSlab singleSlab;

    public BlockTGDoubleSlab(String name, Material mat, SoundType sound, BlockTGSlab singleSlab) {
        super(name, mat);
        this.singleSlab = singleSlab;
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, 0));
        this.setSoundType(sound);
    }

    public BlockTGSlab getSingleSlab() {
        return singleSlab;
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return Item.getItemFromBlock(this.singleSlab);
    }

    @Override
    public int quantityDropped(@NotNull Random random) {
        return 2;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    public @NotNull ItemStack getItem(@NotNull World worldIn, @NotNull BlockPos pos, IBlockState state) {
        return new ItemStack(this.singleSlab, 1, state.getValue(VARIANT));
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, meta & 7);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public void getSubBlocks(@NotNull CreativeTabs itemIn, @NotNull NonNullList<ItemStack> items) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemBlockModels() {
        Item item = Item.getItemFromBlock(this);
        if (item != net.minecraft.init.Items.AIR) {
            net.minecraft.client.renderer.block.model.ModelBakery.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, stack -> {
                IBlockState state = this.singleSlab.getDefaultState().withProperty(BlockTGSlab.VARIANT, stack.getMetadata());
                return new ModelResourceLocation(this.singleSlab.getRegistryName(), BlockUtils.getBlockStateVariantString(state));
            });
        }
    }
}