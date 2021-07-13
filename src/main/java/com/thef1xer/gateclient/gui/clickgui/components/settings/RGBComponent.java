package com.thef1xer.gateclient.gui.clickgui.components.settings;

import com.thef1xer.gateclient.gui.clickgui.ClickComponent;
import com.thef1xer.gateclient.settings.impl.RGBSetting;
import com.thef1xer.gateclient.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class RGBComponent extends ClickComponent{
    //TODO: Try to rework someday (If I find a better method) (mouseClicked method maybe?)

    private final RGBSetting setting;
    private boolean expanded = false;
    private final List<ColorSlider> sliders = new ArrayList<>();

    public RGBComponent(RGBSetting setting, float posX, float posY) {
        super(posX, posY);
        this.setting = setting;
        this.sliders.add(new ColorSlider(this.posX, 0));
        this.sliders.add(new ColorSlider(this.posX, 0));
        this.sliders.add(new ColorSlider(this.posX, 0));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.draw2DRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.border, this.posY + this.border, 0xFFFFFFFF, true);

        if (this.expanded) {
            this.setting.setRed(this.sliders.get(0).renderSlider("Red", setting.getRed(), setting.getRed(), setting.getGreen(), setting.getBlue(), mouseX));
            this.setting.setGreen(this.sliders.get(1).renderSlider("Green", setting.getGreen(), setting.getRed(), setting.getGreen(), setting.getBlue(), mouseX));
            this.setting.setBlue(this.sliders.get(2).renderSlider("Blue", setting.getBlue(), setting.getRed(), setting.getGreen(), setting.getBlue(), mouseX));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isMouseHover(mouseX, mouseY)) {
            this.expanded = !this.expanded;
            this.updateHierarchy();
        }

        if (this.expanded) {
            for (ClickComponent setting : this.sliders) {
                setting.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.expanded) {
            for (ClickComponent setting : this.sliders) {
                setting.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public float updatedByParent(float offsetY) {
        this.posY = offsetY;
        return this.expanded ? this.updateChildren(this.sliders) : offsetY + this.height;
    }

    public static class ColorSlider extends ClickComponent {
        private boolean dragging = false;

        public ColorSlider(float posX, float posY) {
            super(posX, posY);
            this.height = this.height + 4;
        }

        public int renderSlider(String color, int slider, int red, int green, int blue, int mouseX){
            RenderUtil.draw2DRect(this.posX, this.posY, this.posX + width, this.posY + height, 0.15F, 0.15F, 0.15F, 1F);
            this.fontRenderer.drawString(color, this.posX + 2 * this.border, this.posY + this.border, (red << 16) | (green << 8) | (blue), true);
            this.fontRenderer.drawString(((Integer)slider).toString(), this.posX + this.width - fontRenderer.getStringWidth(((Integer)slider).toString()) - 2 * this.border, this.posY + this.border, 0xFFFFFFFF, true);

            RenderUtil.draw2DRect(this.posX + 2 * border, this.posY + height - border - 3, this.posX + width - 2 * border, this.posY + height - border - 1, 0F, 0F, 0F, 1F);
            RenderUtil.draw2DRect(this.posX + 2 * border, this.posY + height - border - 3, this.posX + 2 * border + (width - 4 * border) * slider / 255, this.posY + height - border - 1, 0.85F, 0.43F, 0F, 1F);


            if (dragging) {
                double wMin = posX + 2 * border;
                double wMax = posX + width - 2 * border;

                if (mouseX > wMax) {
                    return 255;
                } else if (mouseX < wMin) {
                    return 0;
                } else {
                    return Math.round(255 * (float) (mouseX - wMin) / (width - 4 * border));
                }
            }

            return slider;
        }

        @Override
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (isMouseHover(mouseX, mouseY)) {
                dragging = true;
            }
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY, int state) {
            if (dragging) {
                dragging = false;
            }
        }
    }
}
