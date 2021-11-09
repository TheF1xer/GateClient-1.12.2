package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.SendPacketEvent;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall extends Module {
    public static final NoFall INSTANCE = new NoFall();

    public NoFall() {
        super("NoFall", "nofall", Module.ModuleCategory.MOVEMENT);
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
    public void onSendPacket(SendPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getPacket() instanceof CPacketPlayer && !((CPacketPlayer) event.getPacket()).isOnGround() && mc.player.fallDistance > 3) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            event.setPacket(new CPacketPlayer.PositionRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch, true));
        }
    }
}
