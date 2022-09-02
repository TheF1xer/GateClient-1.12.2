package me.thef1xer.gateclient.modules.hud;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

public class ModuleList extends Module {
    public static final ModuleList INSTANCE = new ModuleList();

    private List<Module> modulesSorted;
    private final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

    public final EnumSetting color = new EnumSetting("List Color", "color", Color.values(), Color.STATIC);
    public final RGBSetting staticColor = new RGBSetting("Static Color", "static", 255, 255, 255);

    public ModuleList() {
        super("Module List", "modulelist", Module.ModuleCategory.HUD);
        addSettings(color, staticColor);
    }

    public void onRenderGameOverlay(ScaledResolution sr) {
        int drawnModules = 0;
        for (Module module : this.modulesSorted) {

            // Check if module should be drawn
            if (module.isEnabled() && module.drawOnHud.getValue()) {
                int hexColor;

                if (color.getCurrentValue() == Color.RAINBOW) {

                    // Get rainbow with offset
                    int[] rainbow = ColorUtil.getRainbow(5, 0.1F * drawnModules);
                    hexColor = ColorUtil.RGBtoHex(rainbow[0], rainbow[1], rainbow[2]);

                } else if (color.getCurrentValue() == Color.CATEGORY) {

                    // Get color from category
                    hexColor = module.getModuleCategory().getColor();

                } else {

                    // Get color from setting
                    hexColor = ColorUtil.RGBtoHex(staticColor.getRed(), staticColor.getGreen(), staticColor.getBlue());

                }

                // Draw module name
                fr.drawStringWithShadow(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName()) - 4, 4 + drawnModules * fr.FONT_HEIGHT, hexColor);
                drawnModules++;
            }
        }
    }

    public void sortList() {
        modulesSorted = new ArrayList<>(GateClient.getGate().moduleManager.moduleList);

        // Sort module list by name width
        modulesSorted.sort((module1, module2) -> {
            if (fr.getStringWidth(module1.getName()) < fr.getStringWidth(module2.getName())) {
                return 1;

            } else if (fr.getStringWidth(module1.getName()) > fr.getStringWidth(module2.getName())) {
                return -1;

            }
            return 0;
        });
    }

    public enum Color {
        STATIC("Static"),
        CATEGORY("Category"),
        RAINBOW("Rainbow");

        private final String name;
        Color(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
