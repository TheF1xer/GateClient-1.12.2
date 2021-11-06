package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Flight extends Module {
    public static final Flight INSTANCE = new Flight();

    public final EnumSetting mode = new EnumSetting("Mode", "mode", Mode.values(), Mode.VANILLA);

    public Flight() {
        super("Flight", "flight", ModuleCategory.MOVEMENT);
        addSettings(mode);
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
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.capabilities.isFlying = false;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (mode.getCurrentValue() == Mode.VANILLA) {
            Minecraft.getMinecraft().player.capabilities.isFlying = true;
        }
    }

    public enum Mode {
        VANILLA("Vanilla"),
        PACKET("Packet");

        private final String name;
        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
