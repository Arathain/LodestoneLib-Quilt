package com.sammy.lodestone.systems.rendering.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.sammy.lodestone.handlers.RenderHandler;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.setup.LodestoneShaders;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public interface LodestoneWorldParticleRenderLayer extends ParticleTextureSheet{
	LodestoneWorldParticleRenderLayer ADDITIVE = new LodestoneWorldParticleRenderLayer() {
		@Override
		public RenderLayer getRenderType() {
			return LodestoneRenderLayers.ADDITIVE_PARTICLE;
		}

		@Override
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.setShader(LodestoneShaders.LODESTONE_PARTICLE.getInstance());
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
		}

		@Override
		public void draw(Tessellator tesselator) {
			tesselator.draw();
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	};
	LodestoneWorldParticleRenderLayer TRANSPARENT = new LodestoneWorldParticleRenderLayer() {
		@Override
		public RenderLayer getRenderType() {
			return LodestoneRenderLayers.TRANSPARENT_PARTICLE;
		}

		@Override
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShader(LodestoneShaders.LODESTONE_PARTICLE.getInstance());
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
		}

		@Override
		public void draw(Tessellator tesselator) {
			tesselator.draw();
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	};

	default boolean shouldBuffer() {
		return true;
	}
	RenderLayer getRenderType();
}
