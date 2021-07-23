package com.thef1xer.gateclient.gui.clickgui.components.settings;

import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.settings.impl.EnumSetting;
import com.thef1xer.gateclient.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class EnumComponent extends ClickComponent {
    private final EnumSetting setting;
    private final List<Option> options = new ArrayList<>();

    public EnumComponent(EnumSetting setting, float posX, float posY) {
        super(posX, posY);
        this.setting = setting;
        for (Enum<?> option : setting.getValues()) {
            options.add(new Option(option, posX, 0));
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.border, this.posY + this.border, 0xFFFFFFFF, true);

        if (this.expanded) {
            for (Option option: this.options) {
                option.renderCheck(this.setting.getCurrentValue() == option.getOption());
            }
        }
    }

    @Override
    public float updatedByParent(float offsetY) {
        this.posY = offsetY;
        return this.expanded ? this.updateChildren() : offsetY + this.height;
    }

    @Override
    public float updateChildren() {
        float offsetY = this.posY + this.height;
        for (ClickComponent component : options) {
            offsetY = component.updatedByParent(offsetY);
        }
        return offsetY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            this.expanded = !this.expanded;
            this.updateHierarchy();
        }

        for (Option option : this.options) {
            if (option.checkBox(mouseX, mouseY)) {
                this.setting.setCurrentValue(option.getOption());
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Option option : this.options) {
            option.mouseReleased(mouseX, mouseY, state);
        }
    }

    public static class Option extends ClickComponent {
        private final Enum<?> option;

        public Option(Enum<?> option, float posX, float posY) {
            super(posX, posY);
            this.option = option;
        }

        public void renderCheck(boolean isCurrent) {
            RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.15F, 0.15F, 0.15F, 1F);
            if (isCurrent) {
                RenderUtil.draw2DRect(this.posX + 2 * this.border, this.posY + this.border, this.posX + 2 * this.border + 9, this.posY + this.border + 9, 0.85F, 0.43F, 0F, 1F);
            }
            RenderUtil.draw2DRectLines(this.posX + 2 * this.border, this.posY + this.border, this.posX + 2 * this.border + 9, this.posY + this.border + 9, 0.8F, 0.8F, 0.8F, 0.8F);
            fontRenderer.drawString(option.toString(), this.posX + 3 * this.border + 9, this.posY + this.border, 0xFFFFFFFF, true);
        }

        public boolean checkBox(int mouseX, int mouseY) {
            return this.isMouseHover(mouseX, mouseY);
        }

        public Enum<?> getOption() {
            return option;
        }
    }
}
