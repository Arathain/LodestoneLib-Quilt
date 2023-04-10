package com.sammy.lodestone.systems.postprocess;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.framebuffer.Framebuffer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shader.GlUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.sammy.lodestone.LodestoneLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.JsonEffectGlShader;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import static com.mojang.blaze3d.platform.GlConst.GL_DRAW_FRAMEBUFFER;

public abstract class PostProcessor {
	protected static final MinecraftClient MC = MinecraftClient.getInstance();

	public static final Collection<Pair<String, Consumer<GlUniform>>> COMMON_UNIFORMS = Lists.newArrayList(
			Pair.of("cameraPos", u -> u.setVec3(new Vector3f((float) MC.gameRenderer.getCamera().getPos().x, (float) MC.gameRenderer.getCamera().getPos().y, (float) MC.gameRenderer.getCamera().getPos().z))),
			Pair.of("lookVector", u -> u.setVec3(MC.gameRenderer.getCamera().getHorizontalPlane())),
			Pair.of("upVector", u -> u.setVec3(MC.gameRenderer.getCamera().getVerticalPlane())),
			Pair.of("leftVector", u -> u.setVec3(MC.gameRenderer.getCamera().getDiagonalPlane())),
			Pair.of("invViewMat", u -> {
				Matrix4f invertedViewMatrix = new Matrix4f(PostProcessor.viewModelStack.peek().getModel());
				invertedViewMatrix.invert();
				u.setMat4x4(invertedViewMatrix);
			}),
			Pair.of("invProjMat", u -> {
				Matrix4f invertedProjectionMatrix = new Matrix4f(RenderSystem.getProjectionMatrix());
				invertedProjectionMatrix.invert();
				u.setMat4x4(invertedProjectionMatrix);
			}),
			Pair.of("nearPlaneDistance", u -> u.setFloat(GameRenderer.CAMERA_DEPTH)),
			Pair.of("farPlaneDistance", u -> u.setFloat(MC.gameRenderer.getFarDepth())),
			Pair.of("fov", u -> u.setFloat((float) Math.toRadians(MC.gameRenderer.getFov(MC.gameRenderer.getCamera(), MC.getTickDelta(), true)))),
			Pair.of("aspectRatio", u -> u.setFloat((float) MC.getWindow().getWidth() / (float) MC.getWindow().getHeight()))
	);

	/**
	 * Being updated every frame before calling applyPostProcess() by PostProcessHandler
	 */
	public static MatrixStack viewModelStack;

	private boolean initialized = false;
	protected ShaderEffect shaderEffect;
	protected JsonEffectGlShader[] effects;
	private Framebuffer tempDepthBuffer;
	private Collection<Pair<GlUniform, Consumer<GlUniform>>> defaultUniforms;

	private boolean isActive = true;

	protected double time;

	/**
	 * Example: "octus:foo" points to octus:shaders/post/foo.json
	 */
	public abstract Identifier getShaderEffectId();

	public void init() {
		loadPostChain();

		if (shaderEffect != null) {
			tempDepthBuffer = shaderEffect.getSecondaryTarget("depthMain");

			defaultUniforms = new ArrayList<>();
			for (JsonEffectGlShader e : effects) {
				for (Pair<String, Consumer<GlUniform>> pair : COMMON_UNIFORMS) {
					GlUniform u = e.getUniformByName(pair.getFirst());
					if (u != null) {
						defaultUniforms.add(Pair.of(u, pair.getSecond()));
					}
				}
			}
		}

		initialized = true;
	}

	/**
	 * Load or reload the shader
	 */
	public final void loadPostChain() {
		if (shaderEffect != null) {
			shaderEffect.close();
			shaderEffect = null;
		}

		try {
			Identifier file = getShaderEffectId();
			file = new Identifier(file.getNamespace(), "shaders/post/" + file.getPath() + ".json");
			shaderEffect = new ShaderEffect(
					MC.getTextureManager(),
					MC.getResourceManager(),
					MC.getFramebuffer(),
					file
			);
			shaderEffect.setupDimensions(MC.getWindow().getWidth(), MC.getWindow().getHeight());
			effects = shaderEffect.passes.stream().map(PostProcessShader::getProgram).toArray(JsonEffectGlShader[]::new);
		} catch (IOException | JsonParseException e) {
			LodestoneLib.LOGGER.error("Failed to load post-processing shader: ", e);
		}
	}

	public final void copyDepthBuffer() {
		if (isActive) {
			if (shaderEffect == null || tempDepthBuffer == null) return;

			tempDepthBuffer.copyDepthFrom(MC.getFramebuffer());

			// rebind the main framebuffer so that we don't mess up other things
			GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, MC.getFramebuffer().framebufferId);
		}
	}

	public void resize(int width, int height) {
		if (shaderEffect != null) {
			shaderEffect.setupDimensions(width, height);
			if (tempDepthBuffer != null)
				tempDepthBuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
		}
	}

	private void applyDefaultUniforms() {
		Arrays.stream(effects).forEach(e -> e.getUniformByNameOrDummy("time").setFloat((float) time));

		defaultUniforms.forEach(pair -> pair.getSecond().accept(pair.getFirst()));
	}

	public final void applyPostProcess() {
		if (isActive) {
			if (!initialized)
				init();

			if (shaderEffect != null) {
				time += MC.getLastFrameDuration() / 20.0;

				applyDefaultUniforms();

				beforeProcess(viewModelStack);
				if (!isActive) return;
				shaderEffect.render(MC.getTickDelta());

				GlStateManager._glBindFramebuffer(GL_DRAW_FRAMEBUFFER, MC.getFramebuffer().framebufferId);
				afterProcess();
			}
		}
	}

	/**
	 * Set uniforms and bind textures here
	 */
	public abstract void beforeProcess(MatrixStack viewModelStack);

	/**
	 * Unbind textures
	 */
	public abstract void afterProcess();

	public final void setActive(boolean active) {
		this.isActive = active;

		if (!active)
			time = 0.0;
	}

	public final boolean isActive() {
		return isActive;
	}
}
