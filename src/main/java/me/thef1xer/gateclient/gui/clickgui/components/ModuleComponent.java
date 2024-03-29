package me.thef1xer.gateclient.gui.clickgui.components;

import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.gui.clickgui.components.settings.*;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.RenderUtil;

public class ModuleComponent extends ClickComponent {
    private final Module module;

    private boolean expanded = false;

    public ModuleComponent(Module module, float posX, float posY) {
        super(posX, posY);
        this.module = module;

        this.children.add(new KeybindComponent(module, posX, 0));

        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                this.children.add(new BooleanComponent((BooleanSetting) setting, posX, 0));
            } else if (setting instanceof EnumSetting) {
                this.children.add(new EnumComponent((EnumSetting) setting, posX, 0));
            } else if (setting instanceof FloatSetting) {
                this.children.add(new SliderComponent((FloatSetting) setting, posX, 0));
            } else if (setting instanceof RGBSetting) {
                this.children.add(new RGBComponent((RGBSetting) setting, posX, 0));
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        // Drawing with different colors in different occasions
        if (this.module.isEnabled()) {
            RenderUtil.draw2DRect(posX, posY, width, height, 0.85F, 0.43F, 0F, 1F);
            fontRenderer.drawString(module.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFFFFFFF, true);

        } else if (this.isMouseHover(mouseX, mouseY)) {
            RenderUtil.draw2DRect(posX, posY, width, height, 0.85F, 0.43F, 0F, 0.7F);
            fontRenderer.drawString(module.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFFFFFFF, true);

        } else {
            RenderUtil.draw2DRect(posX, posY, width, height, 0F, 0F, 0F, 0.7F);
            fontRenderer.drawString(module.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFAAAAAA, true);
        }

        if (this.expanded) {
            for (ClickComponent setting : this.children) {
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
            for (ClickComponent setting : this.children) {
                setting.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.expanded) {
            for (ClickComponent setting : this.children) {
                setting.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (ClickComponent component : this.children) {
            component.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public float updatedByParent(float offsetY) {
        this.posY = offsetY;
        return this.expanded ? this.updateChildren() : offsetY + this.height;
    }
}
