package com.sammy.ortus.mixin;

import com.sammy.ortus.handlers.FireEffectHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateSwimming()V"))
	private void lodestoneFireEffectTicking(CallbackInfo ci) {
		FireEffectHandler.entityUpdate(((Entity) (Object) this));
	}

	@Inject(method = "setOnFireFor", at = @At(value = "RETURN"))
	private void lodestoneFireEffectOverride(int pSeconds, CallbackInfo ci) {
		FireEffectHandler.onVanillaFireTimeUpdate((Entity) (Object) this);
	}

//	@Unique
//	private SoundType type;
//
//	@ModifyVariable(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SoundType;getStepSound()Lnet/minecraft/sounds/SoundEvent;"))
//	private SoundType ortusGetStepSound(SoundType type) {
//		return this.type = type;
//	}
//
//	@Inject(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
//	private void ortusCallExtendedStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
//		if (type instanceof ExtendedSoundType extendedSoundType) {
//			Entity entity = ((Entity)(Object)this);
//			extendedSoundType.onPlayStepSound(entity.level, pos, state, entity.getSoundSource());
//		}
//	}
}
