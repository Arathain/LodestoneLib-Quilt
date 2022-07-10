package com.sammy.ortus.systems.fireeffect;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.sammy.ortus.config.ClientConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public abstract class FireEffectRenderer<T extends FireEffectInstance> {

	public boolean canRender(T instance) {
		return true;
	}

	public SpriteIdentifier getFirstFlame() {
		return null;
	}

	public SpriteIdentifier getSecondFlame() {
		return null;
	}

	public void renderScreen(T instance, MinecraftClient minecraft, MatrixStack matrices) {
		matrices.push();
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBufferBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.depthFunc(519);
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableTexture();
		Sprite sprite = getFirstFlame().getSprite();
		RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
		float f = sprite.getMinU();
		float f1 = sprite.getMaxU();
		float f2 = (f + f1) / 2.0F;
		float f3 = sprite.getMinV();
		float f4 = sprite.getMaxV();
		float f5 = (f3 + f4) / 2.0F;
		float f6 = sprite.getAnimationFrameDelta();
		float f7 = MathHelper.lerp(f6, f, f2);
		float f8 = MathHelper.lerp(f6, f1, f2);
		float f9 = MathHelper.lerp(f6, f3, f5);
		float f10 = MathHelper.lerp(f6, f4, f5);

		for (int i = 0; i < 2; ++i) {
			matrices.push();
			matrices.translate(((float) (-(i * 2 - 1)) * 0.24F), -0.3F, 0.0D);
			matrices.translate(0, -(ClientConfig.FIRE_OVERLAY_OFFSET) * 0.3f, 0);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (i * 2 - 1) * 10.0F));
			Matrix4f matrix4f = matrices.peek().getPosition();
			bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
			bufferbuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f8, f10).next();
			bufferbuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f7, f10).next();
			bufferbuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f7, f9).next();
			bufferbuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f8, f9).next();
			BufferRenderer.draw(bufferbuilder.end());
			matrices.pop();
		}

		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.depthFunc(515);
		matrices.pop();
	}

	public void renderWorld(T instance, MatrixStack pMatrixStack, VertexConsumerProvider provider, Camera camera, Entity entity) {
		Sprite textureAtlasSprite0 = getFirstFlame().getSprite();
		Sprite textureAtlasSprite1 = getSecondFlame().getSprite();
		pMatrixStack.push();
		float f = entity.getWidth() * 1.4F;
		pMatrixStack.scale(f, f, f);
		float f1 = 0.5F;
		float f3 = entity.getHeight() / f;
		float f4 = 0.0F;
		pMatrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-camera.getYaw()));
		pMatrixStack.translate(0.0D, 0.0D, -0.3F + (float) ((int) f3) * 0.02F);
		float f5 = 0.0F;
		int i = 0;
		VertexConsumer vertexconsumer = provider.getBuffer(TexturedRenderLayers.getEntityCutout());

		for (MatrixStack.Entry last = pMatrixStack.peek(); f3 > 0.0F; ++i) {
			Sprite finalSprite = i % 2 == 0 ? textureAtlasSprite0 : textureAtlasSprite1;
			float f6 = finalSprite.getMinU();
			float f7 = finalSprite.getMinV();
			float f8 = finalSprite.getMaxU();
			float f9 = finalSprite.getMaxV();
			if (i / 2 % 2 == 0) {
				float f10 = f8;
				f8 = f6;
				f6 = f10;
			}

			fireVertex(last, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
			fireVertex(last, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
			fireVertex(last, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f6, f7);
			fireVertex(last, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f8, f7);
			f3 -= 0.45F;
			f4 -= 0.45F;
			f1 *= 0.9F;
			f5 += 0.03F;
		}

		pMatrixStack.pop();
	}

	protected static void fireVertex(MatrixStack.Entry matrixEntry, VertexConsumer pBuffer, float pX, float pY, float pZ, float pTexU, float pTexV) {
		pBuffer.vertex(matrixEntry.getPosition(), pX, pY, pZ).color(255, 255, 255, 255).uv(pTexU, pTexV).overlay(0, 10).light(240).normal(matrixEntry.getNormal(), 0.0F, 1.0F, 0.0F).next();
	}
}
