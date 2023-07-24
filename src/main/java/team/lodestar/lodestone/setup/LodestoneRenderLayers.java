package team.lodestar.lodestone.setup;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.Phases;
import team.lodestar.lodestone.systems.rendering.ShaderUniformHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.quiltmc.loader.api.QuiltLoader;
import team.lodestar.lodestone.LodestoneLib;

import java.util.HashMap;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.VertexFormats.*;


public class LodestoneRenderLayers extends RenderPhase {
	public LodestoneRenderLayers(String string, Runnable runnable, Runnable runnable2) {
		super(string, runnable, runnable2);
	}

	/**
	 * Stores many copies of render layers, a copy is a new instance of a render layer with the same properties.
	 * It's useful when we want to apply different uniform changes with each separate use of our render type.
	 * Use the {@link #copy(RenderLayer)} method to create copies.
	 */
	public static final HashMap<Pair<Integer, RenderLayer>, RenderLayer> COPIES = new HashMap<>();
	public static final Function<RenderLayerData, RenderLayer> GENERIC = (data) -> createGenericRenderLayer(data.name, data.format, data.mode, data.shader, data.transparency, data.texture);


	public static final RenderLayer ADDITIVE_PARTICLE = createGenericRenderLayer(LodestoneLib.MODID, "additive_particle", POSITION_TEXTURE_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.PARTICLE.phase, Phases.ADDITIVE_TRANSPARENCY, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
	public static final RenderLayer ADDITIVE_BLOCK = createGenericRenderLayer(LodestoneLib.MODID, "block", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.LODESTONE_TEXTURE.phase, Phases.ADDITIVE_TRANSPARENCY, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
	public static final RenderLayer ADDITIVE_SOLID = createGenericRenderLayer(LodestoneLib.MODID, "additive_solid", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, RenderPhase.POSITION_COLOR_LIGHTMAP_SHADER, Phases.ADDITIVE_TRANSPARENCY);

	public static final RenderLayer TRANSPARENT_PARTICLE = createGenericRenderLayer(LodestoneLib.MODID, "transparent_particle", POSITION_TEXTURE_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.PARTICLE.phase, Phases.NORMAL_TRANSPARENCY, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
	public static final RenderLayer TRANSPARENT_BLOCK = createGenericRenderLayer(LodestoneLib.MODID, "transparent_block", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, RenderPhase.POSITION_COLOR_LIGHTMAP_SHADER, Phases.NORMAL_TRANSPARENCY, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
	public static final RenderLayer TRANSPARENT_SOLID = createGenericRenderLayer(LodestoneLib.MODID, "transparent_solid", POSITION_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, RenderPhase.POSITION_COLOR_LIGHTMAP_SHADER, Phases.NORMAL_TRANSPARENCY);

	public static final RenderLayer LUMITRANSPARENT_PARTICLE = copyWithUniformChanges("lumitransparent_particle", TRANSPARENT_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);
	public static final RenderLayer LUMITRANSPARENT_BLOCK = copyWithUniformChanges("lumitransparent_block", TRANSPARENT_BLOCK, ShaderUniformHandler.LUMITRANSPARENT);
	public static final RenderLayer LUMITRANSPARENT_SOLID = copyWithUniformChanges("lumitransparent_solid", TRANSPARENT_SOLID, ShaderUniformHandler.LUMITRANSPARENT);



	/**
	 * Render Functions. You can create Render Layers by statically applying these to your texture. Alternatively, use {@link #GENERIC} if none of the presets suit your needs.
	 */


	public static final RenderLayerProvider TEXTURE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "texture", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.LODESTONE_TEXTURE.phase, Phases.NO_TRANSPARENCY, texture));

	public static final RenderLayerProvider TRANSPARENT_TEXTURE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "transparent_texture", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.LODESTONE_TEXTURE.phase, Phases.NORMAL_TRANSPARENCY, texture));
	public static final RenderLayerProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "transparent_texture_triangle", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.TRIANGLE_TEXTURE.phase, Phases.NORMAL_TRANSPARENCY, texture));

	public static final RenderLayerProvider ADDITIVE_TEXTURE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "additive_texture", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.LODESTONE_TEXTURE.phase, Phases.ADDITIVE_TRANSPARENCY, texture));
	public static final RenderLayerProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "additive_texture_triangle", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.TRIANGLE_TEXTURE.phase, Phases.ADDITIVE_TRANSPARENCY, texture));

	public static final RenderLayerProvider RADIAL_NOISE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "radial_noise", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.RADIAL_NOISE.phase, Phases.ADDITIVE_TRANSPARENCY, texture));
	public static final RenderLayerProvider RADIAL_SCATTER_NOISE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "radial_scatter_noise", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.RADIAL_SCATTER_NOISE.phase, Phases.ADDITIVE_TRANSPARENCY, texture));
	public static final RenderLayerProvider SCROLLING_TEXTURE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "scrolling_texture", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.SCROLLING_TEXTURE.phase, Phases.ADDITIVE_TRANSPARENCY, texture));
	public static final RenderLayerProvider SCROLLING_TEXTURE_TRIANGLE = new RenderLayerProvider((texture) -> createGenericRenderLayer(texture.getNamespace(), "scrolling_texture_triangle", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, LodestoneShaders.SCROLLING_TRIANGLE_TEXTURE.phase, Phases.ADDITIVE_TRANSPARENCY, texture));

	public static RenderLayer createGenericRenderLayer(String modId, String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency, Identifier texture) {
		return createGenericRenderLayer(modId + ":" + name, format, mode, shader, transparency, new Texture(texture, false, false));
	}
	public static RenderLayer createGenericRenderLayer(String modId, String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency, TextureBase texture) {
		return createGenericRenderLayer(modId + ":" + name, format, mode, shader, transparency, texture);
	}
	public static RenderLayer createGenericRenderLayer(String modId, String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency) {
		return createGenericRenderLayer(modId + ":" + name, format, mode, shader, transparency, RenderPhase.NO_TEXTURE);
	}

	public static RenderLayer createGenericRenderLayer(String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency, TextureBase texture) {
		return createGenericRenderLayer(name, format, mode, RenderLayer.MultiPhaseParameters.builder()
				.shader(shader)
				.transparency(transparency)
				.texture(texture)
				.lightmap(ENABLE_LIGHTMAP)
				.cull(ENABLE_CULLING));
	}

	/**
	 * Creates a custom render type and creates a buffer builder for it.
	 */
	public static RenderLayer createGenericRenderLayer(String name, VertexFormat format, VertexFormat.DrawMode mode, RenderLayer.MultiPhaseParameters.Builder builder) {
		RenderLayer type = RenderLayer.of(name, format, mode, QuiltLoader.isModLoaded("sodium") ? 262144 : 256, false, false, builder.build(true));
		RenderHandler.addRenderLayer(type);
		return type;
	}
	public static RenderLayer copyWithUniformChanges(RenderLayer type, ShaderUniformHandler handler) {
		return queueUniformChanges(copy(type), handler);
	}

	public static RenderLayer copyWithUniformChanges(String newName, RenderLayer type, ShaderUniformHandler handler) {
		return queueUniformChanges(copy(newName, type), handler);
	}

	/**
	 * Queues shader uniform changes for a render type. When we end batches in {@link RenderHandler}}, we do so one render type at a time.
	 * Prior to ending a batch, we run {@link ShaderUniformHandler#updateShaderData(net.minecraft.client.render.ShaderProgram)} if one is present for a given render type.
	 */
	public static RenderLayer queueUniformChanges(RenderLayer type, ShaderUniformHandler handler) {
		RenderHandler.UNIFORM_HANDLERS.put(type, handler);
		return type;
	}

	/**
	 * Creates a copy of a render type. These are stored in the {@link #COPIES} hashmap, with the key being a pair of original render type and copy index.
	 */
	public static RenderLayer copy(RenderLayer type) {
		return GENERIC.apply(new RenderLayerData((RenderLayer.MultiPhase) type));
	}
	public static RenderLayer copy(String newName, RenderLayer type) {
		return GENERIC.apply(new RenderLayerData(newName, (RenderLayer.MultiPhase) type));
	}

	/**
	 * Stores all relevant data from a RenderLayer.
	 */
	public static class RenderLayerData {
		public final String name;
		public final VertexFormat format;
		public final VertexFormat.DrawMode mode;
		public final Shader shader;
		public Transparency transparency = Phases.ADDITIVE_TRANSPARENCY;
		public final TextureBase texture;

		public RenderLayerData(String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, TextureBase texture) {
			this.name = name;
			this.format = format;
			this.mode = mode;
			this.shader = shader;
			this.texture = texture;
		}

		public RenderLayerData(String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency, TextureBase texture) {
			this(name, format, mode, shader, texture);
			this.transparency = transparency;
		}

		public RenderLayerData(RenderLayer.MultiPhase type) {
			this(type.name, type.getVertexFormat(), type.getDrawMode(), type.phases.shader, type.phases.transparency, type.phases.texture);
		}
		public RenderLayerData(String name, RenderLayer.MultiPhase type) {
			this(name, type.getVertexFormat(), type.getDrawMode(), type.phases.shader, type.phases.transparency, type.phases.texture);
		}
	}

	public static class RenderLayerProvider {
		private final Function<Identifier, RenderLayer> function;
		private final Function<Identifier, RenderLayer> memorizedFunction;

		public RenderLayerProvider(Function<Identifier, RenderLayer> function) {
			this.function = function;
			this.memorizedFunction = Util.memoize(function);
		}

		public RenderLayer apply(Identifier texture) {
			return function.apply(texture);
		}

		public RenderLayer applyAndCache(Identifier texture) {
			return this.memorizedFunction.apply(texture);
		}
	}
}
