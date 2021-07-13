package com.thef1xer.gateclient.commands.impl;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.*;
import com.thef1xer.gateclient.settings.impl.BooleanSetting;
import com.thef1xer.gateclient.settings.impl.RGBSetting;
import com.thef1xer.gateclient.settings.impl.EnumSetting;
import com.thef1xer.gateclient.settings.impl.FloatSetting;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.core.util.Integers;

public class SetCommand extends Command {
    private Module module;

    public SetCommand() {
        super("set", "Changes the settings of a Module", "set <module> <setting> <value>", "set <module> <setting>", "set <module> list");
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 3) {
            if (isModule(args[1])) {
                if (args[2].equalsIgnoreCase("list")) {
                    ChatUtil.clientMessage(TextFormatting.BOLD + "Settings for " + module.getName() + " module:");
                    for (Setting setting : module.getSettings()) {
                        sendMessageSetting(setting);
                    }
                    return;
                }

                for (Setting setting : module.getSettings()) {
                    if (setting.getId().equalsIgnoreCase(args[2])) {
                        sendMessageSetting(setting);
                        return;
                    }
                }
            }
        }

        if (args.length >= 4) {

            if (isModule(args[1])) {

                for (Setting setting : module.getSettings()) {

                    if (args[2].equalsIgnoreCase(setting.getId())) {

                        if (setting instanceof BooleanSetting) {
                            if (args.length == 4) {
                                if (args[3].equalsIgnoreCase("true")) {
                                    ((BooleanSetting) setting).setValue(true);

                                    ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + "true");
                                    if (GateClient.getGate().presetManager.isAutoSave()) {
                                        GateClient.getGate().presetManager.saveActivePreset();
                                    }
                                    return;
                                } else if (args[3].equalsIgnoreCase("false")) {
                                    ((BooleanSetting) setting).setValue(false);

                                    ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + "false");
                                    if (GateClient.getGate().presetManager.isAutoSave()) {
                                        GateClient.getGate().presetManager.saveActivePreset();
                                    }
                                    return;
                                }
                            }
                            ChatUtil.clientMessage("Value must be " + TextFormatting.GOLD + "true" + TextFormatting.WHITE + " or " + TextFormatting.GOLD + "false");

                        } else if (setting instanceof RGBSetting) {
                            if (args.length == 6) {
                                int r, g, b;
                                try {
                                    r = Integers.parseInt(args[3]);
                                    g = Integers.parseInt(args[4]);
                                    b = Integers.parseInt(args[5]);
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

                            ChatUtil.clientMessage("Values must be " + TextFormatting.GOLD + "<red> <green> <blue>");

                        } else if (setting instanceof  EnumSetting) {
                            if (args.length == 4) {
                                if (((EnumSetting) setting).setValueFromName(args[3])) {
                                    ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + ((EnumSetting) setting).getCurrentValueName());
                                    if (GateClient.getGate().presetManager.isAutoSave()) {
                                        GateClient.getGate().presetManager.saveActivePreset();
                                    }
                                    return;
                                }
                            }

                            ChatUtil.clientMessage("Value not valid");
                        } else if (setting instanceof FloatSetting) {
                            if (args.length == 4) {
                                try {
                                    float f = Float.parseFloat(args[3]);
                                    if (((FloatSetting) setting).setValue(f)) {
                                        ChatUtil.clientMessage(setting.getName() + " set to " + TextFormatting.GOLD + f);
                                        if (GateClient.getGate().presetManager.isAutoSave()) {
                                            GateClient.getGate().presetManager.saveActivePreset();
                                        }
                                        return;
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            ChatUtil.clientMessage("Value must be a float value between " + TextFormatting.GOLD + ((FloatSetting) setting).getMin() + TextFormatting.WHITE + " and " + TextFormatting.GOLD + ((FloatSetting) setting).getMax());
                        }

                        return;
                    }
                }
            }
        }

        this.syntaxError();
    }

    private boolean isModule(String s) {
        for (Module module : GateClient.getGate().moduleManager.MODULE_LIST) {
            if (module.getId().equalsIgnoreCase(s)) {
                this.module = module;
                return true;
            }
        }
        return false;
    }

    private void sendMessageSetting(Setting setting) {
        String value = "";
        String current = "";
        if (setting instanceof BooleanSetting) {
            value = "<true / false>";
            current = ((BooleanSetting) setting).getValue() ? "true" : "false";
        } else if (setting instanceof  RGBSetting) {
            value = "<red> <green> <blue>";
            current = ((RGBSetting) setting).getRed() + ", " + ((RGBSetting) setting).getGreen() + ", " +
                    ((RGBSetting) setting).getBlue();
        } else if (setting instanceof EnumSetting) {
            value = "<";
            for (int i = 0; i < ((EnumSetting) setting).getValues().length; i++) {
                value = value.concat(((EnumSetting) setting).getValues()[i].toString());
                if (i == ((EnumSetting) setting).getValues().length - 1) {
                    value = value.concat(">");
                } else {
                    value = value.concat(" / ");
                }
            }
            current = ((EnumSetting) setting).getCurrentValueName() + "]";
        } else if (setting instanceof FloatSetting) {
            value = "<number between " + ((FloatSetting) setting).getMin() + " and " + ((FloatSetting) setting).getMax() + ">";
            current = ((FloatSetting) setting).getValue() + "";
        }

        ChatUtil.clientMessage(TextFormatting.GOLD.toString() + TextFormatting.ITALIC.toString() + setting.getName() + ": " +
                TextFormatting.RESET.toString() + setting.getId() + " " + value + TextFormatting.GOLD.toString() + TextFormatting.ITALIC.toString() + " [" + current + "]");
    }
}
