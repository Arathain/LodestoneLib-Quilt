package com.sammy.lodestone.setup.worldevent;


import com.sammy.lodestone.systems.worldevent.WorldEventInstance;
import com.sammy.lodestone.systems.worldevent.WorldEventRenderer;
import com.sammy.lodestone.systems.worldevent.WorldEventType;

import java.util.HashMap;

public class LodestoneWorldEventRendererRegistry {
	public static HashMap<WorldEventType, WorldEventRenderer<WorldEventInstance>> RENDERERS = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static void registerRenderer(WorldEventType type, WorldEventRenderer<? extends WorldEventInstance> renderer) {
		RENDERERS.put(type, (WorldEventRenderer<WorldEventInstance>) renderer);
	}
}
