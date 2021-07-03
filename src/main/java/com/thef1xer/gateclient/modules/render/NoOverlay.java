package com.thef1xer.gateclient.modules.render;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import net.minecraftforge.client.GuiIngameForge;

public class NoOverlay extends Module {
    public static final NoOverlay INSTANCE = new NoOverlay();

    public NoOverlay() {
        super("No Overlay", "nooverlay", EnumModuleCategory.RENDER);
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        GuiIngameForge.renderObjective = false;
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        GuiIngameForge.renderObjective = true;
    }
}
