package techguns;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import techguns.blocks.EnumOreClusterType;
import techguns.blocks.EnumOreType;
import techguns.blocks.IEnumOreClusterType;
import techguns.init.ITGInitializer;
import techguns.tileentities.operation.MachineOperation;

public class TGOreClusters implements ITGInitializer {
    protected HashMap<EnumOreClusterType, OreCluster> registry = new HashMap<>();

    public OreCluster getClusterForType(IEnumOreClusterType type) {
        return registry.get(type);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registry.put(EnumOreClusterType.COAL, new OreCluster(TGConfig.oreDrills.mininglevel_coal, TGConfig.oreDrills.oremult_coal, TGConfig.oreDrills.powermult_coal));
        registry.put(EnumOreClusterType.COMMON_METAL, new OreCluster(TGConfig.oreDrills.mininglevel_common_metal, TGConfig.oreDrills.oremult_common_metal, TGConfig.oreDrills.powermult_common_metal));
        registry.put(EnumOreClusterType.RARE_METAL, new OreCluster(TGConfig.oreDrills.mininglevel_rare_metal, TGConfig.oreDrills.oremult_rare_metal, TGConfig.oreDrills.powermult_rare_metal));
        registry.put(EnumOreClusterType.SHINY_METAL, new OreCluster(TGConfig.oreDrills.mininglevel_shiny_metal, TGConfig.oreDrills.oremult_shiny_metal, TGConfig.oreDrills.powermult_shiny_metal));
        registry.put(EnumOreClusterType.URANIUM, new OreCluster(TGConfig.oreDrills.mininglevel_uranium, TGConfig.oreDrills.oremult_uranium, TGConfig.oreDrills.powermult_uranium));
        registry.put(EnumOreClusterType.COMMON_GEM, new OreCluster(TGConfig.oreDrills.mininglevel_common_gem, TGConfig.oreDrills.oremult_common_gem, TGConfig.oreDrills.powermult_common_gem));
        registry.put(EnumOreClusterType.SHINY_GEM, new OreCluster(TGConfig.oreDrills.mininglevel_shiny_gem, TGConfig.oreDrills.oremult_shiny_gem, TGConfig.oreDrills.powermult_shiny_gem));
        registry.put(EnumOreClusterType.NETHER_CRYSTAL, new OreCluster(TGConfig.oreDrills.mininglevel_nether_crystal, TGConfig.oreDrills.oremult_nether_crystal, TGConfig.oreDrills.powermult_nether_crystal));
        registry.put(EnumOreClusterType.OIL, new OreCluster(TGConfig.oreDrills.mininglevel_oil, TGConfig.oreDrills.oremult_oil, TGConfig.oreDrills.powermult_oil));
    }

    public void RecipeInit() {
        if (TGConfig.oreDrills.addDefaultClusterOres) {
            this.addOreToCluster(new ItemStack(Blocks.COAL_ORE), EnumOreClusterType.COAL, 99);
            this.addOreToCluster(new ItemStack(Items.DIAMOND), EnumOreClusterType.COAL, 1);

            this.addOreToCluster(new ItemStack(Blocks.IRON_ORE), EnumOreClusterType.COMMON_METAL, 45);
            this.addOreToCluster(new ItemStack(TGBlocks.TG_ORE, 1, EnumOreType.ORE_COPPER.ordinal()), EnumOreClusterType.COMMON_METAL, 30);
            this.addOreToCluster(new ItemStack(TGBlocks.TG_ORE, 1, EnumOreType.ORE_TIN.ordinal()), EnumOreClusterType.COMMON_METAL, 25);

            this.addOreToCluster(new ItemStack(TGBlocks.TG_ORE, 1, EnumOreType.ORE_LEAD.ordinal()), EnumOreClusterType.RARE_METAL, 50);

            this.addOreToCluster(new ItemStack(TGBlocks.TG_ORE, 1, EnumOreType.ORE_TITANIUM.ordinal()), EnumOreClusterType.SHINY_METAL, 20);
            this.addOreToCluster(new ItemStack(Blocks.GOLD_ORE), EnumOreClusterType.SHINY_METAL, 40);

            this.addOreToCluster(new ItemStack(Blocks.REDSTONE_ORE), EnumOreClusterType.COMMON_GEM, 40);
            this.addOreToCluster(new ItemStack(Blocks.LAPIS_ORE), EnumOreClusterType.COMMON_GEM, 20);

            this.addOreToCluster(new ItemStack(Blocks.DIAMOND_ORE), EnumOreClusterType.SHINY_GEM, 40);
            this.addOreToCluster(new ItemStack(Blocks.EMERALD_ORE), EnumOreClusterType.SHINY_GEM, 20);

            this.addOreToCluster(new ItemStack(TGBlocks.TG_ORE, 1, EnumOreType.ORE_URANIUM.ordinal()), EnumOreClusterType.URANIUM, 50);

            this.addOreToCluster(new ItemStack(Blocks.QUARTZ_ORE), EnumOreClusterType.NETHER_CRYSTAL, 50);
            this.addOreToCluster(new ItemStack(Blocks.GLOWSTONE), EnumOreClusterType.NETHER_CRYSTAL, 40);
            this.addOreToCluster(new ItemStack(Items.BLAZE_ROD), EnumOreClusterType.NETHER_CRYSTAL, 10);

            if (!TGFluids.worldspawn_oils.isEmpty()) {
                //prefer oil which has a block implementation
                if (TGFluids.OIL_WORLDSPAWN != null) {
                    this.addOreToCluster(new FluidStack(TGFluids.OIL_WORLDSPAWN, 1000), EnumOreClusterType.OIL, 10);
                    //otherwise use first oil
                } else
                    this.addOreToCluster(new FluidStack(TGFluids.worldspawn_oils.get(0), 1000), EnumOreClusterType.OIL, 10);
            }

            //add modded ores from oreDict
            this.addOreToCluster("oreSilver", EnumOreClusterType.SHINY_METAL, 40);

            this.addOreToCluster("oreOsmium", EnumOreClusterType.RARE_METAL, 50);
            this.addOreToCluster("oreAluminium", EnumOreClusterType.RARE_METAL, 50);

            this.addOreToCluster("oreCertusQuartz", EnumOreClusterType.COMMON_GEM, 40);
            this.addOreToCluster("oreChargedCertusQuartz", EnumOreClusterType.COMMON_GEM, 5);
        }

        for (String s : TGConfig.oreDrills.additionalClusterOres) {
            if (s == null) continue;
            s = s.trim();
            if (s.isEmpty()) continue;

            String[] parts = s.split(";");
            if (parts.length != 4) continue;

            String oredict = parts[0].trim();
            String typeStr = parts[1].trim();
            String weightStr = parts[2].trim();
            String amountStr = parts[3].trim();

            if (oredict.isEmpty() || typeStr.isEmpty() || weightStr.isEmpty() || amountStr.isEmpty()) continue;

            EnumOreClusterType type = EnumOreClusterType.getFromString(typeStr);
            if (type == null) continue;

            try {
                int weight = Integer.parseInt(weightStr);
                int amount = Integer.parseInt(amountStr);
                if (weight <= 0 || amount <= 0) continue;

                this.addOreToCluster(oredict, type, weight, amount);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void removeOreFromCluster(ItemStack ore, EnumOreClusterType type) {
        OreCluster c = this.registry.get(type);
        if (c != null) {
            OreClusterWeightedEntry entry = new OreClusterWeightedEntry(ore, 1);
            c.oreEntries.remove(entry);
        }
    }

    public void removeOreFromCluster(FluidStack fluid, EnumOreClusterType type) {
        OreCluster c = this.registry.get(type);
        if (c != null) {
            OreClusterWeightedEntry entry = new OreClusterWeightedEntry(fluid, 1);
            c.oreEntries.remove(entry);
        }
    }

    public void addOreToCluster(ItemStack ore, EnumOreClusterType type, int weight) {
        OreCluster c = this.registry.get(type);
        if (c != null) {
            ItemStack ore2 = ore.copy();
            OreClusterWeightedEntry entry = new OreClusterWeightedEntry(ore2, weight);
            if (!c.oreEntries.contains(entry)) {
                c.oreEntries.add(entry);
            }
        }
    }

    public void addOreToCluster(FluidStack fluid, EnumOreClusterType type, int weight) {
        OreCluster c = this.registry.get(type);
        if (c != null) {
            FluidStack fluid2 = fluid.copy();
            fluid2.amount = Fluid.BUCKET_VOLUME;
            OreClusterWeightedEntry entry = new OreClusterWeightedEntry(fluid2, weight);
            if (!c.oreEntries.contains(entry)) {
                c.oreEntries.add(entry);
            }
        }
    }

    public void addOreToCluster(String oredict, EnumOreClusterType type, int weight) {
        OreCluster c = this.registry.get(type);
        if (c != null) {
            OreClusterWeightedEntry entry = new OreClusterWeightedEntry(oredict, weight);
            if (!entry.isEmpty() && !c.oreEntries.contains(entry)) {
                c.oreEntries.add(entry);
            }
        }
    }

    public void addOreToCluster(String oredict, EnumOreClusterType type, int weight, int amount) {
        OreCluster c = this.registry.get(type);
        if (c != null) {
            OreClusterWeightedEntry entry = new OreClusterWeightedEntry(oredict, weight, amount);
            if (!entry.isEmpty() && !c.oreEntries.contains(entry)) {
                c.oreEntries.add(entry);
            }
        }
    }

    public static class OreClusterWeightedEntry extends WeightedRandom.Item {
        ItemStack ore = ItemStack.EMPTY;
        FluidStack fluid = null;
        String oredictname = null;

        public OreClusterWeightedEntry(ItemStack ore, int itemWeightIn) {
            super(itemWeightIn);
            this.ore = ore;
        }

        public OreClusterWeightedEntry(FluidStack fluid, int itemWeightIn) {
            super(itemWeightIn);
            this.fluid = fluid;
        }

        public OreClusterWeightedEntry(String oredictname, int itemWeightIn) {
            super(itemWeightIn);
            NonNullList<ItemStack> ores = OreDictionary.getOres(oredictname);
            if (!ores.isEmpty()) {
                this.ore = ores.get(0).copy();
            }
        }

        public OreClusterWeightedEntry(String oredictname, int itemWeightIn, int amount) {
            super(itemWeightIn);
            NonNullList<ItemStack> ores = OreDictionary.getOres(oredictname);
            if (!ores.isEmpty()) {
                this.ore = ores.get(0).copy();
                ore.setCount(amount);
            }
        }

        public boolean isEmpty() {
            return ore.isEmpty() && fluid == null;
        }

        public ItemStack getOre() {
            return ore;
        }

        public FluidStack getFluid() {
            return fluid;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof OreClusterWeightedEntry other) {
                if (this.oredictname != null && other.oredictname != null) {
                    return this.oredictname.equals(other.oredictname);

                } else if (!this.ore.isEmpty() && !other.ore.isEmpty()) {
                    return this.ore.getItem() == other.ore.getItem() && this.ore.getItemDamage() == other.ore.getItemDamage();

                } else if (this.fluid != null && other.fluid != null) {
                    return this.fluid.getFluid() == other.fluid.getFluid();
                }
            }
            return this == obj;
        }


    }

    public static class OreCluster {
        int mininglevel;
        double multiplier_amount;
        double multiplier_power;

        ArrayList<OreClusterWeightedEntry> oreEntries = new ArrayList<>();

        public OreCluster(int mininglevel, double multiplier_amount, double multiplier_power) {
            super();
            this.mininglevel = mininglevel;
            this.multiplier_amount = multiplier_amount;
            this.multiplier_power = multiplier_power;
        }

        public int getMininglevel() {
            return mininglevel;
        }

        public double getMultiplier_amount() {
            return multiplier_amount;
        }

        public double getMultiplier_power() {
            return multiplier_power;
        }

        public ArrayList<OreClusterWeightedEntry> getOreEntries() {
            return oreEntries;
        }

        public MachineOperation getNewOperation(World w, double orePerHour, int radius, double powerMult) {
            if (!oreEntries.isEmpty()) {
                OreClusterWeightedEntry entry = WeightedRandom.getRandomItem(w.rand, oreEntries);

                MachineOperation op = null;

                if (!entry.ore.isEmpty()) {
                    op = new MachineOperation(entry.ore.copy(), null, new ItemStack[0]);
                } else if (entry.fluid != null) {
                    op = new MachineOperation(ItemStack.EMPTY, new FluidStack(entry.fluid.getFluid(), Fluid.BUCKET_VOLUME));
                }
                if (op != null) {
                    op.setPowerPerTick((int) (8 * orePerHour * (1 + Math.max(radius - 1, 0) * 0.2) * powerMult * TGConfig.oreDrills.oreDrillMultiplierPower));
                    return op;
                }

            }
            return getCobbleStoneOperation();
        }

        public MachineOperation getCobbleStoneOperation() {
            MachineOperation op = new MachineOperation(new ItemStack(Blocks.COBBLESTONE));
            op.setPowerPerTick((int) (24 * TGConfig.oreDrills.oreDrillMultiplierPower));
            return op;
        }
    }
}
