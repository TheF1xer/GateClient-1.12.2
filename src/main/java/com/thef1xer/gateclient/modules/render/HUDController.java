package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;

public class HUDController extends Module {
    public HUDController() {
        super("HUD", "hud", EnumModuleCategory.RENDER);
    }

    /*
    The rest happens on HUD.java
     */

    @Override
    public void onEnabled() {
        super.onEnabled();
        GateClient.gateClient.hud.renderModules = true;
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        GateClient.gateClient.hud.renderModules = false;
    }
}
