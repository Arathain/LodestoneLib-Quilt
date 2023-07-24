package team.lodestar.lodestone.mixin;

import com.mojang.blaze3d.shader.ShaderStage;
import com.mojang.datafixers.util.Pair;
import team.lodestar.lodestone.handlers.PostProcessHandler;
import team.lodestar.lodestone.setup.LodestoneShaders;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
final class GameRendererMixin {
	@Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
	private void lodestone$renderWorldLast(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
		Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
		matrix.push();
		matrix.translate(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
		PostProcessHandler.renderLast(matrix);
		matrix.pop();
	}
	@Inject(method = "loadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void lodestone$registerShaders(ResourceFactory factory, CallbackInfo ci, List<ShaderStage> list,  List<Pair<ShaderProgram, Consumer<ShaderProgram>>> list2) throws IOException {
		LodestoneShaders.init(factory);
		list2.addAll(LodestoneShaders.shaderList);
	}
	@Inject(method = "onResized", at = @At(value = "HEAD"))
	public void injectionResizeListener(int width, int height, CallbackInfo ci) {
		PostProcessHandler.resize(width, height);
	}
}
