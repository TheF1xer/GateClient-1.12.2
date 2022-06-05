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
    public void onEnable() {
        super.onEnable();
        Minecraft.getMinecraft().displayGuiScreen(GateClient.getGate().GUI_MANAGER.CLICK_GUI);
    }
}