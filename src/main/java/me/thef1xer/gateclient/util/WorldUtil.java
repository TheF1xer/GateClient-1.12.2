package me.thef1xer.gateclient.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class WorldUtil {

    public static boolean isBlockPosInChunk(Chunk chunk, BlockPos pos) {
        if (chunk == null) return false;

        ChunkPos chunkPos = chunk.getPos();

        return chunkPos.getXStart() <= pos.getX() && pos.getX() <= chunkPos.getXEnd() &&
                chunkPos.getZStart() <= pos.getZ() && pos.getZ() <= chunkPos.getZEnd();
    }
}
