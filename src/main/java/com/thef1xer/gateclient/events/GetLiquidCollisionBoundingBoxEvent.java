package com.thef1xer.gateclient.events;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GetLiquidCollisionBoundingBoxEvent extends Event {
    private AxisAlignedBB collisionBoundingBox;

    public GetLiquidCollisionBoundingBoxEvent() {
        this.collisionBoundingBox = Block.NULL_AABB;
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        return collisionBoundingBox;
    }

    public void setCollisionBoundingBox(AxisAlignedBB collisionBoundingBox) {
        this.collisionBoundingBox = collisionBoundingBox;
    }
}
