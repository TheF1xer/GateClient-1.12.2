package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.util.ChatUtil;
import com.thef1xer.gateclient.util.DirectoryUtil;
import net.minecraft.util.text.TextFormatting;

import java.io.File;

public class PresetCommand extends Command {
    public PresetCommand() {
        super("Preset", "Allows you to have multiple configurations", "preset", "preset list", "preset clear", "preset load <name>", "preset create <name>", "preset remove <name>");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            String preset = this.removeExtension(GateClient.gate.presetManager.activePreset.getFile().getName());
            ChatUtil.clientMessage("Current Preset: " + TextFormatting.GOLD + preset);
            return;
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("list")) {
                GateClient.gate.presetManager.updatePresetList();
                ChatUtil.clientMessage("Preset List:");
                for (File file : GateClient.gate.presetManager.presetList) {
                    if (GateClient.gate.presetManager.activePreset.getFile().equals(file)) {
                        ChatUtil.clientMessage(TextFormatting.GOLD + this.removeExtension(file.getName()));
                    } else {
                        ChatUtil.clientMessage(this.removeExtension(file.getName()));
                    }
                }
                return;
            }

            if (args[1].equalsIgnoreCase("clear")) {
                GateClient.gate.presetManager.updatePresetList();
                for (File file : GateClient.gate.presetManager.presetList) {
                    file.delete();
                }
                GateClient.gate.presetManager.activePreset.setFile(new File(DirectoryUtil.PRESET_FOLDER ,"default.json"));
                GateClient.gate.presetManager.saveActivePreset();
                GateClient.gate.configManager.save();
                ChatUtil.clientMessage("Preset list cleared");
                return;
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("load")) {
                GateClient.gate.presetManager.updatePresetList();
                for (File file : GateClient.gate.presetManager.presetList) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        GateClient.gate.presetManager.activePreset.setFile(file);
                        GateClient.gate.presetManager.loadActivePreset();
                        GateClient.gate.configManager.save();
                        ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2].toLowerCase() + TextFormatting.RESET + " loaded");
                        return;
                    }
                }
                ChatUtil.clientMessage("Preset not found");
                return;
            }

            if (args[1].equalsIgnoreCase("create")) {
                GateClient.gate.presetManager.updatePresetList();
                for (File file : GateClient.gate.presetManager.presetList) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        ChatUtil.clientMessage("That preset already exists, try another name");
                        return;
                    }
                }
                GateClient.gate.presetManager.activePreset.setFile(new File(DirectoryUtil.PRESET_FOLDER ,args[2] + ".json"));
                GateClient.gate.presetManager.saveActivePreset();
                ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " created");
                return;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                if (GateClient.gate.presetManager.activePreset.getFile().getName().equalsIgnoreCase(args[2] + ".json")) {
                    ChatUtil.clientMessage("You can't remove a preset that is active");
                    return;
                }
                GateClient.gate.presetManager.updatePresetList();
                for (File file : GateClient.gate.presetManager.presetList) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        file.delete();
                        ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " removed");
                        return;
                    }
                }
                ChatUtil.clientMessage("Preset not found");
                return;
            }
        }

        this.syntaxError();
    }

    private String removeExtension(String stringIn) {
        StringBuilder newString = new StringBuilder();
        for (char n : stringIn.toCharArray()) {
            if (n == ".".toCharArray()[0]) {
                break;
            }
            newString.append(n);
        }
        return newString.toString();
    }
}
