package me.thef1xer.gateclient.util;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public abstract class SearchBlocksInChunksThread extends Thread {
    public final Chunk[] chunksToSearch;

    public SearchBlocksInChunksThread(Chunk[] chunksToSearch) {
        this.chunksToSearch = chunksToSearch;
    }

    public SearchBlocksInChunksThread(ObjectCollection<Chunk> chunksToSearchCollection) {
        Chunk[] chunksToSearch = new Chunk[chunksToSearchCollection.size()];
        chunksToSearchCollection.toArray(chunksToSearch);
        this.chunksToSearch = chunksToSearch;
    }

    public abstract void searchBlockInChunk(Chunk chunk, BlockPos pos);

    @Override
    public void run() {
        // Loop through every block in the Chunks
        for (Chunk chunk : chunksToSearch) {

            for (int x = chunk.getPos().getXStart(); x <= chunk.getPos().getXEnd(); x++) {
                for (int y = 1; y <= 256; y++) {
                    for (int z = chunk.getPos().getZStart(); z <= chunk.getPos().getZEnd(); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        searchBlockInChunk(chunk, pos);
                    }
                }
            }

        }
    }
}
