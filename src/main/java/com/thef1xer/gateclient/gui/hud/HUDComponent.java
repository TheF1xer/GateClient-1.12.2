package com.thef1xer.gateclient.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class HUDComponent {
    protected final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    protected abstract boolean isEnabled();

    protected abstract void componentAction();

    public final void renderComponent() {
        if (this.isEnabled()) {
            this.componentAction();
        }
    }
}
