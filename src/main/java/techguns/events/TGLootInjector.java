package techguns.events;

import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techguns.TGItems;
import techguns.Techguns;

import static net.minecraft.world.storage.loot.LootTableList.CHESTS_NETHER_BRIDGE;

@Mod.EventBusSubscriber(modid = Techguns.MODID)
public final class TGLootInjector {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (!CHESTS_NETHER_BRIDGE.equals(event.getName())) return;

        LootTable table = event.getTable();
        final int meta = TGItems.BLUEPRINTS.getItemDamage();

        LootPool pool = new LootPool(
                new LootEntry[] {
                        new LootEntryItem(
                                TGItems.BLUEPRINTS.getItem(),
                                1, 0,
                                new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(meta, meta)) },
                                new LootCondition[0],
                                "tg_blueprint"
                        )
                },
                new LootCondition[] { new RandomChance(0.25F) },
                new RandomValueRange(1),
                new RandomValueRange(0),
                "tg_blueprints"
        );

        table.addPool(pool);
    }
}
