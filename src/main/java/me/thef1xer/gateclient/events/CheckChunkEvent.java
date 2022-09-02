package me.thef1xer.gateclient.events;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CheckChunkEvent extends Event {
    private final Chunk chunk;
    private final boolean isLoaded;

    public CheckChunkEvent(Chunk chunk, boolean isLoaded) {
        this.chunk = chunk;
        this.isLoaded = isLoaded;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
