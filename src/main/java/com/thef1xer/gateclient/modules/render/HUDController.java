package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import org.lwjgl.input.Keyboard;

public class HUDController extends Module {
    public HUDController() {
        super("HUD", EnumModuleCategory.RENDER, Keyboard.KEY_B);
    }

    @Override
    public void onEnabled() {
        GateClient.hud.renderModules = true;
    }

    @Override
    public void onDisabled() {
        GateClient.hud.renderModules = false;
    }
}
