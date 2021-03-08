package com.thef1xer.gateclient.gui.hud.components;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.gui.hud.HUDComponent;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.modules.hud.ModuleList;
import com.thef1xer.gateclient.util.ColorUtil;
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
                if (fr.getStringWidth(module1.getName()) < fr.getStringWidth(module2.getName())) {
                    return 1;
                } else if (fr.getStringWidth(module1.getName()) > fr.getStringWidth(module2.getName())) {
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    protected boolean isEnabled() {
        return ModuleList.INSTANCE.isEnabled();
    }

    @Override
    public void componentAction() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int i = 0;
        for (Module module : this.modulesSorted) {
            if (module.isEnabled() && module.drawOnHud.getValue()) {
                int color = 0;
                if (ModuleList.INSTANCE.color.getCurrentValue() == ModuleList.Color.RAINBOW) {
                    int[] rainbow = ColorUtil.getRainbow(5, 0.1F * i);
                    color = ColorUtil.RGBtoHex(rainbow[0], rainbow[1], rainbow[2]);
                } else {
                    color = module.getModuleCategory().getColor();
                }
                fr.drawStringWithShadow(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName()) - 4, 4 + i * fr.FONT_HEIGHT, color);
                i++;
            }
        }
    }
}
