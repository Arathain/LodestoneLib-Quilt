package com.sammy.lodestone.systems.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class LodestoneBoatItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
	private final RegistryObject<EntityType<LodestoneBoatEntity>> boat;

	public LodestoneBoatItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult raytraceresult = getPlayerPOVHitResult(level, playerIn, ClipContext.Fluid.ANY);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			Vec3 vector3d = playerIn.getViewVector(1.0F);
			List<Entity> list = level.getEntities(playerIn, playerIn.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vector3d1 = playerIn.getEyePosition(1.0F);

				for (Entity entity : list) {
					AABB AABB = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (AABB.contains(vector3d1)) {
						return InteractionResultHolder.pass(itemstack);
					}
				}
			}

			if (raytraceresult.getType() == HitResult.Type.BLOCK) {
				LodestoneBoatEntity boatEntity = boat.get().create(level);
				boatEntity.setPos(raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
				boatEntity.setYRot(playerIn.getYRot());
				if (!level.noCollision(boatEntity, boatEntity.getBoundingBox().inflate(-0.1D))) {
					return InteractionResultHolder.fail(itemstack);
				} else {
					if (!level.isClientSide) {
						level.addFreshEntity(boatEntity);
						if (!playerIn.getAbilities().instabuild) {
							itemstack.shrink(1);
						}
					}

					playerIn.awardStat(Stats.ITEM_USED.get(this));
					return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
				}
			} else {
				return InteractionResultHolder.pass(itemstack);
			}
		}
	}
}
