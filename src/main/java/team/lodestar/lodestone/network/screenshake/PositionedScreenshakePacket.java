package team.lodestar.lodestone.network.screenshake;

import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class PositionedScreenshakePacket extends ScreenshakePacket {

	public static final Identifier ID = new Identifier(LodestoneLib.MODID, "positionedscreenshake");

	public final Vec3d position;
	public final float falloffDistance;
	public final float minDot;
	public final float maxDistance;
	public final Easing falloffEasing;

	public PositionedScreenshakePacket(int duration, Vec3d position, float falloffDistance, float minDot, float maxDistance, Easing falloffEasing) {
		super(duration);
		this.position = position;
		this.falloffDistance = falloffDistance;
		this.minDot = minDot;
		this.maxDistance = maxDistance;
		this.falloffEasing = falloffEasing;
	}

	public static PositionedScreenshakePacket fromBuf(PacketByteBuf buf) {
		return ((PositionedScreenshakePacket) new PositionedScreenshakePacket(
				buf.readInt(),
				new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()),
				buf.readFloat(),
				buf.readFloat(),
				buf.readFloat(),
				Easing.valueOf(buf.readString())
		).setIntensity(
				buf.readFloat(),
				buf.readFloat(),
				buf.readFloat()
		).setEasing(
				Easing.valueOf(buf.readString()),
				Easing.valueOf(buf.readString())
		));
	}

	public PositionedScreenshakePacket(int duration, Vec3d position, float falloffDistance, float maxDistance) {
		this(duration, position, falloffDistance, 0f, maxDistance, Easing.LINEAR);
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(duration);
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeFloat(falloffDistance);
		buf.writeFloat(minDot);
		buf.writeFloat(maxDistance);
		buf.writeString(falloffEasing.name);
		buf.writeFloat(intensity1);
		buf.writeFloat(intensity2);
		buf.writeFloat(intensity3);
		buf.writeString(intensityCurveStartEasing.name);
		buf.writeString(intensityCurveEndEasing.name);
	}

	@Override
	public void apply(ClientPlayPacketListener listener) {
		ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(duration, position, falloffDistance, minDot, maxDistance, falloffEasing).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
	}
}
