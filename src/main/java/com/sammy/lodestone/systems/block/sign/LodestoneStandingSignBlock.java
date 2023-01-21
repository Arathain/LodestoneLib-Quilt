package com.sammy.lodestone.systems.block.sign;

import com.sammy.lodestone.systems.blockentity.LodestoneSignBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.SignType;
import net.minecraft.util.math.BlockPos;

public class LodestoneStandingSignBlock extends SignBlock implements BlockEntityProvider {
	public LodestoneStandingSignBlock(Settings settings, SignType signType) {
		super(settings, signType);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LodestoneSignBlockEntity(pos, state);
	}
}
