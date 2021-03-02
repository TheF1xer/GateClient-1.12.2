package com.thef1xer.gateclient.modules.movement;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
    public static final InventoryMove INSTANCE = new InventoryMove();

    public BooleanSetting sneak = new BooleanSetting("Sneak in Inventory", "sneak", false);

    public InventoryMove() {
        super("Inventory Move", "inventorymove", EnumModuleCategory.MOVEMENT);
        this.addSettings(sneak);
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
    public void onKeyUpdate(InputUpdateEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null && mc.player != null) {
            if (mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiOptions) {
                if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                    ++mc.player.movementInput.moveForward;
                    mc.player.movementInput.forwardKeyDown = true;
                }

                if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                    --mc.player.movementInput.moveForward;
                    mc.player.movementInput.backKeyDown = true;
                }

                if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                    --mc.player.movementInput.moveStrafe;
                    mc.player.movementInput.rightKeyDown = true;
                }

                if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                    ++mc.player.movementInput.moveStrafe;
                    mc.player.movementInput.rightKeyDown = true;
                }

                mc.player.movementInput.jump = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
                mc.player.movementInput.sneak = this.sneak.getValue() && Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());

                if (mc.player.movementInput.sneak) {
                    mc.player.movementInput.moveStrafe = (float)((double)mc.player.movementInput.moveStrafe * 0.3D);
                    mc.player.movementInput.moveForward = (float)((double)mc.player.movementInput.moveForward * 0.3D);
                }
            }
        }
    }
}
