package com.sammy.ortus.mixin;

import com.sammy.ortus.handlers.ScreenParticleHandler;
import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.setup.OrtusParticles;
import com.sammy.ortus.setup.OrtusScreenParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.sammy.ortus.OrtusLib.RANDOM;

@Mixin(MinecraftClient.class)
final class MinecraftClientMixin {
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;registerReloader(Lnet/minecraft/resource/ResourceReloader;)V", ordinal = 17))
	private void lodestone$registerParticleFactories(RunArgs runArgs, CallbackInfo ci) {
		OrtusParticles.registerFactories();
		OrtusScreenParticles.registerParticleFactories();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void lodestone$clientTick(CallbackInfo ci) {
		ScreenParticleHandler.clientTick();
		ScreenshakeHandler.clientTick(MinecraftClient.getInstance().gameRenderer.getCamera(), RANDOM);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 4, shift = At.Shift.AFTER))
	private void lodestone$renderTickThingamajig(boolean tick, CallbackInfo ci) {
		ScreenParticleHandler.renderParticles();
	}
}
