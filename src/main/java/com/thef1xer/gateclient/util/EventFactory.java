package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.events.*;
import net.minecraft.block.state.IBlockState;
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

    public static boolean renderBlock(IBlockState stateIn) {
        return MinecraftForge.EVENT_BUS.post(new RenderModelEvent(stateIn));
    }

    public static boolean shouldSideBeRendered(IBlockState blockState, boolean shouldBeRendered) {
        ShouldSideBeRenderedEvent event = new ShouldSideBeRenderedEvent(blockState, shouldBeRendered);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getShouldBeRendered();
    }

    public static float getAmbientOcclusionLightValue(IBlockState state) {
        GetAmbientOcclusionLightValueEvent event = new GetAmbientOcclusionLightValueEvent(state);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getLightValue();
    }
}
