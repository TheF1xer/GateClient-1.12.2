package com.thef1xer.gateclient.gui.clickgui.components.settings;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;
import com.thef1xer.gateclient.util.RenderUtil;

public class BooleanComponent extends ClickComponent {
    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, float posX, float posY) {
        super(posX, posY);
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.border, this.posY + this.border, 0xFFFFFFFF, true);
        if (this.setting.getValue()) {
            RenderUtil.draw2DRect(this.posX + this.width - this.border - 9, this.posY + this.border, this.posX + this.width - this.border, this.posY + this.border + 9, 0.85F, 0.43F, 0F, 1F);
        }
        RenderUtil.draw2DRectLines(this.posX + this.width - this.border - 9, this.posY + this.border, this.posX + this.width - this.border, this.posY + this.border + 9, 0.8F, 0.8F, 0.8F, 0.8F);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            this.setting.toggle();
        }
    }
}
