package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.events.EventSendPacket;
import com.thef1xer.gateclient.events.EventSetOpaqueCube;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

public class EventFactory {
    public static boolean setOpaqueCube() {
        EventSetOpaqueCube setOpaqueCube = new EventSetOpaqueCube();
        return MinecraftForge.EVENT_BUS.post(setOpaqueCube);
    }
    public static boolean sendPacket(Packet<?> packet) {
        return MinecraftForge.EVENT_BUS.post(new EventSendPacket(packet));
    }
}
