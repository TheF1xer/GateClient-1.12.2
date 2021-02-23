package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.commands.*;
import com.thef1xer.gateclient.commands.impl.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public String prefix = ".";
    public List<Command> commandList = new ArrayList<>();

    public void init() {
        commandList.add(new BindCommand());
        commandList.add(new HelpCommand());
        commandList.add(new ModuleListCommand());
        commandList.add(new SayCommand());
        commandList.add(new SetCommand());
        commandList.add(new ToggleCommand());
    }
}
