package me.thef1xer.gateclient.events;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GetLiquidCollisionBoundingBoxEvent extends Event {
    private AxisAlignedBB collisionBoundingBox;
    private final BlockPos blockPos;

    public GetLiquidCollisionBoundingBoxEvent(BlockPos blockPos) {
        this.collisionBoundingBox = Block.NULL_AABB;
        this.blockPos = blockPos;
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return collisionBoundingBox;
    }

    public void setCollisionBoundingBox(AxisAlignedBB collisionBoundingBox) {
        this.collisionBoundingBox = collisionBoundingBox;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
