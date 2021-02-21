package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.BooleanSetting;

public class HUDModule extends Module {
    public static HUDModule INSTANCE;

    private final BooleanSetting renderModuleList = new BooleanSetting("Render Module List", "modulelist", true);
    private final BooleanSetting renderCoords = new BooleanSetting("Render your Coordinates", "coords", true);

    public HUDModule() {
        super("HUD", "hud", EnumModuleCategory.RENDER);
        this.addSettings(renderModuleList, renderCoords);

        HUDModule.INSTANCE = this;
    }

    /*
    The rest happens on HUD.java
     */

    @Override
    public void onEnabled() {
        super.onEnabled();
        GateClient.gate.hud.setRenderModules(renderModuleList.getValue());
        GateClient.gate.hud.setRenderCoords(renderModuleList.getValue());
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        GateClient.gate.hud.setRenderModules(false);
        GateClient.gate.hud.setRenderCoords(false);
    }
}
