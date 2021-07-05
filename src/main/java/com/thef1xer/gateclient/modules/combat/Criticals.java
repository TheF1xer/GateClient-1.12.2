package com.thef1xer.gateclient.modules.combat;

import com.thef1xer.gateclient.events.PacketEvent;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals extends Module {
    public static final Criticals INSTANCE = new Criticals();

    public Criticals() {
        super("Criticals", "criticals", Module.ModuleCategory.COMBAT);
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
    public void onSendPacket(PacketEvent event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
                Minecraft mc = Minecraft.getMinecraft();
                if (packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase && mc.player.onGround) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1D, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                }
            }
        }
    }
}
