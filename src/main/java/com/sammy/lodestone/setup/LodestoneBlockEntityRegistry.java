package com.sammy.lodestone.setup;

import com.sammy.lodestone.systems.block.sign.LodestoneStandingSignBlock;
import com.sammy.lodestone.systems.block.sign.LodestoneWallSignBlock;
import com.sammy.lodestone.systems.blockentity.LodestoneSignBlockEntity;
import com.sammy.lodestone.systems.multiblock.ILodestoneMultiblockComponent;
import com.sammy.lodestone.systems.multiblock.MultiBlockComponentEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.sammy.lodestone.LodestoneLib.MODID;


public class LodestoneBlockEntityRegistry {
	public static final Map<Identifier, BlockEntityType<?>> BLOCK_ENTITY_TYPES  = new LinkedHashMap<>();

	public static final BlockEntityType<MultiBlockComponentEntity> MULTIBLOCK_COMPONENT = register("multiblock_component", BlockEntityType.Builder.create(MultiBlockComponentEntity::new, getBlocks(ILodestoneMultiblockComponent.class)).build(null));
	public static final BlockEntityType<LodestoneSignBlockEntity> SIGN = register("sign", BlockEntityType.Builder.create(LodestoneSignBlockEntity::new, getBlocks(LodestoneStandingSignBlock.class, LodestoneWallSignBlock.class)).build(null));

	public static Block[] getBlocks(Class<?>... blockClasses) {
		Registry<Block> blocks = Registries.BLOCK;
		ArrayList<Block> matchingBlocks = new ArrayList<>();
		for (Block block : blocks) {
			if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
				matchingBlocks.add(block);
			}
		}
		return matchingBlocks.toArray(new Block[0]);
	}

	public static Block[] getBlocksExact(Class<?> clazz) {
		Registry<Block> blocks = Registries.BLOCK;
		ArrayList<Block> matchingBlocks = new ArrayList<>();
		for (Block block : blocks) {
			if (clazz.equals(block.getClass())) {
				matchingBlocks.add(block);
			}
		}
		return matchingBlocks.toArray(new Block[0]);
	}

	static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(new Identifier(MODID, id), type);
		return type;
	}

	public static void init() {
		BLOCK_ENTITY_TYPES.forEach((id, entityType) -> Registry.register(Registries.BLOCK_ENTITY_TYPE, id, entityType));
	}
}
