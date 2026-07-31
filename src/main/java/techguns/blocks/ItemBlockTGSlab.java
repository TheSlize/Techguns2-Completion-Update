package techguns.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

// Th3_Sl1ze: I hate this atrocious shit, gotta try to replace it with something neater later
public class ItemBlockTGSlab extends GenericItemBlockMeta {

    private final BlockTGSlab singleSlab;
    private final BlockTGDoubleSlab doubleSlab;

    public ItemBlockTGSlab(BlockTGSlab singleSlab, BlockTGDoubleSlab doubleSlab) {
        super(singleSlab);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if (iblockstate.getBlock() == this.singleSlab) {
                BlockSlab.EnumBlockHalf half = iblockstate.getValue(BlockTGSlab.HALF);
                int variant = iblockstate.getValue(BlockTGSlab.VARIANT);
                int itemVariant = itemstack.getMetadata();

                if ((facing == EnumFacing.UP && half == BlockSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && half == BlockSlab.EnumBlockHalf.TOP) && variant == itemVariant) {
                    IBlockState doubleState = this.doubleSlab.getDefaultState().withProperty(BlockTGDoubleSlab.VARIANT, variant);
                    AxisAlignedBB axisalignedbb = doubleState.getCollisionBoundingBox(worldIn, pos);

                    if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, doubleState, 11)) {
                        SoundType soundtype = this.doubleSlab.getSoundType(doubleState, worldIn, pos, player);
                        worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        itemstack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.tryPlace(player, itemstack, worldIn, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, @NotNull BlockPos pos, @NotNull EnumFacing side, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        BlockPos blockpos = pos;
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this.singleSlab) {
            boolean isTop = iblockstate.getValue(BlockTGSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
            int variant = iblockstate.getValue(BlockTGSlab.VARIANT);
            int itemVariant = stack.getMetadata();

            if ((side == EnumFacing.UP && !isTop || side == EnumFacing.DOWN && isTop) && variant == itemVariant) {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState iblockstate1 = worldIn.getBlockState(pos);
        return iblockstate1.getBlock() == this.singleSlab && iblockstate1.getValue(BlockTGSlab.VARIANT) == stack.getMetadata() || super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
    }

    private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this.singleSlab) {
            int variant = iblockstate.getValue(BlockTGSlab.VARIANT);
            int itemVariant = stack.getMetadata();

            if (variant == itemVariant) {
                IBlockState doubleState = this.doubleSlab.getDefaultState().withProperty(BlockTGDoubleSlab.VARIANT, variant);
                AxisAlignedBB axisalignedbb = doubleState.getCollisionBoundingBox(worldIn, pos);

                if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, doubleState, 11)) {
                    SoundType soundtype = this.doubleSlab.getSoundType(doubleState, worldIn, pos, player);
                    worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    stack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }
}