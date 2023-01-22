package com.sammy.lodestone.forge;

public interface ChunkUnloadListeningBlockEntity {
	default void onChunkUnloaded() {
		if (this instanceof BlockEntityExtensions ex) {
			ex.invalidateCaps();
		}
	}
}
