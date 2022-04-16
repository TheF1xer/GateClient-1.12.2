package me.thef1xer.gateclient.modules.movement;

import me.thef1xer.gateclient.gui.clickgui.ClickGui;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;

public class GuiMove extends Module {
    public static final GuiMove INSTANCE = new GuiMove();

    public BooleanSetting sneak = new BooleanSetting("Sneak", "sneak", false);

    public GuiMove() {
        super("GUI Move", "guimove", Module.ModuleCategory.MOVEMENT);
        this.addSettings(sneak);
    }

    public void onInputUpdate() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null && mc.player != null) {
            if (mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiOptions || mc.currentScreen instanceof ClickGui) {
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
