package me.thef1xer.gateclient.events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderModelEvent extends Event {
    private final IBlockState state;

    public RenderModelEvent(IBlockState state) {
        this.state = state;
    }

    public IBlockState getState() {
        return state;
    }
}
