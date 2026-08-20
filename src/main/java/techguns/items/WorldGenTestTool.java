package techguns.items;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import techguns.Techguns;
import techguns.world.StructureRegistry;
import techguns.world.structures.WorldgenStructure;

public class WorldGenTestTool extends GenericItem {

    public WorldGenTestTool(String name) {
        this(name, true);
    }

    public WorldGenTestTool(String name, boolean addItemToList) {
        super(name, addItemToList);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack item = player.getHeldItem(hand);

        if (!item.hasTagCompound()) {
            item.setTagCompound(new NBTTagCompound());
        }

        String structureName = item.getTagCompound().getString("structure");

        if (player.isSneaking() || structureName.isEmpty()) {
            Techguns.proxy.openWorldGenTestToolGui();
            return new ActionResult<>(EnumActionResult.SUCCESS, item);
        }

        // Spawn structure on server
        if (!world.isRemote) {
            spawnStructure(world, player, structureName);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, item);
    }

    private void spawnStructure(World world, EntityPlayer player, String structureName) {
        StructureRegistry.init();
        WorldgenStructure structure = StructureRegistry.createStructure(structureName);

        if (structure != null) {
            BlockPos pos = player.getPosition();
            Random rnd = new Random();
            Biome biome = world.getBiome(pos);

            int sizeX = structure.getSizeX(rnd);
            int sizeY = structure.getSizeY(rnd);
            int sizeZ = structure.getSizeZ(rnd);

            structure.setBlocks(world, pos.getX(), pos.getY() - 1, pos.getZ(),
                    sizeX, sizeY, sizeZ, 0,
                    StructureRegistry.getBiomeColorType(biome), rnd);

            player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Spawned: " + TextFormatting.YELLOW + structureName));
        } else {
            player.sendMessage(new TextComponentString(TextFormatting.RED + "Structure not found: " + structureName));
        }
    }
}