package com.sammy.lodestone.mixin;

import com.sammy.lodestone.setup.LodestoneAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void lodestone$createLivingAttributesL(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		LodestoneAttributeRegistry.ATTRIBUTES.forEach((id, entityAttribute) -> info.getReturnValue().add(entityAttribute));
	}
}
