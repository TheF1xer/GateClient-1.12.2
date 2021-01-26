package com.thef1xer.gateclient.commands;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.modules.Module;
import com.thef1xer.gateclient.settings.*;
import com.thef1xer.gateclient.util.ChatUtil;
import com.thef1xer.gateclient.util.MathUtil;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.core.util.Integers;

public class SetCommand extends Command{
    public Module module;

    public SetCommand() {
        super("set", "Changes the settings of a Module", "set <module> <setting> <value> | <module> list", new String[] {"s"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 3 && args[2].equalsIgnoreCase("list")) {
            if (isModule(args[1])) {
                if (module.getSettings() == null) {
                    ChatUtil.clientMessage("This module has no settings");
                    return;
                }

                ChatUtil.clientMessage("Settings for " + module.getName() + " module:");
                for (Setting setting : module.getStreamedSettings()) {
                    if (setting instanceof BooleanSetting) {
                        sendMessageSetting(setting, "<true / false>");
                    } else if (setting instanceof ColorSetting) {
                        sendMessageSetting(setting, "<red> <green> <blue> (<alpha>)");
                    } else if (setting instanceof FloatSetting) {
                        sendMessageSetting(setting, "<value>");
                    }
                }
            } else {
                ChatUtil.clientMessage("Module not found");
            }
            return;
        } else if (args.length > 3) {
            if (isModule(args[1])) {
                if (!module.getSettings().isEmpty()) {
                    for (Setting setting : module.getStreamedSettings()) {
                        if (setting.getId().equalsIgnoreCase(args[2])) {

                            if (setting instanceof BooleanSetting) {

                                if (args.length == 4) {
                                    if (args[3].equalsIgnoreCase("true")) {
                                        ((BooleanSetting)setting).setValue(true);
                                    } else if (args[3].equalsIgnoreCase("false")) {
                                        ((BooleanSetting)setting).setValue(false);
                                    } else {
                                        ChatUtil.clientMessage("Value must be <true / false>");
                                    }
                                } else {
                                    this.syntaxError();
                                }

                            } else if (setting instanceof ColorSetting) {

                                if (args.length == 6) {
                                    if (MathUtil.isInteger(args[3]) && MathUtil.isInteger(args[4]) && MathUtil.isInteger(args[5])) {
                                        int red = Integers.parseInt(args[3]);
                                        int green = Integers.parseInt(args[4]);
                                        int blue = Integers.parseInt(args[5]);
                                        if (red <= 255 && green <= 255 && blue <= 255 &&
                                                red >= 0 && green >= 0 && blue >= 0) {
                                            ((ColorSetting)setting).setRed(red);
                                            ((ColorSetting)setting).setGreen(green);
                                            ((ColorSetting)setting).setBlue(blue);
                                            ChatUtil.clientMessage(setting.getName() + " set to (" + red + ", " + green + ", " + blue + ")");
                                        } else {
                                            ChatUtil.clientMessage("Colors must be between 0 and 255");
                                        }
                                    }
                                } else if (args.length == 7) {
                                    if (MathUtil.isInteger(args[3]) && MathUtil.isInteger(args[4]) && MathUtil.isInteger(args[5]) && MathUtil.isInteger(args[6])) {
                                        int red = Integers.parseInt(args[3]);
                                        int green = Integers.parseInt(args[4]);
                                        int blue = Integers.parseInt(args[5]);
                                        int alpha = Integers.parseInt(args[6]);
                                        if (red <= 255 && green <= 255 && blue <= 255 && alpha <= 255 &&
                                                red >= 0 && green >= 0 && blue >= 0 && alpha >= 0) {
                                            ((ColorSetting)setting).setRed(red);
                                            ((ColorSetting)setting).setGreen(green);
                                            ((ColorSetting)setting).setBlue(blue);
                                            ((ColorSetting)setting).setAlpha(alpha);
                                            ChatUtil.clientMessage(setting.getName() + " set to (" + red + ", " + green + ", " + blue + ", " + alpha + ")");
                                        } else {
                                            ChatUtil.clientMessage("Colors must be between 0 and 255");
                                        }
                                    }
                                } else {
                                    this.syntaxError();
                                }

                            } else if (setting instanceof FloatSetting) {
                                if (args.length == 4) {
                                    try {
                                        float value = Float.parseFloat(args[3]);
                                        if (value > ((FloatSetting) setting).getMin() && value < ((FloatSetting) setting).getMax()) {
                                            ((FloatSetting) setting).setValue(value);
                                            ChatUtil.clientMessage(setting.getName() + " set to " + value);
                                        } else {
                                            ChatUtil.clientMessage("The value must be between " + ((FloatSetting) setting).getMin() + " and " + ((FloatSetting) setting).getMax());
                                        }
                                    } catch (NumberFormatException e) {
                                        ChatUtil.clientMessage("The number introduced is not valid");
                                    }
                                } else {
                                    this.syntaxError();
                                }
                            }
                            return;
                        }
                    }
                }
                ChatUtil.clientMessage("Setting not found");

            } else {
                ChatUtil.clientMessage("Module not found");
            }
            return;
        }

        this.syntaxError();
    }

    public boolean isModule(String name) {
        for (Module module : GateClient.moduleManager.moduleList) {
            if (module.getId().equalsIgnoreCase(name)) {
                this.module = module;
                return true;
            }
        }
        return false;
    }

    public void sendMessageSetting(Setting setting, String value) {
        ChatUtil.userMessage(TextFormatting.AQUA.toString() + TextFormatting.ITALIC.toString() + setting.getName() + ": " + TextFormatting.RESET.toString() + setting.getId() + " " + value);
    }
}
