package techguns.entities.npcs;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import techguns.capabilities.TGExtendedPlayer;
import techguns.items.guns.GenericGun;

public interface ILivingSoldier {

    double SOUND_SCAN_RANGE = 48.0D;
    double BASE_HEARING_RANGE = 12.0D;
    int SOUND_REACTION_COOLDOWN = 40;

    void tickHearingAI();

    default float getPlayerLoudness(EntityPlayer player) {
        if (player.isSilent()) {
            return 0.0f;
        }
        float loudness = 0.0f;
        double moved = Math.abs(player.distanceWalkedModified - player.prevDistanceWalkedModified);

        if (player.isSprinting() && moved > 0.02D) {
            loudness += 1.2f;
        } else if (moved > 0.02D) {
            loudness += player.isSneaking() ? 0.2f : 0.6f;
        }

        if (!player.onGround && player.motionY < -0.2D) {
            loudness += 0.8f;
        }

        loudness += this.getGunNoise(player);

        return loudness;
    }

    default float getGunNoise(EntityPlayer player) {
        TGExtendedPlayer extended = TGExtendedPlayer.get(player);
        if (extended == null) {
            return 0.0f;
        }
        float noise = 0.0f;
        ItemStack mainhand = player.getHeldItemMainhand();
        ItemStack offhand = player.getHeldItemOffhand();

        if (!mainhand.isEmpty() && mainhand.getItem() instanceof GenericGun) {
            noise += this.scaleGunDelayNoise(extended.getFireDelay(EnumHand.MAIN_HAND));
        }
        if (!offhand.isEmpty() && offhand.getItem() instanceof GenericGun) {
            noise += this.scaleGunDelayNoise(extended.getFireDelay(EnumHand.OFF_HAND)) * 0.8f;
        }
        return noise;
    }

    default float scaleGunDelayNoise(int delay) {
        if (delay <= 0) {
            return 0.0f;
        }
        return MathHelper.clamp(0.4f + (float) delay / 12.0f, 0.4f, 1.6f);
    }
}
