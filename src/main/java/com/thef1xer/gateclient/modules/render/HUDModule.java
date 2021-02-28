package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;

public class HUDModule extends Module {
    public static final HUDModule INSTANCE = new HUDModule();

    public final BooleanSetting renderModuleList = new BooleanSetting("Render Module List", "modulelist", true);
    public final BooleanSetting renderCoords = new BooleanSetting("Render your Coordinates", "coords", true);

    public HUDModule() {
        super("HUD", "hud", EnumModuleCategory.RENDER);
        this.addSettings(renderModuleList, renderCoords);
    }

    /*
    The rest happens on HUD.java
     */

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
