package me.thef1xer.gateclient.gui.clickgui.components.settings;

import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.RenderUtil;

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
        //Background and Name
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFFFFFFF, true);

        //Slider
        String s = ((Float) (Math.round(setting.getValue() * 10F) / 10F)).toString();
        fontRenderer.drawString(s, this.posX + this.width - this.padding - fontRenderer.getStringWidth(s), this.posY + this.padding, 0xFFFFFFFF, true);
        RenderUtil.draw2DRect(this.posX + padding, this.posY + height - padding - 3, this.posX + width - padding, this.posY + height - padding - 1, 0.1F, 0.1F, 0.1F, 1F);
        RenderUtil.draw2DRect(this.posX + padding, this.posY + height - padding - 3, this.posX + padding + (width - 2 * padding) * setting.getValue() / setting.getMax(), this.posY + height - padding - 1, 0.85F, 0.43F, 0F, 1F);

        if (this.dragging) {
            double wMin = this.posX + this.padding;
            double wMax = this.posX + width - this.padding;

            if (mouseX > wMax) {
                setting.setValue(setting.getMax());
            } else if (mouseX < wMin) {
                setting.setValue(setting.getMin());
            } else {
                float f1 = (mouseX - this.posX - this.padding) / (this.width - 2 * this.padding) * (setting.getMax() - setting.getMin()) + setting.getMin();
                setting.setValueWithStep(f1);
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
        }
    }
}
