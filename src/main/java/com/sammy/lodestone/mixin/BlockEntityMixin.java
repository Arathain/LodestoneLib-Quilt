package com.sammy.lodestone.mixin;

import com.sammy.lodestone.forge.BlockEntityExtensions;
import com.sammy.lodestone.forge.INBTSerializableCompound;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements BlockEntityExtensions, INBTSerializableCompound {
	@Unique
	private NbtCompound malum$extraData = null;

	@Shadow
	public abstract void readNbt(NbtCompound tag);

	@Shadow
	public abstract NbtCompound toIdentifiedLocatedNbt();

	@Inject(at = @At("RETURN"), method = "writeIndentifyingLocatingData")
	private void port_lib$saveMetadata(NbtCompound nbt, CallbackInfo ci) {
		if (malum$extraData != null && !malum$extraData.isEmpty()) {
			nbt.put("ForgeData", malum$extraData);
		}
	}

	@Inject(at = @At("RETURN"), method = "readNbt")
	private void port_lib$load(NbtCompound tag, CallbackInfo ci) {
		if (tag.contains("ForgeData")) {
			malum$extraData = tag.getCompound("ForgeData");
		} else if (tag.contains("create_ExtraEntityData")) {
			malum$extraData = tag.getCompound("create_ExtraEntityData");
		}
	}

	@Inject(method = "markRemoved", at = @At("TAIL"))
	public void port_lib$invalidate(CallbackInfo ci) {
		invalidateCaps();
	}

	@Override
	public NbtCompound serializeNBT() {
		return this.toIdentifiedLocatedNbt();
	}

	@Override
	public void deserializeNBT(NbtCompound nbt) {
		deserializeNBT(null, nbt);
	}

	@Override
	public NbtCompound getExtraCustomData() {
		if (malum$extraData == null) {
			malum$extraData = new NbtCompound();
		}
		return malum$extraData;
	}

	public void deserializeNBT(BlockState state, NbtCompound nbt) {
		this.readNbt(nbt);
	}
}
