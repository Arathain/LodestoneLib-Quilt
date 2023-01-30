package com.sammy.lodestone.mixin;

import com.sammy.lodestone.handlers.WorldEventHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Inject(method = "addPlayer", at = @At("HEAD"))
	private void malum$eventInject(ServerPlayerEntity player, CallbackInfo ci){
		WorldEventHandler.playerJoin(player);
	}
}
