package me.thef1xer.gateclient.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CheckBlockInChunkEvent extends Event {
    private final BlockPos blockPos;
    private final Chunk chunk;

    public CheckBlockInChunkEvent(BlockPos blockPos, Chunk chunk) {
        this.blockPos = blockPos;
        this.chunk = chunk;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
