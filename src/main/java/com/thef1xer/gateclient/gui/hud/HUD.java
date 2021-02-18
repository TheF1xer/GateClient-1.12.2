package com.thef1xer.gateclient.gui.hud;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HUD {
    //TODO: Rework this whole thing

    private boolean renderModules = false;
    private boolean renderCoords = false;

    private List<Module> modulesSorted = new ArrayList<>();

    public void init() {
        modulesSorted = GateClient.gate.moduleManager.moduleList;
        modulesSorted.sort(new ModuleComparator());
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

            if (this.renderModules) {
                int i = 0;
                for (Module module : this.modulesSorted) {
                    if (module.isEnabled() && module.getDrawOnHud()) {
                        fr.drawStringWithShadow(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName()) - 4, 4 + i * fr.FONT_HEIGHT, 0xFFFFFF);
                        i++;
                    }
                }
            }

            if (this.renderCoords) {
                Vec3d pos = new Vec3d(Math.round(Minecraft.getMinecraft().player.posX * 100D) / 100D,
                        Math.round(Minecraft.getMinecraft().player.posY * 100D) / 100D,
                        Math.round(Minecraft.getMinecraft().player.posZ * 100D) / 100D);
                Vec3d netherPos = new Vec3d(Math.round(Minecraft.getMinecraft().player.posX * 12.5D) / 100D,
                        Math.round(Minecraft.getMinecraft().player.posY * 100D) / 100D,
                        Math.round(Minecraft.getMinecraft().player.posZ * 12.5D) / 100D);
                String coords = TextFormatting.GRAY + "XYZ: " + TextFormatting.RESET + pos.x + ", " + pos.y + ", " + pos.z;
                String nether = TextFormatting.GRAY + "Nether: " + TextFormatting.RESET + netherPos.x + ", " + netherPos.y + ", " + netherPos.z;

                fr.drawStringWithShadow(coords, 4, sr.getScaledHeight() - 2 * fr.FONT_HEIGHT - 4, 0xFFFFFF);
                fr.drawStringWithShadow(nether, 4, sr.getScaledHeight() - fr.FONT_HEIGHT, 0xFFFFFF);
            }
        }
    }

    public void setRenderModules(boolean renderModules) {
        this.renderModules = renderModules;
    }

    public void setRenderCoords(boolean renderCoords) {
        this.renderCoords = renderCoords;
    }

    public static class ModuleComparator implements Comparator<Module> {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

        @Override
        public int compare(Module module1, Module module2) {
            if (fr.getStringWidth(module1.getName()) > fr.getStringWidth(module2.getName())) {
                return 1;
            } else if (fr.getStringWidth(module1.getName()) < fr.getStringWidth(module2.getName())) {
                return -1;
            }
            return 0;
        }
    }
}
