package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.events.PlayerMoveEvent;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafeWalk extends Module {
    public static final SafeWalk INSTANCE = new SafeWalk();

    public final EnumSetting mode = new EnumSetting("Mode", "mode", Mode.values(), Mode.STOP);

    public SafeWalk() {
        super("Safe Walk", "safewalk", Module.ModuleCategory.MOVEMENT);
        this.addSettings(mode);
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

    //Priority must be LOWEST so that this does not interfere with Speed
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.onGround) {
            if (this.mode.getCurrentValue() == Mode.STOP) {

                //If already falling return (sometimes this happens)
                if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, 0)).isEmpty()) {
                    return;
                }

                //Almost a copy of the Sneak code in Minecraft
                while (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(event.x, -1, 0)).isEmpty()) {
                    if (event.x < 0.05D && event.x >= -0.05D) {
                        event.x = 0;
                    } else if (event.x > 0) {
                        event.x -= 0.05D;
                    } else {
                        event.x += 0.05D;
                    }
                }

                while (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, event.z)).isEmpty()) {
                    if (event.z < 0.05D && event.z >= -0.05D) {
                        event.z = 0;
                    } else if (event.z > 0) {
                        event.z -= 0.05D;
                    } else {
                        event.z += 0.05D;
                    }
                }

                while (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(event.x, -1, event.z)).isEmpty()) {
                    if (event.x < 0.05D && event.x >= -0.05D) {
                        event.x = 0;
                    } else if (event.x > 0) {
                        event.x -= 0.05D;
                    } else {
                        event.x += 0.05D;
                    }

                    if (event.z < 0.05D && event.z >= -0.05D) {
                        event.z = 0;
                    } else if (event.z > 0) {
                        event.z -= 0.05D;
                    } else {
                        event.z += 0.05D;
                    }
                }

            } else {
                if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(event.x, -1, 0)).isEmpty()
                        || mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, event.z)).isEmpty()
                        || mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(event.x, -1, event.z)).isEmpty()) {
                    mc.player.movementInput.sneak = true;
                    event.x *= 0.3D;
                    event.z *= 0.3D;
                }
            }
        }
    }

    public enum Mode {
        STOP("Stop"),
        SNEAK("Sneak");

        private final String name;
        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
