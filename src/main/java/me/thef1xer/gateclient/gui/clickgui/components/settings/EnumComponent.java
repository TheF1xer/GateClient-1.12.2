package me.thef1xer.gateclient.gui.clickgui.components.settings;

import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.util.RenderUtil;

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
        // Background
        RenderUtil.draw2DRect(posX, posY, width, height, 0F, 0F, 0F, 0.6F);
        fontRenderer.drawString(setting.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFFFFFFF, true);

        // Enum List
        if (this.expanded) {
            for (Option option: this.options) {
                option.renderCheck(this.setting.getCurrentValue() == option.getOption());
            }
            RenderUtil.draw2DTriangleRight(posX + width - 2 * padding - 6, posY + padding, 4, height - 2 * padding, 1F, 1F, 1F, 1F);
        } else {
            RenderUtil.draw2DTriangleDown(posX + width - 2 * padding - 8, posY + padding + 2, 8, height - 2 * padding - 4, 1F, 1F, 1F, 1F);
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
            RenderUtil.draw2DRect(posX, posY, width, height, 0F, 0F, 0F, 0.6F);
            if (isCurrent) {
                RenderUtil.draw2DRect(posX + 2 * padding, posY + padding, 8, 8, 0.85F, 0.43F, 0F, 1F);
            }
            RenderUtil.draw2DRectLines(posX + 2 * padding, posY + padding, 8, 8, 0.8F, 0.8F, 0.8F, 0.8F);
            fontRenderer.drawString(option.toString(), this.posX + 3 * this.padding + 9, this.posY + this.padding, 0xFFFFFFFF, true);
        }

        public boolean checkBox(int mouseX, int mouseY) {
            return this.isMouseHover(mouseX, mouseY);
        }

        public Enum<?> getOption() {
            return option;
        }
    }
}
