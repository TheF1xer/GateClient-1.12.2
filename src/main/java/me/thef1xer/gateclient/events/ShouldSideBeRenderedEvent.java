package me.thef1xer.gateclient.events;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ShouldSideBeRenderedEvent extends Event {
    private final IBlockState blockState;
    private boolean shouldBeRendered;

    public ShouldSideBeRenderedEvent(IBlockState blockState, boolean shouldBeRendered) {
        this.blockState = blockState;
        this.shouldBeRendered = shouldBeRendered;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public boolean getShouldBeRendered() {
        return shouldBeRendered;
    }

    public void setShouldBeRendered(boolean shouldBeRendered) {
        this.shouldBeRendered = shouldBeRendered;
    }
}
