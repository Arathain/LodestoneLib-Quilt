package com.sammy.lodestone.setup;

import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;
import com.sammy.lodestone.LodestoneLib;
import com.sammy.lodestone.systems.rendering.ExtendedShader;
import com.sammy.lodestone.systems.rendering.ShaderHolder;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LodestoneShaders {
	public static List<Pair<ShaderProgram, Consumer<ShaderProgram>>> shaderList;
	public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder();
	public static ShaderHolder LODESTONE_PARTICLE = new ShaderHolder();

	public static ShaderHolder MASKED_TEXTURE = new ShaderHolder();
	public static ShaderHolder SILHOUETTE = new ShaderHolder();

	public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder("Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");
	public static ShaderHolder METALLIC_NOISE = new ShaderHolder("Intensity", "Size", "Speed", "Brightness");
	public static ShaderHolder RADIAL_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");
	public static ShaderHolder RADIAL_SCATTER_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");

	public static ShaderHolder VERTEX_DISTORTION = new ShaderHolder();

	public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder("Speed");
	public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder();
	public static ShaderHolder COLOR_GRADIENT_TEXTURE = new ShaderHolder("DarkColor");
	public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder("Speed");


	public static void init(ResourceFactory manager) throws IOException {
		shaderList = new ArrayList<>();
		registerShader(ExtendedShader.createShaderInstance(LODESTONE_TEXTURE, manager, LodestoneLib.id("texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(LODESTONE_PARTICLE, manager, LodestoneLib.id("particle"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));

		registerShader(ExtendedShader.createShaderInstance(MASKED_TEXTURE, manager, LodestoneLib.id("masked_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(SILHOUETTE, manager, LodestoneLib.id("silhouette"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));

		registerShader(ExtendedShader.createShaderInstance(DISTORTED_TEXTURE, manager, LodestoneLib.id("noise/distorted_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(METALLIC_NOISE, manager, LodestoneLib.id("noise/metallic"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(RADIAL_NOISE, manager, LodestoneLib.id("noise/radial_noise"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(RADIAL_SCATTER_NOISE, manager, LodestoneLib.id("noise/radial_scatter_noise"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));

		registerShader(ExtendedShader.createShaderInstance(VERTEX_DISTORTION, manager, LodestoneLib.id("vertex_distortion"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));

		registerShader(ExtendedShader.createShaderInstance(SCROLLING_TEXTURE, manager, LodestoneLib.id("vfx/scrolling_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(TRIANGLE_TEXTURE, manager, LodestoneLib.id("vfx/triangle_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(COLOR_GRADIENT_TEXTURE, manager, LodestoneLib.id("vfx/color_gradient_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
		registerShader(ExtendedShader.createShaderInstance(SCROLLING_TRIANGLE_TEXTURE, manager, LodestoneLib.id("vfx/scrolling_triangle_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));


	}
	public static void registerShader(ExtendedShader extendedShaderInstance) {
		registerShader(extendedShaderInstance, (shader) -> ((ExtendedShader) shader).getHolder().setInstance((ExtendedShader) shader));
	}
	public static void registerShader(ShaderProgram shader, Consumer<ShaderProgram> onLoaded)
	{
		shaderList.add(Pair.of(shader, onLoaded));
	}
}
