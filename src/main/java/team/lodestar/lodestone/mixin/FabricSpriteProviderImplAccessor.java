package team.lodestar.lodestone.mixin;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpriteProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FabricSpriteProviderImpl.class)
public interface FabricSpriteProviderImplAccessor {
	@Invoker("<init>")
	static FabricSpriteProviderImpl FabricSpriteProviderImpl(ParticleManager manager, SpriteProvider delegate) {
		throw new AssertionError();
	}
}
