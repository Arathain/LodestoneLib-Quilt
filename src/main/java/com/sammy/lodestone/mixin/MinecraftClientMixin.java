package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.screenparticle.ScreenParticleHandler;
import com.sammy.lodestone.handlers.ScreenshakeHandler;
import com.sammy.lodestone.handlers.WorldEventHandler;
import com.sammy.lodestone.setup.LodestoneParticleRegistry;
import com.sammy.lodestone.setup.LodestoneScreenParticleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.sammy.lodestone.LodestoneLib.RANDOM;

@Mixin(MinecraftClient.class)
final class MinecraftClientMixin {
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;registerReloader(Lnet/minecraft/resource/ResourceReloader;)V", ordinal = 17))
	private void lodestone$registerParticleFactories(RunArgs runArgs, CallbackInfo ci) {
		LodestoneParticleRegistry.registerFactories();
		LodestoneScreenParticleRegistry.registerParticleFactories();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void lodestone$clientTick(CallbackInfo ci) {
		ScreenParticleHandler.tickParticles();
		ScreenshakeHandler.clientTick(MinecraftClient.getInstance().gameRenderer.getCamera(), RANDOM);
		WorldEventHandler.tick(MinecraftClient.getInstance().world);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 4, shift = At.Shift.AFTER))
	private void lodestone$renderTickThingamajig(boolean tick, CallbackInfo ci) {
		ScreenParticleHandler.renderParticles();
	}
}
