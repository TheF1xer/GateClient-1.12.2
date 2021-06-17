package com.thef1xer.gateclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MathUtil {
    public static Vec3d interpolateEntity(Entity entity) {
        double partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

    /*
    yaw in degrees, returns unit vector (x, z)
     */
    public static double[] getDirection(float yaw) {
        return new double[]{-Math.sin(Math.toRadians(yaw)), Math.cos(Math.toRadians(yaw))};
    }
}
