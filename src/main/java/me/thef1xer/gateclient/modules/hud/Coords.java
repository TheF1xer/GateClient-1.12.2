package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class Coords extends Module {
    public static final Coords INSTANCE = new Coords();

    public Coords() {
        super("Coords", "coords", Module.ModuleCategory.HUD);
    }

    public void onRenderGameOverlay(ScaledResolution sr) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRenderer;

        //Round player coords
        float[] playerPos = {
                Math.round(mc.player.posX * 100D) / 100F,
                Math.round(mc.player.posY * 100D) / 100F,
                Math.round(mc.player.posZ * 100D) / 100F
        };

        // Draw Coords
        String coordsString = TextFormatting.GRAY + "XYZ: " + TextFormatting.RESET + playerPos[0] + ", " + playerPos[1] + ", " + playerPos[2];
        fr.drawStringWithShadow(coordsString, 4, sr.getScaledHeight() - fr.FONT_HEIGHT - 4, 0xFFFFFF);

        // Check player dimension
        if (mc.player.dimension == 0) {

            // (Overworld) Divide x and z coordinates by 8
            float[] netherPos = {
                    Math.round(mc.player.posX * 12.5D) / 100F,
                    playerPos[1], // y coordinate stays the same in the nether
                    Math.round(mc.player.posZ * 12.5D) / 100F
            };

            // Draw Nether coords
            String netherCoordsString = TextFormatting.GRAY + "Nether: " + TextFormatting.RESET + netherPos[0] + ", " + netherPos[1] + ", " + netherPos[2];
            fr.drawStringWithShadow(netherCoordsString, 4, sr.getScaledHeight() - 2 * fr.FONT_HEIGHT - 4, 0xFFFFFF);

        } else if (mc.player.dimension == -1) {

            // (Nether) multiply x and z coordinates by 8
            float[] OWPos = {
                    playerPos[0] * 8,
                    playerPos[1],
                    playerPos[2] * 8
            };

            // Draw Overworld coords
            String overworldCoordsString = TextFormatting.GRAY + "Overworld: " + TextFormatting.RESET + OWPos[0] + ", " + OWPos[1] + ", " + OWPos[2];
            fr.drawStringWithShadow(overworldCoordsString, 4, sr.getScaledHeight() - 2 * fr.FONT_HEIGHT - 4, 0xFFFFFF);
        }
    }
}
