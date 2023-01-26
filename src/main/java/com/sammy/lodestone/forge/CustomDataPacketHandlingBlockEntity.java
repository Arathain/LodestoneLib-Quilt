package com.sammy.lodestone.forge;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

public interface CustomDataPacketHandlingBlockEntity {
	void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet);
}
