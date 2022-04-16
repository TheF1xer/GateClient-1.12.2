package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.SendPacketEvent;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {
    public static final NoFall INSTANCE = new NoFall();

    public NoFall() {
        super("NoFall", "nofall", Module.ModuleCategory.MOVEMENT);
    }

    public void onSendPacket(SendPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // If player is falling, make them be "on ground"
        if (event.getPacket() instanceof CPacketPlayer && mc.player.fallDistance > 3) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packet.onGround = true;
        }
    }
}
