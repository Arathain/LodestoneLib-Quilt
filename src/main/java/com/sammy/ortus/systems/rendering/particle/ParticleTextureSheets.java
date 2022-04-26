package com.sammy.ortus.systems.rendering.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.setup.OrtusShaders;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public class ParticleTextureSheets {
    public static final ParticleTextureSheet ADDITIVE = new ParticleTextureSheet() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            RenderSystem.setShader(OrtusShaders.ADDITIVE_PARTICLE.getShader());
            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
            RenderHandler.PARTICLE_MATRIX = RenderSystem.getModelViewMatrix();
            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        }

        @Override
        public void draw(Tessellator tesselator) {
            tesselator.draw();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }

        @Override
        public String toString() {
            return "ADDITIVE";
        }
    };
    public static final ParticleTextureSheet TRANSPARENT = new ParticleTextureSheet() {
        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
            RenderHandler.PARTICLE_MATRIX = RenderSystem.getModelViewMatrix();
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
}
