package com.thef1xer.gateclient.gui.hud.components;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.hud.HUDComponent;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.modules.render.HUDModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModulesComponent extends HUDComponent {
    private List<Module> modulesSorted;

    public void init() {
        this.modulesSorted = new ArrayList<>(GateClient.gate.moduleManager.moduleList);
        this.modulesSorted.sort(new Comparator<Module>() {
            public int compare(Module module1, Module module2) {
                if (fr.getStringWidth(module1.getName()) > fr.getStringWidth(module2.getName())) {
                    return 1;
                } else if (fr.getStringWidth(module1.getName()) < fr.getStringWidth(module2.getName())) {
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    protected boolean isEnabled() {
        return HUDModule.INSTANCE.renderModuleList.getValue();
    }

    @Override
    public void componentAction() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int i = 0;
        for (Module module : this.modulesSorted) {
            if (module.isEnabled() && module.getDrawOnHud()) {
                fr.drawStringWithShadow(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName()) - 4, 4 + i * fr.FONT_HEIGHT, 0xFFFFFF);
                i++;
            }
        }
    }
}
