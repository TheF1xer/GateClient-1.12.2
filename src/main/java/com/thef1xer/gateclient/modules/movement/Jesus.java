package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.events.GetLiquidCollisionBoundingBoxEvent;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Jesus extends Module {
    //TODO: Make bounding box lower

    public static Jesus INSTANCE;

    public Jesus() {
        super("Jesus", "jesus", EnumModuleCategory.MOVEMENT);

        Jesus.INSTANCE = this;
    }

    @Override
    public void onEnabled() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onLiquidCollision(GetLiquidCollisionBoundingBoxEvent event) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) {
            return;
        }

        if (Minecraft.getMinecraft().player.isSneaking() || Minecraft.getMinecraft().player.fallDistance > 3F) {
            return;
        }

        if (Minecraft.getMinecraft().player.isInWater() || Minecraft.getMinecraft().player.isInLava()) {
            Minecraft.getMinecraft().player.motionY = 0.30000001192092896D;
            return;
        }
        event.setCollisionBoundingBox(Block.FULL_BLOCK_AABB);
    }
}
