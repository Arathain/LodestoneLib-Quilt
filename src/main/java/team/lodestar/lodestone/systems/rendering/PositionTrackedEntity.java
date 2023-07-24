package team.lodestar.lodestone.systems.rendering;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public interface PositionTrackedEntity {
	public void trackPastPositions();
	public ArrayList<Vec3d> getPastPositions();
}
