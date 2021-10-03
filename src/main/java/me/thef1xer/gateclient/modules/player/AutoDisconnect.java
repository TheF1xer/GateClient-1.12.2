package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoDisconnect extends Module {
    public static final AutoDisconnect INSTANCE = new AutoDisconnect();

    public final BooleanSetting players = new BooleanSetting("Players", "players", false);
    public final FloatSetting health = new FloatSetting("Health", "health", 10, 0, 20, 1);
    public final BooleanSetting ignoreTotems = new BooleanSetting("Ignore Totems", "ignoretotems", true);
    public final BooleanSetting disable = new BooleanSetting("Disable", "disable", true);

    public AutoDisconnect() {
        super("Auto Disconnect", "autodisconnect", Module.ModuleCategory.PLAYER);
        this.addSettings(players, health, ignoreTotems, disable);
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        //Players
        if (players.getValue()) {
            for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
                if (entity instanceof EntityPlayer && entity != Minecraft.getMinecraft().player && entity != Freecam.INSTANCE.camera) {
                    Minecraft.getMinecraft().player.connection.onDisconnect(new TextComponentString("A Player was found"));
                    if (disable.getValue()) {
                        this.setEnabled(false);
                    }
                    return;
                }
            }
        }

        //Totems
        if (!ignoreTotems.getValue() && Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == Item.getItemById(449)) {
            return;
        }

        //Health
        if (Minecraft.getMinecraft().player.getHealth() <= health.getValue()) {
            Minecraft.getMinecraft().player.connection.onDisconnect(new TextComponentString("Health was " + Minecraft.getMinecraft().player.getHealth()));
            if (disable.getValue()) {
                this.setEnabled(false);
            }
        }
    }
}
