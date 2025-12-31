package techguns.client.render;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import techguns.TGArmors;
import techguns.TGItems;
import techguns.Techguns;
import techguns.items.GenericItemShared;
import techguns.items.armors.GenericArmor;
import techguns.items.guns.GenericGrenade;
import techguns.items.guns.GenericGun;

@Mod.EventBusSubscriber(modid = Techguns.MODID, value = Side.CLIENT)
public class TGItemRenderEvents {

    @SubscribeEvent(receiveCanceled = true)
    public static void onRenderLivingPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
        TGItemRendererContext.pushEntity(event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderLivingPreCanceled(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.isCanceled()) {
            TGItemRendererContext.popEntity(event.getEntity());
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void onRenderLivingPost(RenderLivingEvent.Post<EntityLivingBase> event) {
        TGItemRendererContext.popEntity(event.getEntity());
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        GenericGun.guns.forEach(item -> registerBuiltInModel(event, new ModelResourceLocation(item.getRegistryName(), "inventory")));
        GenericGrenade.grenades.forEach(item -> registerBuiltInModel(event, new ModelResourceLocation(item.getRegistryName(), "inventory")));
        GenericItemShared shared = TGItems.SHARED_ITEM;
        if (shared != null) {
            shared.getSharedItems().stream()
                    .filter(GenericItemShared.SharedItemEntry::usesRenderHack)
                    .forEach(entry -> registerBuiltInModel(event, new ModelResourceLocation(new ResourceLocation(Techguns.MODID, entry.getName()), "inventory")));
        }
        TGArmors.armors.stream().filter(GenericArmor::usesRenderHack).forEach(item -> registerBuiltInModel(event, new ModelResourceLocation(item.getModelLocation(), "inventory")));

    }
    private static void registerBuiltInModel(ModelBakeEvent event, ModelResourceLocation loc) {
        IBakedModel original = event.getModelRegistry().getObject(loc);
        if (original != null) {
            event.getModelRegistry().putObject(loc, new TGBuiltInModel(original));
        }
    }
}
