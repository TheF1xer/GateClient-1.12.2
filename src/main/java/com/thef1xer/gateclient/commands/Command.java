package com.thef1xer.gateclient.commands;

import com.thef1xer.gateclient.GateClient;
import com.thef1xer.gateclient.util.ChatUtil;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
    private final String name;
    private final String desc;
    private final String[] syntax;
    private final List<String> aliases = new ArrayList<>();

    public Command(String name, String desc, String... syntax) {
        this.name = name;
        this.desc = desc;
        this.syntax = syntax;
    }

    public void syntaxError() {
        ChatUtil.clientMessage("Incorrect Syntax: ");
        for (String syntax : this.getSyntax()) {
            ChatUtil.clientMessage(TextFormatting.YELLOW + GateClient.gate.commandManager.prefix + syntax);
        }
    }

    public void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public abstract void onCommand(String[] args);

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getSyntax() {
        return syntax;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}
