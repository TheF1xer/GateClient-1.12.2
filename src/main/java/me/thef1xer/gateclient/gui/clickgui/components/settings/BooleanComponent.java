package me.thef1xer.gateclient.gui.clickgui.components.settings;

import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.util.RenderUtil;

public class BooleanComponent extends ClickComponent {
    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, float posX, float posY) {
        super(posX, posY);
        this.setting = setting;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(posX, posY, width, height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFFFFFFF, true);
        if (this.setting.getValue()) {
            RenderUtil.draw2DRect(posX + width - padding - 8, posY + padding, 8, 8, 0.85F, 0.43F, 0F, 1F);
        }
        RenderUtil.draw2DRectLines(posX + width - padding - 8, posY + padding, 8, 8, 0.8F, 0.8F, 0.8F, 0.8F);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            this.setting.toggle();
        }
    }
}
