package me.thef1xer.gateclient.gui.clickgui.components.settings;

import me.thef1xer.gateclient.gui.clickgui.ClickComponent;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class RGBComponent extends ClickComponent{
    // TODO: Try to rework someday, this code is a mess (ColorSlider class is unnecessary)

    private final RGBSetting setting;
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
        RenderUtil.draw2DRect(posX, posY, width, height, 0.15F, 0.15F, 0.15F, 1F);
        fontRenderer.drawString(setting.getName(), this.posX + this.padding, this.posY + this.padding, 0xFFFFFFFF, true);

        if (this.expanded) {
            RenderUtil.draw2DTriangleRight(posX + width - 2 * padding - 6, posY + padding, 4, height - 2 * padding, 1F, 1F, 1F, 1F);
            this.setting.setRed(this.sliders.get(0).renderSlider("Red", setting.getRed(), setting.getRed(), setting.getGreen(), setting.getBlue(), mouseX));
            this.setting.setGreen(this.sliders.get(1).renderSlider("Green", setting.getGreen(), setting.getRed(), setting.getGreen(), setting.getBlue(), mouseX));
            this.setting.setBlue(this.sliders.get(2).renderSlider("Blue", setting.getBlue(), setting.getRed(), setting.getGreen(), setting.getBlue(), mouseX));
        } else {
            RenderUtil.draw2DTriangleDown(posX + width - 2 * padding - 8, posY + padding + 2, 8, height - 2 * padding - 4, 1F, 1F, 1F, 1F);
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
        return this.expanded ? this.updateChildren() : offsetY + this.height;
    }

    @Override
    public float updateChildren() {
        float offsetY = this.posY + this.height;
        for (ClickComponent component : this.sliders) {
            offsetY = component.updatedByParent(offsetY);
        }
        return offsetY;
    }

    public static class ColorSlider extends ClickComponent {
        private boolean dragging = false;

        public ColorSlider(float posX, float posY) {
            super(posX, posY);
            this.height = this.height + 4;
        }

        public int renderSlider(String color, int slider, int red, int green, int blue, int mouseX){
            //Background
            RenderUtil.draw2DRect(posX, posY, width, height, 0.15F, 0.15F, 0.15F, 1F);

            //Text
            fontRenderer.drawString(color, this.posX + 2 * this.padding, this.posY + this.padding, (red << 16) | (green << 8) | (blue), true);
            fontRenderer.drawString(((Integer)slider).toString(), this.posX + this.width - fontRenderer.getStringWidth(((Integer)slider).toString()) - 2 * this.padding, this.posY + this.padding, 0xFFFFFFFF, true);

            //Slider
            RenderUtil.draw2DRect(posX + 2 * padding, posY + height - padding - 3, width - 4 * padding, 2, 0F, 0F, 0F, 1F);
            RenderUtil.draw2DRect(posX + 2 * padding, posY + height - padding - 3, (width - 4 * padding) * slider / 255F, 2, 0.85F, 0.43F, 0F, 1F);


            if (dragging) {
                double wMin = posX + 2 * padding;
                double wMax = posX + width - 2 * padding;

                if (mouseX > wMax) {
                    return 255;
                } else if (mouseX < wMin) {
                    return 0;
                } else {
                    return Math.round(255 * (float) (mouseX - wMin) / (width - 4 * padding));
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
