package techguns.client.render.entities.projectiles;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import techguns.Techguns;
import techguns.client.render.TGRenderHelper;
import techguns.client.render.TGRenderHelper.RenderType;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.util.MathUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Random;

public class DeathEffectEntityRenderer {
	private static final ResourceLocation RES_BIO_EFFECT = new ResourceLocation(Techguns.MODID,"textures/fx/bio.png");
	private static final ResourceLocation RES_LASER_EFFECT = new ResourceLocation(Techguns.MODID,"textures/fx/laserdeath.png");
	private static final int MAX_DEATH_TIME = 20;

	public static Field RLB_mainModel = ReflectionHelper.findField(RenderLivingBase.class, "mainModel", "field_77045_g");
	protected static Method RLB_preRenderCallback = ReflectionHelper.findMethod(RenderLivingBase.class, "preRenderCallback", "func_77041_b", EntityLivingBase.class, float.class);
	protected static Method R_bindEntityTexture = ReflectionHelper.findMethod(Render.class, "bindEntityTexture", "func_180548_c", Entity.class);

	public static Field R_renderManager = ReflectionHelper.findField(Render.class, "renderManager","field_76990_c");
	
	public static void preRenderCallback(RenderLivingBase<? extends EntityLivingBase> renderer, EntityLivingBase elb, float ptt) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		RLB_preRenderCallback.invoke(renderer, elb, ptt);
	}

	public static void bindEntityTexture(Render<? extends Entity> renderer, Entity entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		R_bindEntityTexture.invoke(renderer, entity);
	}
	
	public static void setRenderScalingForEntity(EntityLivingBase elb){
		if (elb instanceof EntitySlime){
			EntitySlime slime = (EntitySlime)elb;
			int size = slime.getSlimeSize();
			GlStateManager.scale((float)size, (float)size, (float)size);
			
			//slimes are 1,2 and 4
			if (size==2){
				GlStateManager.translate(0, -0.8f, 0);
			} else if (size==4){
				GlStateManager.translate(0, -1.2f, 0);
			}
		}
		
	}
	/**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
	public static void doRender(RenderLivingBase renderer, EntityLivingBase entity, double x, double y, double z, float ptt, DeathType deathType)
	{

		GlStateManager.pushMatrix();
        GlStateManager.disableCull();
	        ModelBase mainModel = null;
	        try {
	        	mainModel = (ModelBase)RLB_mainModel.get(renderer);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        mainModel.isChild = entity.isChild();

	        try
	        {
	            float f2 = MathUtil.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, ptt);
	            float f3 = MathUtil.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, ptt);
	            float f4;        

	            float f13 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * ptt;
	            
	            GlStateManager.translate((float)x, (float)y, (float)z);
	            
	            f4 = (float)entity.ticksExisted + ptt;
	                      
	            float f5 = 0.0625F;
	            GlStateManager.enableRescaleNormal();
	            GlStateManager.scale(-1.0f, -1.0f, -1.0f);
	            
	            GlStateManager.translate(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
	            
	            float f6 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * ptt;
	            float f7 = entity.limbSwing - entity.limbSwingAmount * (1.0F - ptt);

	            if (entity.isChild())
	            {
	                f7 *= 3.0F;
	            }

	            if (f6 > 1.0F)
	            {
	                f6 = 1.0F;
	            }

	            GlStateManager.enableAlpha();
	            
	            switch(deathType) {
				case BIO:
					mainModel.setLivingAnimations(entity, f7, f6, ptt);
					preRenderCallback(renderer, entity, ptt);
					renderModelDeathBio(renderer, entity, f7, f6, f4, f3 - f2, f13, f5);
					break;
				case LASER:
					mainModel.setLivingAnimations(entity, f7, f6, ptt);
					preRenderCallback(renderer, entity, ptt);
					renderModelDeathLaser(renderer, entity, f7, f6, f4, f3 - f2, f13, f5);
					break;
				case GORE:
				case DEFAULT:
				default:
					break;
	            
	            }
	            GlStateManager.disableRescaleNormal();
	            
	        } catch (Exception exception) {
	        }

	        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	        GlStateManager.enableTexture2D();
	        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	        GlStateManager.enableCull();
	        GlStateManager.popMatrix();
	}
	
	//------------
	
	/**
     * Renders the model in RenderLiving
     */
    static void renderModelDeathBio(RenderLivingBase renderer, EntityLivingBase entity, float f7, float f6, float f4, float p_77036_5_, float f13, float f5)
    {
    	float prog = ((float)entity.deathTime / (float)MAX_DEATH_TIME); 
    	
    	Random rand = new Random(entity.getEntityId());
    	RenderType renderType = RenderType.ADDITIVE;
    	ModelBase mainModel = null;
    	RenderManager renderManager = null;
    	try {
    		mainModel = (ModelBase)RLB_mainModel.get(renderer);
    		renderManager = (RenderManager)R_renderManager.get(renderer);
           	R_bindEntityTexture.invoke(renderer, entity);
        	
        }catch (Exception e) {
        	e.printStackTrace();
        }
        
    	if (mainModel instanceof ModelBiped) {
    		mainModel.setRotationAngles(f7, f6, f4, p_77036_5_, f13, f5, entity);
    	}
    	
    	HashSet<ModelRenderer> childBoxes = new HashSet<>(64);
        for (ModelRenderer o : mainModel.boxList) {
			if (o.childModels != null) {
        		childBoxes.addAll(o.childModels);
        	}
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
                
        setRenderScalingForEntity(entity);
        
        for (ModelRenderer o : mainModel.boxList) {
			if (!childBoxes.contains(o) && !o.isHidden && o.showModel) {
        		float scale = 1.0f+(rand.nextFloat()*prog);
        		GlStateManager.pushMatrix();
        		GlStateManager.translate(-o.offsetX, -o.offsetY, -o.offsetZ);
        		GlStateManager.scale(scale, scale, scale);
        		GlStateManager.translate(o.offsetX, o.offsetY, o.offsetZ);
        		double mainColor = 1.0-Math.pow(prog, 2.0);
        		double mainAlpha = Math.pow(1.0-prog, 2.0);
        		GlStateManager.color((float)mainColor, 1.0f, (float)mainColor, (float)mainAlpha);
        		o.render(f5);
        		renderManager.renderEngine.bindTexture(RES_BIO_EFFECT);
            	TGRenderHelper.enableBlendMode(renderType);
            	double overlayColor = 0.5+(Math.sin((Math.sqrt(prog)+0.75)*2.0*Math.PI)/2);
            	GlStateManager.color((float)overlayColor, (float)overlayColor, (float)overlayColor);
        		o.render(f5);
                TGRenderHelper.disableBlendMode(renderType);
                GlStateManager.popMatrix();
        	}
        }
        
        GlStateManager.popMatrix();
    }
	
	/**
     * Renders the model in RenderLiving
     */
    static void renderModelDeathLaser(RenderLivingBase renderer, EntityLivingBase entity, float f7, float f6, float f4, float p_77036_5_, float f13, float f5)
    {
    	float prog = ((float)entity.deathTime / (float)MAX_DEATH_TIME);
    	RenderType renderType = RenderType.ADDITIVE;
    	ModelBase mainModel = null;
    	RenderManager renderManager = null;
    	try {
    		mainModel = (ModelBase)RLB_mainModel.get(renderer);
    		renderManager = (RenderManager)R_renderManager.get(renderer);
           	R_bindEntityTexture.invoke(renderer, entity);
        	
        }catch (Exception e) {
        	e.printStackTrace();
        }
        
    	if (mainModel instanceof ModelBiped) {
    		mainModel.setRotationAngles(f7, f6, f4, p_77036_5_, f13, f5, entity);
    	}
        
        GlStateManager.pushMatrix();
        GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
        
        setRenderScalingForEntity(entity);

		double mainColor = 1.0-Math.pow(prog, 2.0);
		double mainAlpha = Math.pow(1.0-prog, 2.0);
		GlStateManager.color(1.0f, (float)mainColor, (float)mainColor, (float)mainAlpha);
		mainModel.render(entity, f7, f6, f4, p_77036_5_, f13, f5);
		renderManager.renderEngine.bindTexture(RES_LASER_EFFECT);
    	TGRenderHelper.enableBlendMode(renderType);
    	double overlayColor = 0.5+(Math.sin((Math.sqrt(prog)+0.75)*2.0*Math.PI)/2);
    	GlStateManager.color((float)overlayColor, (float)overlayColor, (float)overlayColor);
    	mainModel.render(entity, f7, f6, f4, p_77036_5_, f13, f5);
        TGRenderHelper.disableBlendMode(renderType);
 
        GlStateManager.popMatrix();
        
    }
}
