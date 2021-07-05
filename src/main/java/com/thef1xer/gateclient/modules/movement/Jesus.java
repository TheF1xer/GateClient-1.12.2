package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.events.GetLiquidCollisionBoundingBoxEvent;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Jesus extends Module {
    public static final Jesus INSTANCE = new Jesus();

    public final FloatSetting offset = new FloatSetting("Offset", "offset", 0.2F, 0F, 1F);

    public Jesus() {
        super("Jesus", "jesus", Module.ModuleCategory.MOVEMENT);
        this.addSettings(offset);
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
        event.setCollisionBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D - (double) offset.getValue(), 1.0D));
    }
}
