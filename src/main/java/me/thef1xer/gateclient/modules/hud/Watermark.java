package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.util.ColorUtil;
import me.thef1xer.gateclient.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class Watermark extends Module {
    public static final Watermark INSTANCE = new Watermark();

    private final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    public Watermark() {
        super("Watermark", "watermark", ModuleCategory.HUD);
        setEnabled(true);
    }

    public void drawWatermark() {
        int[] rainbow = ColorUtil.getRainbow(5, 0);
        int rainbowHex = ColorUtil.RGBtoHex(rainbow[0], rainbow[1], rainbow[2]);

        GlStateManager.pushMatrix();
        GlStateManager.translate(5, 5, 0);
        GlStateManager.scale(1.5F, 1.5F, 1);
        int nameEnd = fr.drawStringWithShadow(Reference.NAME, 0, 0, rainbowHex);

        GlStateManager.scale((float) 2/3, (float) 2/3, 1);
        GlStateManager.translate(1.5 * nameEnd, 4, 0);
        fr.drawStringWithShadow(Reference.VERSION, 0, 0, 0x909090);
        GlStateManager.popMatrix();
    }
}
