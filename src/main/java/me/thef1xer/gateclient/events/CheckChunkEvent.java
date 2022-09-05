package me.thef1xer.gateclient.events;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CheckChunkEvent extends Event {
    private final Chunk chunk;
    private final boolean isChunkLoaded;

    public CheckChunkEvent(Chunk chunk, boolean isChunkLoaded) {
        this.chunk = chunk;
        this.isChunkLoaded = isChunkLoaded;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public boolean isChunkLoaded() {
        return isChunkLoaded;
    }
}
