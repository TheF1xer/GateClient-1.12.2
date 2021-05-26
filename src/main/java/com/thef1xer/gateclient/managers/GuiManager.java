package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.gui.clickgui.ClickGui;

public class GuiManager {
    public final ClickGui CLICK_GUI = new ClickGui();

    public void init() {
        this.CLICK_GUI.init();
    }
}
