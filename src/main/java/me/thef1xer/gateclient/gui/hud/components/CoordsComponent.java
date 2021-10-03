package me.thef1xer.gateclient.gui.hud.components;

import me.thef1xer.gateclient.gui.hud.HUDComponent;
import me.thef1xer.gateclient.modules.hud.Coords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

public class CoordsComponent extends HUDComponent {
    @Override
    protected boolean isEnabled() {
        return Coords.INSTANCE.isEnabled();
    }

    @Override
    public void componentAction(ScaledResolution sr) {
        Vec3d pos = new Vec3d(Math.round(Minecraft.getMinecraft().player.posX * 100D) / 100D,
                Math.round(Minecraft.getMinecraft().player.posY * 100D) / 100D,
                Math.round(Minecraft.getMinecraft().player.posZ * 100D) / 100D);

        String coords = TextFormatting.GRAY + "XYZ: " + TextFormatting.RESET + pos.x + ", " + pos.y + ", " + pos.z;
        fr.drawStringWithShadow(coords, 4, sr.getScaledHeight() - fr.FONT_HEIGHT - 4, 0xFFFFFF);

        if (Minecraft.getMinecraft().player.dimension == 0) {
            Vec3d netherPos = new Vec3d(Math.round(Minecraft.getMinecraft().player.posX * 12.5D) / 100D,
                    Math.round(Minecraft.getMinecraft().player.posY * 100D) / 100D,
                    Math.round(Minecraft.getMinecraft().player.posZ * 12.5D) / 100D);

            String nether = TextFormatting.GRAY + "Nether: " + TextFormatting.RESET + netherPos.x + ", " + netherPos.y + ", " + netherPos.z;
            fr.drawStringWithShadow(nether, 4, sr.getScaledHeight() - 2 * fr.FONT_HEIGHT - 4, 0xFFFFFF);

        } else if (Minecraft.getMinecraft().player.dimension == -1) {
            Vec3d OWPos = new Vec3d(Math.round(Minecraft.getMinecraft().player.posX * 800D) / 100D,
                    Math.round(Minecraft.getMinecraft().player.posY * 100D) / 100D,
                    Math.round(Minecraft.getMinecraft().player.posZ * 800D) / 100D);

            String overworld = TextFormatting.GRAY + "Overworld: " + TextFormatting.RESET + OWPos.x + ", " + OWPos.y + ", " + OWPos.z;
            fr.drawStringWithShadow(overworld, 4, sr.getScaledHeight() - 2 * fr.FONT_HEIGHT - 4, 0xFFFFFF);
        }
    }
}
