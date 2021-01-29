package com.thef1xer.gateclient.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thef1xer.gateclient.GateClient;
import net.minecraft.client.Minecraft;

import java.io.*;

public class ConfigManager {
    public File activePreset;
    public File gateFolder = new File(Minecraft.getMinecraft().mcDataDir, "Gate Client");
    public File presetFolder = new File(gateFolder, "Presets");
    public File configFile = new File(gateFolder, "config.json");

    public void init() {
        gateFolder.mkdir();
        presetFolder.mkdir();
        this.load();
        GateClient.gateClient.presetManager.updatePresetList(presetFolder);
        GateClient.gateClient.presetManager.loadActivePreset(activePreset, presetFolder);
    }

    public void save() {
        JsonObject config = new JsonObject();
        config.addProperty("Active Config", activePreset != null ? activePreset.toString() : new File(presetFolder, "default.json").toString());
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
        GateClient.gateClient.presetManager.updatePresetList(presetFolder);
        JsonParser parser = new JsonParser();

        if (!configFile.exists()) {
            this.save();
        }

        try {
            JsonObject config = parser.parse(new FileReader(configFile)).getAsJsonObject();
            String active = config.get("Active Config").getAsString();
            activePreset = new File(active);
            this.save();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
