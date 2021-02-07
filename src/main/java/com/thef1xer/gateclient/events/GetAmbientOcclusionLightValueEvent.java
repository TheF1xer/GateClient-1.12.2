package com.thef1xer.gateclient.events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GetAmbientOcclusionLightValueEvent extends Event {
    private float lightValue;

    public GetAmbientOcclusionLightValueEvent(IBlockState state) {
        this.lightValue = state.getBlock().getAmbientOcclusionLightValue(state);
    }

    public float getLightValue() {
        return lightValue;
    }

    public void setLightValue(float lightValue) {
        this.lightValue = lightValue;
    }
}
