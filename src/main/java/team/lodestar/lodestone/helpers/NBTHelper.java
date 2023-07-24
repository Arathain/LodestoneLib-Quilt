package team.lodestar.lodestone.helpers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.ArrayList;
import java.util.List;

public class NBTHelper {

	public static NbtCompound filterTag(NbtCompound orig, TagFilter filter) {
		if (filter.filters.isEmpty()) {
			return orig;
		}
		NbtCompound copy = orig.copy();
		removeTags(copy, filter);
		return copy;
	}

	public static NbtCompound removeTags(NbtCompound tag, TagFilter filter) {
		NbtCompound newTag = new NbtCompound();
		for (String i : filter.filters) {
			if (tag.contains(i)) {
				if (filter.isWhitelist) {
					newTag.put(i, newTag);
				} else {
					tag.remove(i);
				}
			} else {
				for (String key : tag.getKeys()) {
					NbtElement value = tag.get(key);
					if (value instanceof NbtCompound ctag) {
						removeTags(ctag, filter);
					}
				}
			}
		}
		if (filter.isWhitelist) {
			tag = newTag;
		}
		return tag;
	}

	public static TagFilter create(String... filters) {
		return new TagFilter(filters);
	}

	public static class TagFilter {
		public final ArrayList<String> filters = new ArrayList<>();
		public boolean isWhitelist;

		public TagFilter(String... filters) {
			this.filters.addAll(List.of(filters));
		}

		public TagFilter setWhitelist() {
			this.isWhitelist = true;
			return this;
		}
	}
}
