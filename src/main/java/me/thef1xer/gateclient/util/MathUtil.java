package me.thef1xer.gateclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class MathUtil {
    public static double[] interpolateEntity(Entity entity) {
        double partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        return new double[] { entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks };
    }

    /*
    yaw in degrees, returns unit vector (x, z)
     */
    public static double[] getDirection(float yaw) {
        return new double[] {-Math.sin(Math.toRadians(yaw)), Math.cos(Math.toRadians(yaw))};
    }
}
