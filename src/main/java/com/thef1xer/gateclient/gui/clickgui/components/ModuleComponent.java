package com.thef1xer.gateclient.gui.clickgui.components;

import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.gui.clickgui.components.settings.BooleanComponent;
import com.thef1xer.gateclient.gui.clickgui.components.settings.EnumComponent;
import com.thef1xer.gateclient.gui.clickgui.components.settings.RGBComponent;
import com.thef1xer.gateclient.gui.clickgui.components.settings.SliderComponent;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.Setting;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;
import com.thef1xer.gateclient.settings.impl.EnumSetting;
import com.thef1xer.gateclient.settings.impl.RGBSetting;
import com.thef1xer.gateclient.settings.impl.FloatSetting;
import com.thef1xer.gateclient.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class ModuleComponent extends ClickComponent {
    private final Module module;
    private final List<ClickComponent> settings = new ArrayList<>();

    private boolean expanded = false;

    public ModuleComponent(Module module, float posX, float posY) {
        super(posX, posY);
        this.module = module;

        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                this.settings.add(new BooleanComponent((BooleanSetting) setting, posX, 0));
            } else if (setting instanceof EnumSetting) {
                this.settings.add(new EnumComponent((EnumSetting) setting, posX, 0));
            } else if (setting instanceof FloatSetting) {
                this.settings.add(new SliderComponent((FloatSetting) setting, posX, 0));
            } else if (setting instanceof RGBSetting) {
                this.settings.add(new RGBComponent((RGBSetting) setting, posX, 0));
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        float r, g, b;
        int textColor = 0xFFFFFFFF;
        if (this.module.isEnabled()) {
            r = 0.85F;
            g = 0.43F;
            b = 0F;
        } else if (this.isMouseHover(mouseX, mouseY)) {
            r = 0.42F;
            g = 0.21F;
            b = 0F;
        } else {
            r = 0.1F;
            g = 0.1F;
            b = 0.1F;
            textColor = 0xFFAAAAAA;
        }
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, r, g, b, 1F);
        fontRenderer.drawString(module.getName(), this.posX + this.border, this.posY + this.border, textColor, true);

        if (this.expanded) {
            for (ClickComponent setting : this.settings) {
                setting.drawComponent(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.module.toggle();
            } else if (mouseButton == 1) {
                this.expanded = !this.expanded;
                this.updateHierarchy();
            }
        }

        if (this.expanded) {
            for (ClickComponent setting : this.settings) {
                setting.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.expanded) {
            for (ClickComponent setting : this.settings) {
                setting.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public float updatedByParent(float offsetY) {
        this.posY = offsetY;
        return this.expanded ? this.updateChildren(this.settings) : offsetY + this.height;
    }
}
