package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;

public class NoOverlay extends Module {
    public static final NoOverlay INSTANCE = new NoOverlay();

    public NoOverlay() {
        super("No Overlay", "nooverlay", EnumModuleCategory.RENDER);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        GateClient.gate.hud.noOverlay = true;
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        GateClient.gate.hud.noOverlay = false;
    }
}
