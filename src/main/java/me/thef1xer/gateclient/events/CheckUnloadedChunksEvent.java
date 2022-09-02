package me.thef1xer.gateclient.events;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class CheckUnloadedChunksEvent extends Event {
    private final List<Chunk> chunks;

    public CheckUnloadedChunksEvent(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }
}
