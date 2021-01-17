package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.commands.Command;
import com.thef1xer.gateclient.commands.ToggleCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public String prefix = ".";
    public List<Command> commandList = new ArrayList<>();

    public void init() {
        commandList.add(new ToggleCommand());
    }
}
