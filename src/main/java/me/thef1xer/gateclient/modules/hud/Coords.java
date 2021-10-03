package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.modules.Module;

public class Coords extends Module {
    public static final Coords INSTANCE = new Coords();

    public Coords() {
        super("Coords", "coords", Module.ModuleCategory.HUD);
    }
}
