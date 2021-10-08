package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ClickGuiModule extends Module {
    public static final ClickGuiModule INSTANCE = new ClickGuiModule();

    public ClickGuiModule() {
        super("Click GUI", "clickgui", Keyboard.KEY_RSHIFT, ModuleCategory.HUD);
    }

    @Override
    public void onEnabled() {
        Minecraft.getMinecraft().displayGuiScreen(GateClient.getGate().guiManager.CLICK_GUI);
        super.onEnabled();
    }
}