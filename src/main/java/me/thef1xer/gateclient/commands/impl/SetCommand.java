package me.thef1xer.gateclient.commands.impl;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.commands.Command;
import me.thef1xer.gateclient.modules.Module;
import me.thef1xer.gateclient.settings.impl.*;
import me.thef1xer.gateclient.util.ChatUtil;
import me.thef1xer.gateclient.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;

public class SetCommand extends Command {
    private final GateClient gate = GateClient.getGate();

    public SetCommand() {
        super("set", "Changes the settings of a Module", "set <module> <setting> <value>", "set <module> <setting>", "set <module> list");
    }

    @Override
    public void onCommand(String[] args) {
        Module commandModule = getModuleFromId(args[1]);

        if (commandModule == null) {
            syntaxError();
            return;
        }


        if (args.length == 2) {

            // set <module>
            // Print module setting list
            ChatUtil.clientMessage(TextFormatting.BOLD + "Settings for " + commandModule.getName() + " module:");
            for (Setting setting : commandModule.getSettings()) {
                ChatUtil.clientMessage(setting.getCommandSyntax());
            }

            return;
        }


        if (args.length == 3) {

            // set <module> list
            if (args[2].equalsIgnoreCase("list")) {

                // Print module setting list
                ChatUtil.clientMessage(TextFormatting.BOLD + "Settings for " + commandModule.getName() + " module:");
                for (Setting setting : commandModule.getSettings()) {
                    ChatUtil.clientMessage(setting.getCommandSyntax());
                }

                return;
            }


            // set <module> <setting>
            for (Setting setting : commandModule.getSettings()) {
                if (args[2].equalsIgnoreCase(setting.getId())) {
                    ChatUtil.clientMessage(setting.getCommandSyntax());
                    return;
                }
            }


            syntaxError();
            return;
        }




        // set <module> <setting> <values...>
        for (Setting setting : commandModule.getSettings()) {

            if (args[2].equalsIgnoreCase(setting.getId())) {



                // Block List Setting
                if (setting instanceof BlockListSetting) {

                    if (args.length == 4) {

                        // set <module> <setting> list
                        if (args[3].equalsIgnoreCase("list")) {
                            ChatUtil.clientMessage(TextFormatting.BOLD + "Block List:");

                            for (Block block : ((BlockListSetting) setting).getBlockList()) {
                                ChatUtil.clientMessage(TextFormatting.GOLD + block.getLocalizedName() + TextFormatting.WHITE + " (" + Block.REGISTRY.getNameForObject(block) + ")");
                            }

                            return;
                        }
                    }

                    if (args.length == 5) {

                        // set <module> <setting> add <block>
                        if (args[3].equalsIgnoreCase("add")) {
                            Block blockToAdd = Block.getBlockFromName(args[4]);

                            // Block not found
                            if (blockToAdd == null) {
                                ChatUtil.clientMessage("Block not found");
                                return;
                            }

                            // Block is already in the list
                            if (((BlockListSetting) setting).getBlockList().contains(blockToAdd)) {
                                ChatUtil.clientMessage(TextFormatting.GOLD + blockToAdd.getLocalizedName() + TextFormatting.WHITE + " is already in the Block List");
                                return;
                            }

                            ((BlockListSetting) setting).getBlockList().add(blockToAdd);
                            ChatUtil.clientMessage(TextFormatting.GOLD + blockToAdd.getLocalizedName() + TextFormatting.WHITE + " added to the Block List");

                            if (GateClient.getGate().presetManager.isAutoSave()) {
                                GateClient.getGate().presetManager.saveActivePreset();
                            }

                            return;
                        }

                        // set <module> <setting> remove <block>
                        if (args[3].equalsIgnoreCase("remove")) {
                            Block blockToRemove = Block.getBlockFromName(args[4]);

                            if (blockToRemove == null) {
                                ChatUtil.clientMessage("That block does not exist");
                                return;
                            }

                            for (Block settingBlock : ((BlockListSetting) setting).getBlockList()) {
                                if (blockToRemove == settingBlock) {

                                    ((BlockListSetting) setting).getBlockList().remove(blockToRemove);
                                    ChatUtil.clientMessage(TextFormatting.GOLD + blockToRemove.getLocalizedName() + TextFormatting.WHITE + " removed from the Block List");

                                    if (GateClient.getGate().presetManager.isAutoSave()) {
                                        GateClient.getGate().presetManager.saveActivePreset();
                                    }

                                    return;
                                }
                            }

                            ChatUtil.clientMessage(TextFormatting.GOLD + blockToRemove.getLocalizedName() + TextFormatting.WHITE + " is not in the Block List");
                            return;
                        }
                    }

                    ChatUtil.clientMessage("Arguments must be " + TextFormatting.GOLD + "<add / remove / list> <block id to add or remove>");
                    return;
                }



                // Boolean Settings
                if (setting instanceof BooleanSetting) {

                    if (args.length == 4) {

                        // set <module> <setting> <true>
                        if (args[3].equalsIgnoreCase("true")) {
                            ((BooleanSetting) setting).setValue(true);

                            ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + "true");

                            if (GateClient.getGate().presetManager.isAutoSave()) {
                                GateClient.getGate().presetManager.saveActivePreset();
                            }

                            return;
                        }

                        // set <module> <setting> <false>
                        if (args[3].equalsIgnoreCase("false")) {
                            ((BooleanSetting) setting).setValue(false);

                            ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + "false");

                            if (GateClient.getGate().presetManager.isAutoSave()) {
                                GateClient.getGate().presetManager.saveActivePreset();
                            }

                            return;
                        }
                    }

                    // set <module> <setting> <invalid...>
                    ChatUtil.clientMessage("Value must be " + TextFormatting.GOLD + "true" + TextFormatting.WHITE + " or " + TextFormatting.GOLD + "false");
                    return;
                }



                // Enum Settings
                if (setting instanceof EnumSetting) {

                    if (args.length == 4) {

                        // set <module> <setting> <value>
                        if (((EnumSetting) setting).setValueFromName(args[3])) {
                            ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + ((EnumSetting) setting).getCurrentValueName());

                            if (GateClient.getGate().presetManager.isAutoSave()) {
                                GateClient.getGate().presetManager.saveActivePreset();
                            }

                            return;
                        }
                    }

                    // set <module> <setting> <invalid...>
                    ChatUtil.clientMessage("Value not valid");
                    return;
                }



                // Float Settings
                if (setting instanceof FloatSetting) {

                    if (args.length == 4) {

                        // set <module> <setting> <value>
                        try {

                            // Parse float
                            float floatValue = Float.parseFloat(args[3]);

                            if (((FloatSetting) setting).setValue(floatValue)) {
                                ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + floatValue);

                                if (GateClient.getGate().presetManager.isAutoSave()) {
                                    GateClient.getGate().presetManager.saveActivePreset();
                                }

                                return;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    // set <module> <setting> <invalid...>
                    ChatUtil.clientMessage("Value must be a float value between " + TextFormatting.GOLD + ((FloatSetting) setting).getMin() + TextFormatting.WHITE + " and " + TextFormatting.GOLD + ((FloatSetting) setting).getMax());
                    return;
                }



                // RGB Settings
                if (setting instanceof RGBSetting) {

                    if (args.length == 6) {

                        // set <module> <setting> <red> <green> <blue>
                        int r, g, b;
                        try {
                            r = Integer.parseInt(args[3]);
                            g = Integer.parseInt(args[4]);
                            b = Integer.parseInt(args[5]);
                        } catch (Exception e) {
                            ChatUtil.clientMessage("Red, Green and Blue must be integers");
                            return;
                        }

                        if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
                            ChatUtil.clientMessage("Red, Green and Blue must be between " + TextFormatting.GOLD + "0" + TextFormatting.WHITE + " and " + TextFormatting.GOLD + "255");
                            return;
                        }

                        ((RGBSetting) setting).setRed(r);
                        ((RGBSetting) setting).setGreen(g);
                        ((RGBSetting) setting).setBlue(b);

                        ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + "(" + r + ", " + g + ", " + b + ")");

                        if (GateClient.getGate().presetManager.isAutoSave()) {
                            GateClient.getGate().presetManager.saveActivePreset();
                        }

                        return;

                    }

                    // set <module> <setting> <invalid...>
                    ChatUtil.clientMessage("Values must be " + TextFormatting.GOLD + "<red> <green> <blue>");
                    return;
                }
            }
        }

        syntaxError();
    }

    private Module getModuleFromId(String moduleId) {
        for (Module module : gate.moduleManager.MODULE_LIST) {
            if (module.getId().equalsIgnoreCase(moduleId)) {
                return module;
            }
        }
        return null;
    }
}
