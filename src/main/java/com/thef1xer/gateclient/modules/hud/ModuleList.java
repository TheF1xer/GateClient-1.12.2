package com.thef1xer.gateclient.modules.hud;

import com.thef1xer.gateclient.modules.EnumModuleCategory;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.impl.EnumSetting;

public class ModuleList extends Module {
    public static final ModuleList INSTANCE = new ModuleList();

    public final EnumSetting<Color> color = new EnumSetting<>("Module List Color", "color", Color.values(), Color.CATEGORY);

    public ModuleList() {
        super("Module List", "modulelist", EnumModuleCategory.HUD);
        this.addSettings(color);
    }

    public enum Color {
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
