package com.thef1xer.gateclient.commands;

import net.minecraft.client.Minecraft;

public class SayCommand extends Command{
    public SayCommand() {
        super("Say", "Sends a message (that can include the prefix)", "say <message>", new String[] {"s"});
    }

    @Override
    public void onCommand(String[] args) {
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);
            if (i != args.length - 1) {
                message.append(" ");
            }
        }
        Minecraft.getMinecraft().player.sendChatMessage(message.toString());
    }
}
