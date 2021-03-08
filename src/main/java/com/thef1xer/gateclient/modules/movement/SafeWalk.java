package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.events.PlayerMoveEvent;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.EnumSetting;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class SafeWalk extends Module {
    public static final SafeWalk INSTANCE = new SafeWalk();

    public final EnumSetting<Mode> mode = new EnumSetting<>("Mode", "mode", Mode.values(), Mode.STOP);

    public SafeWalk() {
        super("Safe Walk", "safewalk", EnumModuleCategory.MOVEMENT);
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

    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.onGround) {
            List<AxisAlignedBB> list = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(event.x, -1, event.z));
            if (list.isEmpty()) {
                if (this.mode.getCurrentValue() == Mode.STOP) {
                    event.x = 0;
                } else {
                    mc.player.movementInput.sneak = true;
                }
            }

            if (list.isEmpty()) {
                if (this.mode.getCurrentValue() == Mode.STOP) {
                    event.z = 0;
                } else {
                    mc.player.movementInput.sneak = true;
                }
            }
        }
    }

    private boolean isAir(IBlockState state) {
        return state.getMaterial() == Material.AIR || state.getMaterial() == Material.VINE;
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
