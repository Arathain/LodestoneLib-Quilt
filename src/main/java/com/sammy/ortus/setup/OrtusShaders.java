package com.sammy.ortus.setup;

import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;
import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.rendering.ExtendedShader;
import com.sammy.ortus.systems.rendering.ShaderHolder;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OrtusShaders {
	public static List<Pair<ShaderProgram, Consumer<ShaderProgram>>> shaderList;
	public static ShaderHolder ADDITIVE_TEXTURE = new ShaderHolder();
	public static ShaderHolder ORTUS_PARTICLE = new ShaderHolder();
	public static ShaderHolder ADDITIVE_PARTICLE = new ShaderHolder();

	public static ShaderHolder MASKED_TEXTURE = new ShaderHolder();
	public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder("Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");
	public static ShaderHolder METALLIC_NOISE = new ShaderHolder("Intensity", "Size", "Speed", "Brightness");
	public static ShaderHolder RADIAL_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");
	public static ShaderHolder RADIAL_SCATTER_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");

	public static ShaderHolder VERTEX_DISTORTION = new ShaderHolder();
	//public static ShaderHolder BLOOM = new ShaderHolder();

	public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder("Speed");
	public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder();
	public static ShaderHolder COLOR_GRADIENT_TEXTURE = new ShaderHolder("DarkColor");
	public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder("Speed");

	public static void init(ResourceManager manager) throws IOException {
		shaderList = new ArrayList<>();
		registerShader(ExtendedShader.createShaderInstance(ORTUS_PARTICLE, manager, OrtusLib.id("particle"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(ADDITIVE_TEXTURE, manager, OrtusLib.id("additive_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(ADDITIVE_PARTICLE, manager, OrtusLib.id("additive_particle"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));

		registerShader(ExtendedShader.createShaderInstance(DISTORTED_TEXTURE, manager, OrtusLib.id("noise/distorted_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(METALLIC_NOISE, manager, OrtusLib.id("noise/metallic"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(RADIAL_NOISE, manager, OrtusLib.id("noise/radial_noise"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(RADIAL_SCATTER_NOISE, manager, OrtusLib.id("noise/radial_scatter_noise"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));

		registerShader(ExtendedShader.createShaderInstance(SCROLLING_TEXTURE, manager, OrtusLib.id("vfx/scrolling_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(TRIANGLE_TEXTURE, manager, OrtusLib.id("vfx/triangle_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(SCROLLING_TRIANGLE_TEXTURE, manager, OrtusLib.id("vfx/scrolling_triangle_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
	}
	public static void registerShader(ExtendedShader extendedShaderInstance) {
		registerShader(extendedShaderInstance, (shader) -> ((ExtendedShader) shader).getHolder().setInstance((ExtendedShader) shader));
	}
	public static void registerShader(ShaderProgram shader, Consumer<ShaderProgram> onLoaded)
	{
		shaderList.add(Pair.of(shader, onLoaded));
	}
}
