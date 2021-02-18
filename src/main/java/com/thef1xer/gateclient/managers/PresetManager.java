package com.thef1xer.gateclient.managers;

import com.google.gson.*;
import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.*;

import java.io.*;
import java.util.*;

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
            GateClient.gate.configManager.activePreset = preset;
            GateClient.gate.configManager.save();
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
                Set<Map.Entry<String, JsonElement>> moduleSet = moduleObject.entrySet();

                for (Module module : GateClient.gate.moduleManager.moduleList) {

                    if (!this.contains(moduleSet, "name", new JsonPrimitive(module.getName()))) {
                        continue;
                    }

                    for (Map.Entry<String, JsonElement> value : moduleSet) {
                        String key = value.getKey();
                        JsonElement val = value.getValue();

                        if (key.equals("enabled")) {
                            module.setEnabled(val.getAsBoolean());
                            continue;
                        }

                        if (key.equals("keybind")) {
                            module.setKeyBind(val.getAsInt());
                            continue;
                        }

                        if (key.equals("drawOnHud")) {
                            module.setDrawOnHud(val.getAsBoolean());
                            continue;
                        }

                        if (key.equals("settings")) {

                            for (JsonElement element1 : val.getAsJsonArray()) {
                                if (!(element1 instanceof JsonObject)) {
                                    continue;
                                }

                                JsonObject settingObject = (JsonObject) element1;
                                Set<Map.Entry<String, JsonElement>> settingSet = settingObject.entrySet();

                                for (Setting setting : module.getSettings()) {
                                    if (!this.contains(settingSet, "id", new JsonPrimitive(setting.getId()))) {
                                        continue;
                                    }

                                    System.out.println("Setting " + setting.getName());

                                    for (Map.Entry<String, JsonElement> value1 : settingSet) {
                                        String settingKey = value1.getKey();
                                        JsonElement settingVal = value1.getValue();

                                        if (setting instanceof BooleanSetting) {
                                            if (settingKey.equals("value")) {
                                                ((BooleanSetting) setting).setValue(settingVal.getAsBoolean());
                                            }
                                        } else if (setting instanceof ColorSetting) {
                                            if (settingKey.equals("red")) {
                                                ((ColorSetting) setting).setRed(settingVal.getAsInt());
                                                continue;
                                            }

                                            if (settingKey.equals("green")) {
                                                ((ColorSetting) setting).setGreen(settingVal.getAsInt());
                                                continue;
                                            }

                                            if (settingKey.equals("blue")) {
                                                ((ColorSetting) setting).setBlue(settingVal.getAsInt());
                                                continue;
                                            }

                                            if (settingKey.equals("alpha")) {
                                                ((ColorSetting) setting).setAlpha(settingVal.getAsInt());
                                            }
                                        } else if (setting instanceof EnumSetting) {
                                            if (settingKey.equals("value")) {
                                                ((EnumSetting<?>) setting).setValueFromName(settingVal.getAsString());
                                            }
                                        } else if (setting instanceof FloatSetting) {
                                            if (settingKey.equals("value")) {
                                                ((FloatSetting) setting).setValue(settingVal.getAsFloat());
                                            }
                                        }
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

        this.saveActivePreset(preset);
    }

    public void saveActivePreset(File preset) {
        JsonObject presetJson = new JsonObject();

        JsonArray moduleArray = new JsonArray();
        for (Module module : GateClient.gate.moduleManager.moduleList) {
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
                } else if (setting instanceof EnumSetting) {
                    settingObject.addProperty("value", ((EnumSetting<?>) setting).getCurrentValueName());
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

    private boolean presetExists(File preset) {
        for (File file : presetList) {
            if (file.getPath().equals(preset.getPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(Set<Map.Entry<String, JsonElement>> set, String key, JsonElement value) {

        for (Map.Entry<String, JsonElement> entry : set) {
            if (entry.getKey().equals(key) && entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
