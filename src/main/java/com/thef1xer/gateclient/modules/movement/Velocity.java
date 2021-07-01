package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.events.PacketEvent;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
    public static final Velocity INSTANCE = new Velocity();

    public final FloatSetting horizontal = new FloatSetting("Horizontal", "horizontal", 0, 0, 1);
    public final FloatSetting vertical = new FloatSetting("Vertical", "vertical", 0, 0, 1);

    public Velocity() {
        super("Velocity", "velocity", EnumModuleCategory.MOVEMENT);
        this.addSettings(horizontal, vertical);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        //I could use access transformers but I don't really want to set them up

        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        if (event.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity velPacket = (SPacketEntityVelocity) event.getPacket();

            if (velPacket.getEntityID() == Minecraft.getMinecraft().player.getEntityId()) {
                if (horizontal.getValue() != 0) {
                    Minecraft.getMinecraft().player.motionX = (double)velPacket.getMotionX() * horizontal.getValue() / 8000.0D;
                    Minecraft.getMinecraft().player.motionZ = (double)velPacket.getMotionZ() * horizontal.getValue() / 8000.0D;
                }

                if (vertical.getValue() != 0) {
                    Minecraft.getMinecraft().player.motionY = (double)velPacket.getMotionY() * vertical.getValue() / 8000.0D;
                }

                event.setCanceled(true);
            }
        }

        if (event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion expPacket = (SPacketExplosion) event.getPacket();

            Explosion explosion = new Explosion(Minecraft.getMinecraft().world, null, expPacket.getX(), expPacket.getY(), expPacket.getZ(), expPacket.getStrength(), expPacket.getAffectedBlockPositions());
            explosion.doExplosionB(true);

            if (horizontal.getValue() != 0) {
                Minecraft.getMinecraft().player.motionX += expPacket.getMotionX() * horizontal.getValue();
                Minecraft.getMinecraft().player.motionZ += expPacket.getMotionZ() * horizontal.getValue();
            }

            if (vertical.getValue() != 0) {
                Minecraft.getMinecraft().player.motionY += expPacket.getMotionY() * vertical.getValue();
            }

            event.setCanceled(true);
        }
    }
}
