package me.thef1xer.gateclient.managers;

import me.thef1xer.gateclient.events.CheckBlockInChunkEvent;
import me.thef1xer.gateclient.events.CheckUnloadedChunksEvent;
import me.thef1xer.gateclient.events.ThreadTickEvent;
import me.thef1xer.gateclient.events.SearchChunksEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
    public final Thread clientThread;

    public List<Chunk> loadedChunksToCheck = new ArrayList<>();
    public List<Chunk> unloadedChunksToCheck = new ArrayList<>();
    public List<Chunk> chunksToCheck = new ArrayList<>();

    private final Minecraft mc = Minecraft.getMinecraft();

    public ThreadManager() {
        clientThread = new Thread(() -> {

            // Run method override
            while (true) {
                // I don't know why, but if I remove this, the Thread won't work anymore
                MinecraftForge.EVENT_BUS.post(new ThreadTickEvent());

                // Do I really need Events to do this? I feel like I'm just using a lot of memory just to make everything more readable
                // But other people with more experience than me have done it this way
                // I think I really hate event systems, they kinda feel unnecessary, can't we do this with methods anyway?

                // Check unloaded Chunks
                if (mc.world != null && !unloadedChunksToCheck.isEmpty()) {

                    MinecraftForge.EVENT_BUS.post(new CheckUnloadedChunksEvent(unloadedChunksToCheck));

                    unloadedChunksToCheck.clear();
                }

                // Check loaded Chunks
                if (!loadedChunksToCheck.isEmpty() && MinecraftForge.EVENT_BUS.post(new SearchChunksEvent())) {
                    for (int i = 0; i < loadedChunksToCheck.size(); i++) {
                        Chunk chunk = loadedChunksToCheck.get(i);

                        if (mc.world == null) continue;
                        if (!mc.world.getChunkProvider().chunkMapping.containsValue(chunk)) continue;

                        for (int x = chunk.getPos().getXStart(); x <= chunk.getPos().getXEnd(); x++) {
                            for (int y = 1; y <= 256; y++) {
                                for (int z = chunk.getPos().getZStart(); z <= chunk.getPos().getZEnd(); z++) {

                                    // Post new event with BlockPos and Chunk, I hate events so much
                                    MinecraftForge.EVENT_BUS.post(new CheckBlockInChunkEvent(new BlockPos(x, y, z), chunk));
                                }
                            }
                        }

                    }

                    loadedChunksToCheck.clear();
                }

            }
        });

        clientThread.setDaemon(true);
    }

    public void init() {
        clientThread.start();
    }
}
