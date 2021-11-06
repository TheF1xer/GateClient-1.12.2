package me.thef1xer.gateclient.modules.render;

import me.thef1xer.gateclient.modules.Module;
import net.minecraftforge.client.GuiIngameForge;

public class NoOverlay extends Module {
    public static final NoOverlay INSTANCE = new NoOverlay();

    public NoOverlay() {
        super("No Overlay", "nooverlay", Module.ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        GuiIngameForge.renderObjective = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        GuiIngameForge.renderObjective = true;
    }
}
