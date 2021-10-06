package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.modules.Module;

public class Watermark extends Module {
    public static final Watermark INSTANCE = new Watermark();

    public Watermark() {
        super("Watermark", "watermark", ModuleCategory.HUD);
        setEnabled(true);
    }
}
