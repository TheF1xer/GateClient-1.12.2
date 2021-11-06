package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.PacketEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
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
        super("Velocity", "velocity", Module.ModuleCategory.MOVEMENT);
        this.addSettings(horizontal, vertical);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        //I could use access transformers but I don't really want to set them up

        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        if (event.getPacket() instanceof SPacketEntityVelocity) {

            //This is the same that the SPacketEntityVelocity does but the motion is multiplied by the settings
            SPacketEntityVelocity velPacket = (SPacketEntityVelocity) event.getPacket();

            if (velPacket.getEntityID() == Minecraft.getMinecraft().player.getEntityId()) {

                //We don't want to calculate this if the settings's value is 0
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

            //The same that the SPacketExplosion already does but multiplied by the settings
            SPacketExplosion expPacket = (SPacketExplosion) event.getPacket();

            Explosion explosion = new Explosion(Minecraft.getMinecraft().world, null, expPacket.getX(), expPacket.getY(), expPacket.getZ(), expPacket.getStrength(), expPacket.getAffectedBlockPositions());
            explosion.doExplosionB(true);

            //We don't want to calculate this if the settings's value is 0
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
