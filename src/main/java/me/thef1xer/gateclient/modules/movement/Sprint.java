package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint extends Module {
    public static final Sprint INSTANCE = new Sprint();

    public Sprint() {
        super("Sprint", "sprint", Module.ModuleCategory.MOVEMENT);
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
    public void onLivingEvent(LivingEvent.LivingUpdateEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null && mc.world.isRemote) {
            if (mc.player != null &&
                    mc.player.moveForward > 0F &&
                    !mc.player.collidedHorizontally &&
                    !mc.player.isSprinting() &&
                    !mc.player.isSneaking()) {

                mc.player.setSprinting(true);
            }
        }
    }
}
