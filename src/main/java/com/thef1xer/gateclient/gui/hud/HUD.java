package com.thef1xer.gateclient.gui.hud;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HUD {
    public List<Module> modulesSorted = new ArrayList<>();
    public boolean renderModules = false;
    FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    public void init() {
        modulesSorted = GateClient.moduleManager.moduleList;
        modulesSorted.sort(new ModuleComparator());
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        //Check if Text is being rendered
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            if (renderModules) {
                int i = 0;
                for (Module module : GateClient.moduleManager.moduleList) {
                    if (module.isEnabled() && module.getDrawOnHud()) {
                        fr.drawStringWithShadow(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName()) - 4, 4 + i * fr.FONT_HEIGHT, 0xFFFFFF);
                        i++;
                    }
                }
            }
        }
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
