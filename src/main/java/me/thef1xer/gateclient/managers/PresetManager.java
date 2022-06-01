package me.thef1xer.gateclient.managers;

import com.google.gson.*;
import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.Setting;
import me.thef1xer.gateclient.settings.impl.*;
import me.thef1xer.gateclient.util.DirectoryUtil;
import net.minecraft.block.Block;

import java.io.*;
import java.util.*;

public class PresetManager {
    public final List<File> PRESET_LIST = new ArrayList<>();
    private File activePreset;
    private boolean autoSave;

    public void init() {
        this.updatePresetList();
        this.loadActivePreset();
    }

    public File getActivePreset() {
        return activePreset;
    }

    public void setActivePreset(File activePreset) {
        this.activePreset = activePreset;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        if (this.autoSave != autoSave) {
            this.autoSave = autoSave;
            this.saveActivePreset();
        }
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
            setActivePreset(new File(DirectoryUtil.PRESET_FOLDER, "default.json"));
            GateClient.getGate().configManager.save();

            // Create Preset if preset doesn't exist
            if (!presetExists(getActivePreset())) {
                setAutoSave(true);
                saveActivePreset();
            }
        }

        JsonParser parser = new JsonParser();
        try {
            JsonObject presetObject = parser.parse(new FileReader(this.getActivePreset())).getAsJsonObject();

            JsonElement autoSave = presetObject.get("auto save");
            setAutoSave(autoSave == null || autoSave.getAsBoolean());

            JsonArray moduleArray = presetObject.getAsJsonArray("modules");

            // Read Module information
            for (JsonElement moduleElement : moduleArray) {
                if (!(moduleElement instanceof JsonObject)) {
                    continue;
                }

                JsonObject moduleObject = (JsonObject) moduleElement;
                Set<Map.Entry<String, JsonElement>> moduleSet = moduleObject.entrySet();

                for (Module module : GateClient.getGate().moduleManager.MODULE_LIST) {

                    // Check if the name key coincides with the module name
                    if (!containsKeyAndValue(moduleSet, "name", new JsonPrimitive(module.getName()))) {
                        continue;
                    }

                    // Loop through entries in module object
                    for (Map.Entry<String, JsonElement> entry : moduleSet) {
                        String moduleKey = entry.getKey();
                        JsonElement moduleValue = entry.getValue();

                        if (moduleKey.equals("enabled")) {
                            module.setEnabled(moduleValue.getAsBoolean());
                            continue;
                        }

                        if (moduleKey.equals("keybind")) {
                            module.setKeyBind(moduleValue.getAsInt());
                            continue;
                        }

                        if (moduleKey.equals("settings")) {

                            for (JsonElement settingElement : moduleValue.getAsJsonArray()) {
                                if (!(settingElement instanceof JsonObject)) {
                                    continue;
                                }

                                JsonObject settingObject = (JsonObject) settingElement;
                                Set<Map.Entry<String, JsonElement>> settingSet = settingObject.entrySet();

                                for (Setting setting : module.getSettings()) {
                                    if (!containsKeyAndValue(settingSet, "id", new JsonPrimitive(setting.getId()))) {
                                        continue;
                                    }

                                    // Loop through entries in setting object
                                    for (Map.Entry<String, JsonElement> settingEntry : settingSet) {
                                        String settingKey = settingEntry.getKey();
                                        JsonElement settingVal = settingEntry.getValue();

                                        if (setting instanceof BlockListSetting) {
                                            if (settingKey.equals("list")) {
                                                ((BlockListSetting) setting).getBlockList().clear();

                                                for (JsonElement blockListElement : settingVal.getAsJsonArray()) {
                                                    Block block = Block.getBlockFromName(blockListElement.getAsString());
                                                    ((BlockListSetting) setting).getBlockList().add(block);
                                                }
                                            }

                                        } else if (setting instanceof BooleanSetting) {
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

        saveActivePreset();
    }

    public void saveActivePreset() {
        JsonObject presetJson = new JsonObject();
        presetJson.addProperty("auto save", this.isAutoSave());

        // Modules
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

                if (setting instanceof BlockListSetting) {
                    JsonArray blockList = new JsonArray();
                    for (Block block : ((BlockListSetting) setting).getBlockList()) {
                        blockList.add(Block.REGISTRY.getNameForObject(block).toString());
                    }
                    settingObject.add("list", blockList);

                } else if (setting instanceof BooleanSetting) {
                    settingObject.addProperty("value", ((BooleanSetting) setting).getValue());

                } else if (setting instanceof EnumSetting) {
                    settingObject.addProperty("value", ((EnumSetting) setting).getCurrentValueName());

                } else if (setting instanceof FloatSetting) {
                    settingObject.addProperty("value", ((FloatSetting) setting).getValue());

                } else if (setting instanceof RGBSetting) {
                    settingObject.addProperty("red", ((RGBSetting) setting).getRed());
                    settingObject.addProperty("green", ((RGBSetting) setting).getGreen());
                    settingObject.addProperty("blue", ((RGBSetting) setting).getBlue());

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
        }
        GateClient.getGate().configManager.save();
    }

    private boolean presetExists(File preset) {
        for (File file : PRESET_LIST) {
            if (file.getPath().equals(preset.getPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsKeyAndValue(Set<Map.Entry<String, JsonElement>> set, String key, JsonElement value) {

        for (Map.Entry<String, JsonElement> entry : set) {
            if (entry.getKey().equals(key) && entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
