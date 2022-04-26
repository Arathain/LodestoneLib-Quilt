package com.sammy.ortus.systems.rendering;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderPhase;

public class Phases extends RenderPhase {
	public Phases(String string, Runnable runnable, Runnable runnable2) {
		super(string, runnable, runnable2);
	}
	public static final Transparency ADDITIVE_TRANSPARENCY = new Transparency("additive_transparency", () -> {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(true);
	});

	public static final Transparency NORMAL_TRANSPARENCY = new Transparency("normal_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});
}
