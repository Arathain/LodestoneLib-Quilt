package team.lodestar.lodestone.systems.rendering;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.client.texture.SpriteAtlasTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShaderHolder {
	public ExtendedShaderProgram instance;
	public ArrayList<String> uniforms;
	public ArrayList<UniformData> defaultUniformData = new ArrayList<>();
	public final RenderPhase.Shader phase = new RenderPhase.Shader(getInstance());

	public ShaderHolder(String... uniforms) {
		this.uniforms = new ArrayList<>(List.of(uniforms));
	}

	public void setUniformDefaults() {
		RenderSystem.setShaderTexture(1, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
		defaultUniformData.forEach(u -> u.setUniformValue(instance.getUniformOrDefault(u.uniformName)));
	}

	public void setInstance(ExtendedShaderProgram instance) {
		this.instance = instance;
	}

	public Supplier<ShaderProgram> getInstance() {
		return () -> instance;
	}
}
