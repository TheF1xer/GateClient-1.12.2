package com.thef1xer.gateclient.util;

import com.thef1xer.gateclient.commands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtil {
    public static void userMessage(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

    public static void clientMessage(String message) {
        message = TextFormatting.BLUE + "[" + Reference.NAME + "] " + TextFormatting.RESET + message;
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

    public static boolean isCommand(String name, Command command) {
        if (name.equalsIgnoreCase(command.getName())) {
            return true;
        }

        for (String alias : command.getAliases()) {
            if (name.equalsIgnoreCase(alias)) {
                return true;
            }
        }

        return false;
    }
}
