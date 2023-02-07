package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.screenparticle.ScreenParticleHandler;
import com.sammy.lodestone.handlers.ScreenshakeHandler;
import com.sammy.lodestone.handlers.WorldEventHandler;
import com.sammy.lodestone.setup.LodestoneParticleRegistry;
import com.sammy.lodestone.setup.LodestoneScreenParticleRegistry;
import com.sammy.lodestone.systems.client.ClientTickCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.sammy.lodestone.LodestoneLib.RANDOM;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
	@Shadow
	public abstract boolean isPaused();

	@Shadow
	private volatile boolean paused;

	@Shadow
	private float pausedTickDelta;

	@Shadow
	@Final
	private RenderTickCounter renderTickCounter;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManager;registerReloader(Lnet/minecraft/resource/ResourceReloader;)V", ordinal = 17))
	private void lodestone$registerParticleFactories(RunArgs runArgs, CallbackInfo ci) {
		LodestoneParticleRegistry.registerFactories();
		LodestoneScreenParticleRegistry.registerParticleFactories();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void lodestone$clientTick(CallbackInfo ci) {
		ClientTickCounter.clientTick();
		if(!isPaused()){
			ScreenParticleHandler.tickParticles();
			ScreenshakeHandler.clientTick(MinecraftClient.getInstance().gameRenderer.getCamera(), RANDOM);
			WorldEventHandler.tick(MinecraftClient.getInstance().world);
		}

	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 4, shift = At.Shift.AFTER))
	private void lodestone$renderTickThingamajig(boolean tick, CallbackInfo ci) {
		ScreenParticleHandler.renderParticles();
		float realPartialTick = paused ? pausedTickDelta : renderTickCounter.tickDelta;
		ClientTickCounter.renderTick(realPartialTick);
	}
}
