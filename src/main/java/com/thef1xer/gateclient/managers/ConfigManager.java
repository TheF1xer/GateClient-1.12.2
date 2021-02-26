package com.thef1xer.gateclient.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.preset.Preset;
import com.thef1xer.gateclient.util.DirectoryUtil;

import java.io.*;

public class ConfigManager {
    public File configFile = new File(DirectoryUtil.GATE_FOLDER, "config.json");

    public void init() {
        DirectoryUtil.GATE_FOLDER.mkdir();
        DirectoryUtil.PRESET_FOLDER.mkdir();
        this.load();
    }

    public void save() {
        Preset activePreset = GateClient.gate.presetManager.activePreset;
        JsonObject config = new JsonObject();
        config.addProperty("Active Config", activePreset.getFile() != null ? activePreset.getFile().toString() : new File(DirectoryUtil.PRESET_FOLDER, "default.json").toString());
        try {
            FileWriter writer = new FileWriter(configFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(config));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        GateClient.gate.presetManager.updatePresetList();
        JsonParser parser = new JsonParser();

        if (!configFile.exists()) {
            this.save();
        }

        try {
            JsonObject config = parser.parse(new FileReader(configFile)).getAsJsonObject();
            String active = config.get("Active Config").getAsString();
            GateClient.gate.presetManager.activePreset.setFile(new File(active));
            this.save();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.save();
    }
}
