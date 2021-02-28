package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.events.SendPacketEvent;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall extends Module {
    public static final NoFall INSTANCE = new NoFall();

    public NoFall() {
        super("NoFall", "nofall", EnumModuleCategory.MOVEMENT);
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
    public void onPacket(SendPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getPacket() instanceof CPacketPlayer && !((CPacketPlayer) event.getPacket()).isOnGround() && mc.player.fallDistance > 3) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            event.setPacket(new CPacketPlayer.PositionRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch, true));
        }
    }
}
