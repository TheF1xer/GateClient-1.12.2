package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;

public class AutoDisconnect extends Module {
    public static final AutoDisconnect INSTANCE = new AutoDisconnect();

    public final BooleanSetting players = new BooleanSetting("Players", "players", false);
    public final FloatSetting health = new FloatSetting("Health", "health", 10, 0, 20, 1);
    public final BooleanSetting ignoreTotems = new BooleanSetting("Ignore Totems", "ignoretotems", true);
    public final BooleanSetting disable = new BooleanSetting("Disable", "disable", true);

    public AutoDisconnect() {
        super("Auto Disconnect", "autodisconnect", Module.ModuleCategory.PLAYER);
        addSettings(players, health, ignoreTotems, disable);
    }

    public void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null) {
            return;
        }

        // Players
        if (players.getValue()) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityPlayer && entity != mc.player && entity != Freecam.INSTANCE.camera) {
                    mc.player.connection.onDisconnect(new TextComponentString("A Player was found"));
                    if (disable.getValue()) {
                        this.setEnabled(false);
                    }
                    return;
                }
            }
        }

        // Totems
        if (!ignoreTotems.getValue() && mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }

        // Health
        if (mc.player.getHealth() <= health.getValue()) {
            mc.player.connection.onDisconnect(new TextComponentString("Health was " + mc.player.getHealth()));
            if (disable.getValue()) {
                this.setEnabled(false);
            }
        }
    }
}
