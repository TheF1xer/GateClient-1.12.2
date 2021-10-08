package me.thef1xer.gateclient.managers;

import com.google.gson.*;
import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.settings.impl.BooleanSetting;
import me.thef1xer.gateclient.settings.impl.RGBSetting;
import me.thef1xer.gateclient.settings.impl.EnumSetting;
import me.thef1xer.gateclient.settings.impl.FloatSetting;
import me.thef1xer.gateclient.util.DirectoryUtil;

import java.io.*;
import java.util.*;

public class PresetManager {
    //This might break and need a rework

    public final List<File> PRESET_LIST = new ArrayList<>();
    private File activePreset;
    private boolean autoSave;

    public void init() {
        this.updatePresetList();
        this.loadActivePreset();
    }

    public void setActivePreset(File activePreset) {
        this.activePreset = activePreset;
    }

    public File getActivePreset() {
        return activePreset;
    }

    public void setAutoSave(boolean autoSave) {
        if (this.autoSave != autoSave) {
            this.autoSave = autoSave;
            this.saveActivePreset();
        }
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void updatePresetList() {
        this.PRESET_LIST.clear();
        if (DirectoryUtil.PRESET_FOLDER.listFiles() != null) {
            for (File file : DirectoryUtil.PRESET_FOLDER.listFiles()) {
                if (!file.isDirectory() && DirectoryUtil.isJson(file)) {
                    this.PRESET_LIST.add(file);
                }
            }
        }
    }

    public void loadActivePreset() {
        if (!presetExists(getActivePreset())) {
            System.out.println("Preset not found");
            setActivePreset(new File(DirectoryUtil.PRESET_FOLDER, "default.json"));
            GateClient.getGate().configManager.save();

            if (!presetExists(getActivePreset())) {
                System.out.println("Default preset created");
                setAutoSave(true);
                saveActivePreset();
            }
        }

        JsonParser parser = new JsonParser();
        try {
            //Small optimizations can be done here
            JsonObject object = parser.parse(new FileReader(this.getActivePreset())).getAsJsonObject();
            JsonElement autoSave = object.get("auto save");
            setAutoSave(autoSave != null ? autoSave.getAsBoolean() : true);
            JsonArray moduleArray = object.getAsJsonArray("modules");

            for (JsonElement element : moduleArray) {
                if (!(element instanceof JsonObject)) {
                    continue;
                }
                JsonObject moduleObject = (JsonObject) element;
                Set<Map.Entry<String, JsonElement>> moduleSet = moduleObject.entrySet();

                for (Module module : GateClient.getGate().moduleManager.MODULE_LIST) {

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

                                    for (Map.Entry<String, JsonElement> value1 : settingSet) {
                                        String settingKey = value1.getKey();
                                        JsonElement settingVal = value1.getValue();

                                        if (setting instanceof BooleanSetting) {
                                            if (settingKey.equals("value")) {
                                                ((BooleanSetting) setting).setValue(settingVal.getAsBoolean());
                                            }
                                        } else if (setting instanceof EnumSetting) {
                                            if (settingKey.equals("value")) {
                                                ((EnumSetting) setting).setValueFromName(settingVal.getAsString());
                                            }
                                        } else if (setting instanceof FloatSetting) {
                                            if (settingKey.equals("value")) {
                                                ((FloatSetting) setting).setValue(settingVal.getAsFloat());
                                            }
                                        } else if (setting instanceof RGBSetting) {
                                            if (settingKey.equals("red")) {
                                                ((RGBSetting) setting).setRed(settingVal.getAsInt());
                                                continue;
                                            }

                                            if (settingKey.equals("green")) {
                                                ((RGBSetting) setting).setGreen(settingVal.getAsInt());
                                                continue;
                                            }

                                            if (settingKey.equals("blue")) {
                                                ((RGBSetting) setting).setBlue(settingVal.getAsInt());
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

        System.out.println("Preset loaded");
        saveActivePreset();
    }

    public void saveActivePreset() {
        JsonObject presetJson = new JsonObject();
        presetJson.addProperty("auto save", this.isAutoSave());

        JsonArray moduleArray = new JsonArray();
        for (Module module : GateClient.getGate().moduleManager.MODULE_LIST) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("name", module.getName());
            moduleObject.addProperty("enabled", module.isEnabled());
            moduleObject.addProperty("keybind", module.getKeyBind());

            JsonArray settingsArray = new JsonArray();

            for (Setting setting : module.getSettings()) {
                JsonObject settingObject = new JsonObject();
                settingObject.addProperty("id", setting.getId());
                if (setting instanceof BooleanSetting) {
                    settingObject.addProperty("value", ((BooleanSetting) setting).getValue());
                } else if (setting instanceof RGBSetting) {
                    settingObject.addProperty("red", ((RGBSetting) setting).getRed());
                    settingObject.addProperty("green", ((RGBSetting) setting).getGreen());
                    settingObject.addProperty("blue", ((RGBSetting) setting).getBlue());
                } else if (setting instanceof EnumSetting) {
                    settingObject.addProperty("value", ((EnumSetting) setting).getCurrentValueName());
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
            FileWriter writer = new FileWriter(this.getActivePreset());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(presetJson));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Preset saved");
    }

    public boolean createNewPreset(String path) {
        updatePresetList();
        for (File file : PRESET_LIST) {
            if (file.getName().equalsIgnoreCase(path)) {
                return false;
            }
        }

        setActivePreset(new File(DirectoryUtil.PRESET_FOLDER, path));
        setAutoSave(true);
        saveActivePreset();
        GateClient.getGate().configManager.save();
        return true;
    }

    public boolean removePreset(String presetName) {
        updatePresetList();
        for (File file : PRESET_LIST) {
            if (file.getName().equalsIgnoreCase(presetName)) {
                return file.delete();
            }
        }
        return false;
    }

    public void removeActivePreset() {
        getActivePreset().delete();
        updatePresetList();
        if (PRESET_LIST.size() != 0) {
            setActivePreset(PRESET_LIST.get(0));
            loadActivePreset();
        } else {
            setActivePreset(new File(DirectoryUtil.PRESET_FOLDER, "default.json"));
            setAutoSave(true);
            saveActivePreset();
            GateClient.getGate().configManager.save();
        }
    }

    private boolean presetExists(File preset) {
        for (File file : PRESET_LIST) {
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
