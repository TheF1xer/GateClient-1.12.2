package me.thef1xer.gateclient.events;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CheckBlockInChunkEvent extends Event {
    private final BlockPos blockPos;

    public CheckBlockInChunkEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
