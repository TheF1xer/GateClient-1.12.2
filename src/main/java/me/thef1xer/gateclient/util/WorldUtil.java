package me.thef1xer.gateclient.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public class WorldUtil {

    public static boolean isBlockPosInChunks(List<Chunk> chunks, BlockPos pos) {

        // Don't use enhanced for please
        for (int i = 0; i < chunks.size(); i++) {
            if (chunks.get(i) == null) continue;
            ChunkPos chunkPos = chunks.get(i).getPos();

            if (chunkPos.getXStart() <= pos.getX() && pos.getX() <= chunkPos.getXEnd() &&
                    chunkPos.getZStart() <= pos.getZ() && pos.getZ() <= chunkPos.getZEnd()) {
                return true;
            }
        }

        return false;
    }
}
