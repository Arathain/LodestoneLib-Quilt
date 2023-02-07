package com.sammy.lodestone.network.screenshake;

import com.sammy.lodestone.LodestoneLib;
import com.sammy.lodestone.handlers.ScreenshakeHandler;
import com.sammy.lodestone.systems.easing.Easing;
import com.sammy.lodestone.systems.screenshake.ScreenshakeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Identifier;

public class ScreenshakePacket implements Packet<ClientPlayPacketListener> {
	public static final Identifier ID = new Identifier(LodestoneLib.MODID, "screenshake");

	public final int duration;
	public float intensity1, intensity2, intensity3;
	public Easing intensityCurveStartEasing = Easing.LINEAR, intensityCurveEndEasing = Easing.LINEAR;

	public ScreenshakePacket(int duration) {
		this.duration = duration;
	}
	public ScreenshakePacket(PacketByteBuf buf) {
		duration = buf.readInt();
		intensity1 = buf.readFloat();
		intensity2 = buf.readFloat();
		intensity3 = buf.readFloat();
		setEasing(Easing.valueOf(buf.readString()), Easing.valueOf(buf.readString()));
	}

	public ScreenshakePacket setIntensity(float intensity) {
		return setIntensity(intensity, intensity);
	}

	public ScreenshakePacket setIntensity(float intensity1, float intensity2) {
		return setIntensity(intensity1, intensity2, intensity2);
	}

	public ScreenshakePacket setIntensity(float intensity1, float intensity2, float intensity3) {
		this.intensity1 = intensity1;
		this.intensity2 = intensity2;
		this.intensity3 = intensity3;
		return this;
	}

	public ScreenshakePacket setEasing(Easing easing) {
		this.intensityCurveStartEasing = easing;
		this.intensityCurveEndEasing = easing;
		return this;
	}

	public ScreenshakePacket setEasing(Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
		this.intensityCurveStartEasing = intensityCurveStartEasing;
		this.intensityCurveEndEasing = intensityCurveEndEasing;
		return this;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(duration);
		buf.writeFloat(intensity1);
		buf.writeFloat(intensity2);
		buf.writeFloat(intensity3);
		buf.writeString(intensityCurveStartEasing.name);
		buf.writeString(intensityCurveEndEasing.name);
	}

	@Override
	public void apply(ClientPlayPacketListener listener) {
		ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(duration).setIntensity(intensity1, intensity2, intensity3).setEasing(intensityCurveStartEasing, intensityCurveEndEasing));
	}
}
