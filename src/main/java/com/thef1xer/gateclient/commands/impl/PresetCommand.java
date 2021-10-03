package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.managers.PresetManager;
import com.thef1xer.gateclient.util.ChatUtil;
import com.thef1xer.gateclient.util.DirectoryUtil;
import net.minecraft.util.text.TextFormatting;

import java.io.File;

public class PresetCommand extends Command {
    private final PresetManager presetManager = GateClient.getGate().presetManager;

    public PresetCommand() {
        super("preset", "Allows you to have multiple configurations", "preset", "preset list", "preset clear", "preset load <name>", "preset create <name>", "preset save", "preset remove <name>", "preset autosave <true/false>");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            String preset = DirectoryUtil.removeExtension(presetManager.getActivePreset().getName());
            ChatUtil.clientMessage("Current Preset: " + TextFormatting.GOLD + preset + TextFormatting.WHITE + " Auto Save: " + TextFormatting.GOLD + presetManager.isAutoSave());
            return;
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("list")) {
                presetManager.updatePresetList();
                ChatUtil.clientMessage(TextFormatting.BOLD + "Preset List:");
                for (File file : presetManager.PRESET_LIST) {
                    if (presetManager.getActivePreset().equals(file)) {
                        ChatUtil.clientMessage(TextFormatting.GOLD + DirectoryUtil.removeExtension(file.getName()));
                    } else {
                        ChatUtil.clientMessage(DirectoryUtil.removeExtension(file.getName()));
                    }
                }
                return;
            }

            if (args[1].equalsIgnoreCase("clear")) {
                presetManager.updatePresetList();
                for (File file : presetManager.PRESET_LIST) {
                    file.delete();
                }
                presetManager.createNewPreset("default.json");
                ChatUtil.clientMessage("Preset list cleared");
                return;
            }

            if (args[1].equalsIgnoreCase("save")) {
                presetManager.saveActivePreset();
                ChatUtil.clientMessage("Preset saved");
                return;
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("load")) {
                presetManager.updatePresetList();
                for (File file : presetManager.PRESET_LIST) {
                    if (file.getName().equalsIgnoreCase(args[2] + ".json")) {
                        presetManager.setActivePreset(file);
                        GateClient.getGate().configManager.save();
                        ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2].toLowerCase() + TextFormatting.RESET + " loaded");
                        return;
                    }
                }
                ChatUtil.clientMessage("Preset not found");
                return;
            }

            if (args[1].equalsIgnoreCase("create")) {
                if (presetManager.createNewPreset(args[2] + ".json")) {
                    ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " created");
                } else {
                    ChatUtil.clientMessage("That preset already exists, try another name");
                }
                return;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                if (presetManager.getActivePreset().getName().equalsIgnoreCase(args[2] + ".json")) {
                    presetManager.removeActivePreset();
                    ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " removed");
                    return;

                }

                if (presetManager.removePreset(args[2] + ".json")) {
                    ChatUtil.clientMessage("Preset " + TextFormatting.GOLD + args[2] + TextFormatting.RESET + " removed");
                } else {
                    ChatUtil.clientMessage("Preset not found");
                }

                return;
            }

            if (args[1].equalsIgnoreCase("autosave")) {
                if (args[2].equalsIgnoreCase("true")) {
                    presetManager.setAutoSave(true);
                    ChatUtil.clientMessage("The current Preset will now be saved automatically");
                } else if (args[2].equalsIgnoreCase("false")) {
                    presetManager.setAutoSave(false);
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
