package techguns.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TGBuiltInModel implements IBakedModel {

    private final IBakedModel original;

    public TGBuiltInModel(@Nullable IBakedModel original) {
        this.original = original;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return original != null ? original.getQuads(state, side, rand) : Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return original != null ? original.getParticleTexture() : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return original != null ? original.getOverrides() : ItemOverrideList.NONE;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        TGItemRendererContext.setTransform(cameraTransformType);
        Matrix4f identity = new Matrix4f();
        identity.setIdentity();
        return Pair.of(this, identity);
    }
}
