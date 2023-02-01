package com.sammy.lodestone.systems.particle.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.sammy.lodestone.setup.LodestoneShaderRegistry;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public interface LodestoneScreenParticleTextureSheet {
	LodestoneScreenParticleTextureSheet ADDITIVE = new LodestoneScreenParticleTextureSheet() {
		@Override
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.setShader(LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance());
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		}

		@Override
		public void draw(Tessellator tessellator) {
			tessellator.draw();
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	};
	LodestoneScreenParticleTextureSheet TRANSPARENT = new LodestoneScreenParticleTextureSheet() {
		@Override
		public void begin(BufferBuilder builder, TextureManager manager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.setShader(LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance());
			RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		}

		@Override
		public void draw(Tessellator tessellator) {
			tessellator.draw();
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	};

	void begin(BufferBuilder pBuilder, TextureManager pTextureManager);

	void draw(Tessellator pTesselator);
}
