package techguns.client.render.entities.npcs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import techguns.Techguns;
import techguns.entities.npcs.AttackHelicopter;

import java.io.IOException;

public class RenderAttackHelicopter extends RenderLiving<AttackHelicopter> {

    public static ResourceLocation texture = new ResourceLocation(Techguns.MODID,"textures/entity/apache.png");
	private static final ModelResourceLocation MODEL_LOC0 = new ModelResourceLocation(new ResourceLocation(Techguns.MODID, "helicopter0"), "inventory");
	private static final ModelResourceLocation MODEL_LOC1 = new ModelResourceLocation(new ResourceLocation(Techguns.MODID, "helicopter1"), "inventory");
	private static final ModelResourceLocation MODEL_LOC2 = new ModelResourceLocation(new ResourceLocation(Techguns.MODID, "helicopter2"), "inventory");

	private static IBakedModel HELICOPTER_MAIN;
	private static IBakedModel HELICOPTER_ROTOR;
	private static IBakedModel HELICOPTER_GUN;

	public static void initModels() {
		HELICOPTER_MAIN = loadAndBakeModel(MODEL_LOC0);
		HELICOPTER_ROTOR = loadAndBakeModel(MODEL_LOC1);
		HELICOPTER_GUN = loadAndBakeModel(MODEL_LOC2);
	}

	private static IBakedModel loadAndBakeModel(ModelResourceLocation modelLoc) {
		IModel model = ModelLoaderRegistry.getModelOrLogError(modelLoc, "Could not load model: " + modelLoc);
		return model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, r -> {
            TextureAtlasSprite sprite = new TextureAtlasSprite(texture.getPath()) {};
            try {
                PngSizeInfo png = PngSizeInfo.makeFromResource(Minecraft.getMinecraft().getResourceManager().getResource(texture));
                sprite.loadSprite(png, false);
                sprite.initSprite(png.pngWidth, png.pngHeight, 0, 0, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return sprite;
        });
	}

	public RenderAttackHelicopter(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBase() {}, 5.0f);
	}

	@Override
	protected ResourceLocation getEntityTexture(@NotNull AttackHelicopter entity) {
		return new ResourceLocation(Techguns.MODID,"textures/entity/apache.png");
	}

	@Override
	public void doRender(AttackHelicopter entity, double x, double y, double z, float entityYaw, float partialTicks) {
		RenderHelper.disableStandardItemLighting();
		GlStateManager.pushMatrix();

		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.rotate(-interp(entity.rotationYaw, entity.prevRotationYaw, partialTicks) - 90f, 0.0F, 1.0F, 0.0F);

		float scale = 2.5f;
		GlStateManager.scale(scale, scale, scale);

		this.bindEntityTexture(entity);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();

		if (entity.deathTime == 0) {
			renderBakedModel(tessellator, builder, HELICOPTER_MAIN);

			GlStateManager.pushMatrix();
			float angle = (((Minecraft.getMinecraft().world.getTotalWorldTime() % 60) + partialTicks) * 24.0f);
			GlStateManager.rotate(angle, 0f, 1f, 0f);
			renderBakedModel(tessellator, builder, HELICOPTER_ROTOR);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			float offsetX = 1.2f;
			GlStateManager.translate(offsetX, 0, 0);
			GlStateManager.rotate(-interp(entity.rotationPitch, entity.prevRotationPitch, partialTicks), 0f, 0f, 1f);
			GlStateManager.translate(-offsetX, 0, 0);
			renderBakedModel(tessellator, builder, HELICOPTER_GUN);
			GlStateManager.popMatrix();

		} else {

			float deathProgress = ((float) entity.deathTime + partialTicks) / (float) AttackHelicopter.MAX_DEATH_TIME;
			float p = (float) Math.pow(deathProgress, 2);

			GlStateManager.translate(0, p * -4f, 0);

			float angle = p * 720.0f;
			GL11.glRotatef(angle, 0, 1.0f, 0);
			GlStateManager.rotate(angle, 0, 1f, 0);

			renderBakedModel(tessellator, builder, HELICOPTER_MAIN);
			renderBakedModel(tessellator, builder, HELICOPTER_GUN);

			angle = (((Minecraft.getMinecraft().world.getTotalWorldTime() % 60) + partialTicks) * 12.0f);
			GlStateManager.rotate(angle, 0, 1f, 0);
			renderBakedModel(tessellator, builder, HELICOPTER_ROTOR);
		}

		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}

	private void renderBakedModel(Tessellator tessellator, BufferBuilder buffer, IBakedModel model) {
		buffer.begin(GL11.GL_QUADS, Attributes.DEFAULT_BAKED_FORMAT);
		for (BakedQuad bakedQuad : model.getQuads(null, null, 0))
			buffer.addVertexData(bakedQuad.getVertexData());
		tessellator.draw();
	}

	public float interp(float val, float prev_value, float ptt) {
		return prev_value + (val - prev_value) * ptt;
	}
}
