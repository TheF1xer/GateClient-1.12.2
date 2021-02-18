package com.thef1xer.gateclient.commands;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

public abstract class Command {
    private final String name;
    private final String desc;
    private final String syntax;
    private final String[] aliases;

    public Command(String name, String desc, String syntax, String[] aliases) {
        this.name = name;
        this.desc = desc;
        this.syntax = syntax;
        this.aliases = aliases;
    }

    public void syntaxError() {
        ChatUtil.clientMessage("Incorrect Syntax: " + TextFormatting.YELLOW + GateClient.gate.commandManager.prefix + this.getSyntax());
    }

    public abstract void onCommand(String[] args);

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSyntax() {
        return syntax;
    }

    public String[] getAliases() {
        return aliases;
    }
}
