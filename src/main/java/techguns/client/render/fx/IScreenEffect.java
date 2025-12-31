package techguns.client.render.fx;

import techguns.client.render.TGRenderHelper.RenderType;
import techguns.client.render.fx.ScreenEffect.FadeType;

public interface IScreenEffect {
	
	//public static ScreenEffect muzzleFlash = new ScreenEffect("textures/guns/handgun.png", 2, 2, 4);
	//public static ScreenEffect muzzleFlashYellow = new ScreenEffect("textures/fx/muzzleFlashYellow2x2.png", 2, 2, 4, RenderType.SOLID);
    IScreenEffect muzzleFlashLaser = new ScreenEffect("textures/fx/laserflare02.png", 4, 4, 7, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashLightningOld = new ScreenEffect("textures/fx/lightningflare4x4_2.png", 4, 4, 16, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashFireball_alpha = new ScreenEffect("textures/fx/fireball_4x4_alpha.png", 4, 4, 16, RenderType.ALPHA);
	IScreenEffect muzzleFlashFireball_add = new ScreenEffect("textures/fx/fireball_4x4_add.png", 4, 4, 16, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashBlaster = new ScreenEffect("textures/fx/blasterflare01.png", 4, 4, 14, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashAlienBlaster = new ScreenEffect("textures/fx/alienflare.png", 4, 4, 14, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashLightning = new ScreenEffect("textures/fx/teslaflare01.png", 4, 4, 14, RenderType.ADDITIVE);
	IScreenEffect FlamethrowerMuzzleFlame = new ScreenEffect("textures/fx/flamethrower2.png", 4, 4, 16, RenderType.ADDITIVE).setFlipAxis(false, true);
	IScreenEffect FlamethrowerMuzzleFlash = new ScreenEffect("textures/fx/flamethrower.png", 4, 4, 16, RenderType.ADDITIVE).setFlipAxis(false, true);//.setFade(FadeType.SMOOTH);
	IScreenEffect muzzleFlashSonic = new ScreenEffect("textures/fx/sonicwave4x4.png", 4, 4, 16, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashNukeBeam = new ScreenEffect("textures/fx/nukebeamflare.png", 4, 4, 16, RenderType.ADDITIVE);
	IScreenEffect muzzleFlashMibGun = new ScreenEffect("textures/fx/alienflare.png", 4, 4, 14, RenderType.ADDITIVE).setColor(0.15686f, 1.0f, 0.549f, 1f);

	
	IScreenEffect muzzleFlashTFG_glow = new ScreenEffect("textures/fx/lensflare1.png", 1, 1, 1, RenderType.ADDITIVE).setFade(FadeType.SMOOTH).setColor(0.25f, 1.0f, 0.25f, 0.75f);
	IScreenEffect muzzleFlashTFG_flash = new ScreenEffect("textures/fx/lensflare4.png", 1, 1, 1, RenderType.ADDITIVE).setFade(FadeType.FAST).setColor(0.25f, 1.0f, 0.25f, 1.0f);
	IScreenEffect muzzleFlashTFG_main = new ScreenEffect("textures/fx/tfg_flare.png", 4, 4, 16, RenderType.ADDITIVE).setFade(FadeType.FAST);
	
	IScreenEffect muzzleFlashTFG = new MultiScreenEffect().add(muzzleFlashTFG_main, 0, 0.75f, 1.0f).add(muzzleFlashTFG_glow, 0, 1.0f, 0.75f).add(muzzleFlashTFG_flash, 0, 0.35f, 1.35f);
	
	//ScreenEffect muzzleFlashNew2 = new ScreenEffect("textures/fx/muzzleflashnew.png", 4, 4, 16, RenderType.ALPHA);
	
	//<Proper Generic Gun muzzle flashes that should be in use>
	IScreenEffect muzzleFlash_rifle = new ScreenEffect("textures/fx/muzzleflashnew_add.png", 4, 4, 12, RenderType.ADDITIVE); //.setJitter(0.1f, 0.1f, 0.1f, 0.1f);
	IScreenEffect muzzleFlash_gun = new ScreenEffect("textures/fx/muzzleflashnew_2_add_2.png", 4, 4, 11, RenderType.ADDITIVE);
	IScreenEffect muzzleFlash_minigun = new ScreenEffect("textures/fx/muzzleflash_minigun.png", 2, 2, 4, RenderType.ADDITIVE);
	IScreenEffect muzzleFlash_blue = new ScreenEffect("textures/fx/bluemuzzleflash.png", 4, 4, 8, RenderType.ADDITIVE).setFlipAxis(true, true);
    //</Proper Generic Gun muzzle flashes that should be in use>
	
	//<Old gun muzzle flashes>
	@Deprecated
	IScreenEffect muzzleFlash = new ScreenEffect("textures/fx/muzzleflash4x4.png", 4, 4, 10, RenderType.SOLID);
	@Deprecated
	IScreenEffect muzzleFlash2 = new ScreenEffect("textures/fx/muzzleflash2.png", 4, 4, 8, RenderType.SOLID);
	@Deprecated
	IScreenEffect muzzleFlashNew = new ScreenEffect("textures/fx/muzzleflashnew_2.png", 4, 4, 16, RenderType.ALPHA);
	//</Old gun muzzle flashes>
	
    IScreenEffect muzzleGreenFlare = new ScreenEffect("textures/fx/lensflare1.png", 1, 1, 1, RenderType.ADDITIVE).setFade(FadeType.SMOOTH).setColor(0.5f, 1.0f, 0.25f, 1.0f);
	IScreenEffect sniperScope = new ScreenEffect("textures/fx/testscope.png",1,1,1, RenderType.ALPHA);
	IScreenEffect techScope = new ScreenEffect("textures/fx/techscope.png",1,1,1, RenderType.ALPHA).setFlipAxis(false, true);

    default void doRender(float progress, float offsetX, float offsetY, float offsetZ, float scale, boolean is3p) {
		doRender(progress, offsetX + 0.5f, offsetY + 0.5f, offsetZ + 0.5f, scale, 0, 0, 0, is3p);
	}

	void doRender(float progress, float offsetX, float offsetY, float offsetZ, float scale, float rot_x, float rot_y,
			float rot_z, boolean is3p);

}