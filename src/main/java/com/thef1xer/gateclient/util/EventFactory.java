package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.events.EventSetOpaqueCube;
import net.minecraftforge.common.MinecraftForge;

public class EventFactory {
    public static boolean setOpaqueCube() {
        EventSetOpaqueCube setOpaqueCube = new EventSetOpaqueCube();
        return MinecraftForge.EVENT_BUS.post(setOpaqueCube);
    }
}
