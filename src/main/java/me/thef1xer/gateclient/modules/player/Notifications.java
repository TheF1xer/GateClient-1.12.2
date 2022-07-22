package me.thef1xer.gateclient.modules.player;

import me.thef1xer.gateclient.modules.Module;

public class Notifications extends Module {
    public static final Notifications INSTANCE = new Notifications();

    public Notifications() {
        super("Notifications", "notifications", ModuleCategory.PLAYER);
        setEnabled(true);
    }

    /*
    Code for this module is found inside the methods that should create a notification when called.
    This module doesn't interact with the ModuleEventHandler right now
     */
}
