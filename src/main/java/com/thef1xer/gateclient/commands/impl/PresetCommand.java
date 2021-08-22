package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.util.ChatUtil;
import com.thef1xer.gateclient.util.DirectoryUtil;
import net.minecraft.util.text.TextFormatting;

import java.io.File;

public class PresetCommand extends Command {
    public PresetCommand() {
        super("preset", "Allows you to have multiple configurations", "preset", "preset list", "preset clear", "preset load <name>", "preset create <name>", "preset save", "preset remove <name>", "preset autosave <true/false>");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            String preset = DirectoryUtil.removeExtension(GateClient.getGate().presetManager.getActivePreset().getName());
            ChatUtil.clientMessage("Current Preset: " + TextFormatting.GOLD + preset + TextFormatting.WHITE + " Auto Save: " + TextFormatting.GOLD + GateClient.getGate().presetManager.isAutoSave());
            return;
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("list")) {
                GateClient.getGate().presetManager.updatePresetList();
                ChatUtil.clientMessage(TextFormatting.BOLD + "Preset List:");
                for (File file : GateClient.getGate().presetManager.PRESET_LIST) {
                    if (GateClient.getGate().presetManager.getActivePreset().equals(file)) {
                        ChatUtil.clientMessage(TextFormatting.GOLD + DirectoryUtil.removeExtension(file.getName()));
                    } else {
                        ChatUtil.clientMessage(DirectoryUtil.removeExtension(file.getName()));
                    }
                }
                return;
            }

            if (args[1].equalsIgnoreCase("clear")) {
                GateClient.getGate().presetManager.updatePresetList();
                for (File file : GateClient.getGate().presetManager.PRESET_LIST) {
                    file.delete();
                }
                GateClient.getGate().presetManager.createNewPreset("default.json");
                ChatUtil.clientMessage("Preset list cleared");
                return;
            }

            if (args[1].equalsIgnoreCase("save")) {
                GateClient.getGate().presetManager.saveActivePreset();
                ChatUtil.clientMessage("Preset saved");
                return;
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("load")) {
                GateClient.getGate().presetManager.updatePresetList();
                for (File file : GateClient.getGate().presetManager.PRESET_LIST) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        GateClient.getGate().presetManager.setActivePreset(file);
                        GateClient.getGate().configManager.save();
                        ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2].toLowerCase() + TextFormatting.RESET + " loaded");
                        return;
                    }
                }
                ChatUtil.clientMessage("Preset not found");
                return;
            }

            if (args[1].equalsIgnoreCase("create")) {
                GateClient.getGate().presetManager.updatePresetList();
                for (File file : GateClient.getGate().presetManager.PRESET_LIST) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        ChatUtil.clientMessage("That preset already exists, try another name");
                        return;
                    }
                }
                GateClient.getGate().presetManager.createNewPreset(args[2] + ".json");
                ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " created");
                return;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                if (GateClient.getGate().presetManager.getActivePreset().getName().equalsIgnoreCase(args[2] + ".json")) {
                    ChatUtil.clientMessage("You can't remove a preset that is active");
                    return;
                }
                GateClient.getGate().presetManager.updatePresetList();
                for (File file : GateClient.getGate().presetManager.PRESET_LIST) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        if (file.delete()) {
                            ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " removed");
                        } else {
                            ChatUtil.clientMessage("Preset was not able to be removed");
                        }
                        return;
                    }
                }
                ChatUtil.clientMessage("Preset not found");
                return;
            }

            if (args[1].equalsIgnoreCase("autosave")) {
                if (args[2].equalsIgnoreCase("true")) {
                    GateClient.getGate().presetManager.setAutoSave(true);
                    ChatUtil.clientMessage("The current Preset will now be saved automatically");
                } else if (args[2].equalsIgnoreCase("false")) {
                    GateClient.getGate().presetManager.setAutoSave(false);
                    ChatUtil.clientMessage("The current Preset will no longer be saved automatically");
                } else {
                    ChatUtil.clientMessage("The second argument must be either true or false");
                }

                return;
            }
        }

        this.syntaxError();
    }
}
