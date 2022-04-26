package com.sammy.ortus.systems.rendering;


import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;

public class TrailPoint {

	public final float xp;
	public final float xn;
	public final float yp;
	public final float yn;
	public final float z;

	public TrailPoint(float xp, float xn, float yp, float yn, float z) {
		this.xp = xp;
		this.xn = xn;
		this.yp = yp;
		this.yn = yn;
		this.z = z;
	}

	public void renderStart(VertexConsumer builder, int packedLight, float r, float g, float b, float a, float u0, float v0, float u1, float v1) {
		builder.vertex(xp, yp, z).color(r, g, b, a).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).next();
		builder.vertex(xn, yn, z).color(r, g, b, a).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).next();
	}

	public void renderEnd(VertexConsumer builder, int packedLight, float r, float g, float b, float a, float u0, float v0, float u1, float v1) {
		builder.vertex(xn, yn, z).color(r, g, b, a).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).next();
		builder.vertex(xp, yp, z).color(r, g, b, a).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).next();
	}
}
