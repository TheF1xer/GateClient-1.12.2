package com.thef1xer.gateclient.managers;

import com.google.gson.*;
import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.BooleanSetting;
import com.thef1xer.gateclient.settings.ColorSetting;
import com.thef1xer.gateclient.settings.FloatSetting;
import com.thef1xer.gateclient.settings.Setting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PresetManager {
    public List<File> presetList = new ArrayList<>();

    public void updatePresetList(File path) {
        if (path.listFiles() != null) {
            for (File file : path.listFiles()) {
                if (!file.isDirectory() && this.isJson(file.getName())) {
                    presetList.add(file);
                }
            }
        }
    }

    public boolean isJson(String name) {
        int index = name.lastIndexOf(".");
        return name.substring(index).equals(".json");
    }

    public void loadActivePreset(File preset, File folder) {
        if (!this.presetExists(preset)) {
            preset = new File(folder, "default.json");
            GateClient.gateClient.configManager.activePreset = preset;
            GateClient.gateClient.configManager.save();
            if (!this.presetExists(preset)) {
                this.saveActivePreset(preset);
            }
        }

        JsonParser parser = new JsonParser();
        try {
            JsonObject object = parser.parse(new FileReader(preset)).getAsJsonObject();
            JsonArray moduleArray = object.getAsJsonArray("modules");

            for (JsonElement element : moduleArray) {
                if (!(element instanceof JsonObject)) {
                    continue;
                }
                JsonObject moduleObject = (JsonObject) element;
                for (Module module : GateClient.gateClient.moduleManager.moduleList) {
                    if (moduleObject.get("name").getAsString().equals(module.getName())) {
                        boolean enabled = moduleObject.get("enabled").getAsBoolean();
                        int keybind = moduleObject.get("keybind").getAsInt();
                        boolean drawOnHud = moduleObject.get("drawOnHud").getAsBoolean();

                        module.setEnabled(enabled);
                        module.setKeyBind(keybind);
                        module.setDrawOnHud(drawOnHud);

                        for (JsonElement element1 : moduleObject.getAsJsonArray("settings")) {
                            if (!(element1 instanceof JsonObject)) {
                                continue;
                            }
                            JsonObject settingObject = (JsonObject) element1;
                            for (Setting setting : module.getSettings()) {
                                if (settingObject.get("id").getAsString().equals(setting.getId())) {
                                    if (setting instanceof BooleanSetting) {
                                        ((BooleanSetting) setting).setValue(settingObject.get("value").getAsBoolean());
                                    } else if (setting instanceof ColorSetting) {
                                        ((ColorSetting) setting).setRed(settingObject.get("red").getAsInt());
                                        ((ColorSetting) setting).setGreen(settingObject.get("green").getAsInt());
                                        ((ColorSetting) setting).setBlue(settingObject.get("blue").getAsInt());
                                        ((ColorSetting) setting).setAlpha(settingObject.get("alpha").getAsInt());
                                    } else if (setting instanceof FloatSetting) {
                                        ((FloatSetting) setting).setValue(settingObject.get("value").getAsFloat());
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveActivePreset(File preset) {
        JsonObject presetJson = new JsonObject();

        JsonArray moduleArray = new JsonArray();
        for (Module module : GateClient.gateClient.moduleManager.moduleList) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("name", module.getName());
            moduleObject.addProperty("enabled", module.isEnabled());
            moduleObject.addProperty("keybind", module.getKeyBind());
            moduleObject.addProperty("drawOnHud", module.getDrawOnHud());

            JsonArray settingsArray = new JsonArray();

            for (Setting setting : module.getSettings()) {
                JsonObject settingObject = new JsonObject();
                settingObject.addProperty("id", setting.getId());
                if (setting instanceof BooleanSetting) {
                    settingObject.addProperty("value", ((BooleanSetting) setting).getValue());
                } else if (setting instanceof ColorSetting) {
                    settingObject.addProperty("red", ((ColorSetting) setting).getRed());
                    settingObject.addProperty("green", ((ColorSetting) setting).getGreen());
                    settingObject.addProperty("blue", ((ColorSetting) setting).getBlue());
                    settingObject.addProperty("alpha", ((ColorSetting) setting).getAlpha());
                } else if (setting instanceof FloatSetting) {
                    settingObject.addProperty("value", ((FloatSetting) setting).getValue());
                }
                settingsArray.add(settingObject);
            }

            moduleObject.add("settings", settingsArray);

            moduleArray.add(moduleObject);
        }

        presetJson.add("modules", moduleArray);

        try {
            FileWriter writer = new FileWriter(preset);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(presetJson));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean presetExists(File preset) {
        for (File file : presetList) {
            if (file.getPath().equals(preset.getPath())) {
                return true;
            }
        }
        return false;
    }
}
