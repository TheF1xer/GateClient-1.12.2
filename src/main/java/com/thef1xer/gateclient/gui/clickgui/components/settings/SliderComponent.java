package com.thef1xer.gateclient.gui.clickgui.components.settings;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.settings.impl.FloatSetting;
import com.thef1xer.gateclient.util.RenderUtil;

public class SliderComponent extends ClickComponent {
    private final FloatSetting setting;
    private boolean dragging = false;

    public SliderComponent(FloatSetting setting, float posX, float posY) {
        super(posX, posY);
        this.setting = setting;
        this.height = this.height + 4;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.border, this.posY + this.border, 0xFFFFFFFF, true);

        String s = ((Float) (Math.round(setting.getValue() * 10F) / 10F)).toString();
        fontRenderer.drawString(s, this.posX + this.width - this.border - fontRenderer.getStringWidth(s), this.posY + this.border, 0xFFFFFFFF, true);
        RenderUtil.draw2DRect(this.posX + border, this.posY + height - border - 3, this.posX + width - border, this.posY + height - border - 1, 0.1F, 0.1F, 0.1F, 1F);

        float f = setting.getValue() < setting.getMax() ? setting.getValue() / setting.getMax() : 1F;
        RenderUtil.draw2DRect(this.posX + border, this.posY + height - border - 3, this.posX + border + (width - 2 * border) * f, this.posY + height - border - 1, 0.85F, 0.43F, 0F, 1F);

        if (this.dragging) {
            double wMin = this.posX + this.border;
            double wMax = this.posX + width - this.border;

            if (mouseX > wMax) {
                setting.setValue(setting.getMax());
            } else if (mouseX < wMin) {
                setting.setValue(setting.getMin());
            } else {
                float f1 = (mouseX - this.posX - this.border) / (this.width - 2 * this.border) * setting.getMax();
                setting.setValue(Math.round(f1 * 10F) / 10F);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.dragging) {
            this.dragging = false;
            GateClient.gate.presetManager.saveActivePreset();
        }
    }
}
