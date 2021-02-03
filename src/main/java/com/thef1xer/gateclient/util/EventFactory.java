package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.events.SendPacketEvent;
import com.thef1xer.gateclient.events.SetOpaqueCubeEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

public class EventFactory {
    public static boolean setOpaqueCube() {
        SetOpaqueCubeEvent setOpaqueCube = new SetOpaqueCubeEvent();
        return MinecraftForge.EVENT_BUS.post(setOpaqueCube);
    }
    public static boolean sendPacket(Packet<?> packet) {
        return MinecraftForge.EVENT_BUS.post(new SendPacketEvent(packet));
    }
}
