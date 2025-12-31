package techguns.plugins.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techguns.tileentities.operation.GunBenchRecipe;

@ZenClass("mods.techguns.GunBench")
public class GunBenchTweaker {

    private GunBenchTweaker() {
    }


    @ZenMethod
    public static void addRecipe(IItemStack inputBarrel, IItemStack inputSpecial, IItemStack inputReceiver, IItemStack inputMag, IItemStack inputStock, IItemStack output) {
        CraftTweakerAPI.apply(new GunBenchTweaker.addInputAction(CraftTweakerMC.getItemStack(inputBarrel), CraftTweakerMC.getItemStack(inputSpecial), CraftTweakerMC.getItemStack(inputReceiver), CraftTweakerMC.getItemStack(inputMag), CraftTweakerMC.getItemStack(inputStock), CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void addRecipe(String name, IItemStack inputBarrel, IItemStack inputSpecial, IItemStack inputReceiver, IItemStack inputMag, IItemStack inputStock, IItemStack output) {
        CraftTweakerAPI.apply(new GunBenchTweaker.addInputAction(name, CraftTweakerMC.getItemStack(inputBarrel), CraftTweakerMC.getItemStack(inputSpecial), CraftTweakerMC.getItemStack(inputReceiver), CraftTweakerMC.getItemStack(inputMag), CraftTweakerMC.getItemStack(inputStock), CraftTweakerMC.getItemStack(output)));
    }

    private static class addInputAction implements IAction
    {
        String name=null;
        GunBenchRecipe added_recipe=null;
        ItemStack[] input;
        ItemStack output;

        public addInputAction(ItemStack inputBarrel, ItemStack inputSpecial, ItemStack inputReceiver, ItemStack inputMag, ItemStack inputStock, ItemStack output) {
            super();
            this.input = new ItemStack[]{
                    inputBarrel,
                    inputSpecial,
                    inputReceiver,
                    inputMag,
                    inputStock
            };
            this.output=output;
        }

        public addInputAction(String recName, ItemStack inputBarrel, ItemStack inputSpecial, ItemStack inputReceiver, ItemStack inputMag, ItemStack inputStock, ItemStack output) {
            super();
            this.name=recName;
            this.input = new ItemStack[]{
                    inputBarrel,
                    inputSpecial,
                    inputReceiver,
                    inputMag,
                    inputStock
            };
            this.output=output;
        }

        @Override
        public void apply() {
            if(name!=null) this.added_recipe=GunBenchRecipe.addRecipe(name, input, output);
            else this.added_recipe=GunBenchRecipe.addRecipe(input, output);
        }


        @Override
        public String describe() {
            return "Add "+(input)+" to ChargingStation";
        }

    }

}
