package com.thef1xer.gateclient.gui.clickgui.components;

import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.util.RenderUtil;

public class PresetComponent extends ClickComponent {
    private final float fontX = (this.width - fontRenderer.getStringWidth("Presets")) / 2F;

    public PresetComponent(float posX, float posY) {
        super(posX, posY);
        this.border = 5;
        this.height = 19;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        //Header
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.18F, 0.38F, 0.9F, 1F);
        fontRenderer.drawString("Presets", this.posX + this.fontX, this.posY + this.border, 0xFFFFFFFF, true);
    }
}
