package com.sammy.ortus.systems.rendering;


import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.Shader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShaderHolder {
	public ExtendedShader instance;
	public ArrayList<String> uniforms;
	public ArrayList<UniformData> defaultUniformData = new ArrayList<>();
	public final RenderPhase.Shader phase = new RenderPhase.Shader(getShader());

	public ShaderHolder(String... uniforms) {
		this.uniforms = new ArrayList<>(List.of(uniforms));
	}

	public void setUniformDefaults() {
		defaultUniformData.forEach(u -> u.setUniformValue(instance.getUniformOrDefault(u.uniformName)));
	}

	public void setShader(ExtendedShader instance) {
		this.instance = instance;
	}

	public Supplier<Shader> getShader() {
		return () -> instance;
	}
}
