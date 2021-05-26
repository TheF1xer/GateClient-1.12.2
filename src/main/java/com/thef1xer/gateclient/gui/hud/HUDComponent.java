package com.thef1xer.gateclient.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public abstract class HUDComponent {
    protected final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    protected abstract boolean isEnabled();

    protected abstract void componentAction(ScaledResolution sr);

    public final void renderComponent(ScaledResolution sr) {
        if (this.isEnabled()) {
            this.componentAction(sr);
        }
    }
}
