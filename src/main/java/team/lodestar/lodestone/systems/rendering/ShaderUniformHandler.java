package team.lodestar.lodestone.systems.rendering;

import net.minecraft.client.render.ShaderProgram;

public interface ShaderUniformHandler {
	ShaderUniformHandler LUMITRANSPARENT = instance -> {
		instance.getUniformOrDefault("LumiTransparency").setFloat(1f);
	};
	void updateShaderData(ShaderProgram instance);
}
