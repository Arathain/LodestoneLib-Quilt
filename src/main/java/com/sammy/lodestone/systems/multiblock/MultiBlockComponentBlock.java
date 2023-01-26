package com.sammy.lodestone.systems.multiblock;

import com.sammy.lodestone.setup.LodestoneBlockEntityRegistry;
import com.sammy.lodestone.systems.block.LodestoneEntityBlock;
import net.minecraft.block.AbstractBlock;

public class MultiBlockComponentBlock extends LodestoneEntityBlock<MultiBlockComponentEntity> implements ILodestoneMultiblockComponent {
	public MultiBlockComponentBlock(AbstractBlock.Settings settings) {
		super(settings);
		setBlockEntity(LodestoneBlockEntityRegistry.MULTIBLOCK_COMPONENT);
	}
}
