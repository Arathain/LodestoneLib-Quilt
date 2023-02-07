package com.sammy.lodestone.systems.particle.world;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.sammy.lodestone.handlers.RenderHandler;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.setup.LodestoneShaderRegistry;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public interface LodestoneWorldParticleTextureSheet extends ParticleTextureSheet {
	LodestoneWorldParticleTextureSheet ADDITIVE = new LodestoneWorldParticleTextureSheet() {

		@Override
		public RenderLayer getType() {
			return LodestoneRenderLayers.ADDITIVE_PARTICLE;
		}

		@Override
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.setShader(LodestoneShaderRegistry.PARTICLE.getInstance());
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
			RenderHandler.MATRIX4F = RenderSystem.getModelViewMatrix();
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
		}

		@Override
		public void draw(Tessellator tessellator) {
			tessellator.draw();
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	};
	LodestoneWorldParticleTextureSheet TRANSPARENT = new LodestoneWorldParticleTextureSheet() {

		@Override
		public RenderLayer getType() {
			return LodestoneRenderLayers.TRANSPARENT_PARTICLE;
		}

		@Override
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShader(LodestoneShaderRegistry.PARTICLE.getInstance());
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
			RenderHandler.MATRIX4F = RenderSystem.getModelViewMatrix();
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
		}

		@Override
		public void draw(Tessellator tessellator) {
			tessellator.draw();
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	};

	default boolean shouldBuffer() {
		return true;
	}

	RenderLayer getType();
}
