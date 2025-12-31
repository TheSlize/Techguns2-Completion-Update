package techguns.server;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import techguns.TGBlocks;
import techguns.Techguns;
import techguns.tileentities.TGSpawnerTileEnt;

import java.util.ArrayList;
import java.util.List;

public class CommandSetSpawner extends CommandBase {

    @Override
    public String getName() {
        return "setspawner";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/setspawner <mobsLeft> <maxActive> <spawnDelay> <spawnRange> <mob1>:<weight1> [<mob2>:<weight2> ...]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 5) {
            throw new CommandException("Usage: " + getUsage(sender));
        }

        int mobsLeft = parseInt(args[0]);
        int maxActive = parseInt(args[1]);
        int spawnDelay = parseInt(args[2]);
        int spawnRange = parseInt(args[3]);
        int metadata = parseInt(args[4]);

        List<Class> mobClasses = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (int i = 5; i < args.length; i++) {
            int lastColonIndex = args[i].lastIndexOf(':');
            if (lastColonIndex == -1) {
                throw new CommandException("Invalid mob format: " + args[i]);
            }

            String mobName = args[i].substring(0, lastColonIndex);
            String weightStr = args[i].substring(lastColonIndex + 1);

            int weight;
            try {
                weight = Integer.parseInt(weightStr);
            } catch (NumberFormatException e) {
                throw new CommandException("Invalid weight for mob: " + args[i]);
            }

            Class<? extends EntityLiving> mobClass = (Class<? extends EntityLiving>) EntityList.getClass(new ResourceLocation(mobName));
            if (mobClass == null) {
                throw new CommandException("Unknown mob: " + mobName);
            }

            mobClasses.add(mobClass);
            weights.add(weight);
        }

        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();

        IBlockState spawnerState = TGBlocks.MONSTER_SPAWNER.getStateFromMeta(metadata);
        world.setBlockState(pos, spawnerState);

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TGSpawnerTileEnt) {
            TGSpawnerTileEnt spawner = (TGSpawnerTileEnt) tile;

            spawner.setParams(mobsLeft, maxActive, spawnDelay, spawnRange);

            for (int i = 0; i < mobClasses.size(); i++) {
                spawner.addMobType(mobClasses.get(i), weights.get(i));
            }

            System.out.println("Set spawner at " + pos + " with " + mobClasses.size() + " mobs.");
            for(int i = 0; i < mobClasses.size(); i++) {
                System.out.println("Mobs: " + mobClasses.get(i));
            }
        }
    }
}