package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.commands.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public String prefix = ".";
    public List<Command> commandList = new ArrayList<>();

    public void init() {
        commandList.add(new BindCommand());
        commandList.add(new HelpCommand());
        commandList.add(new SayCommand());
        commandList.add(new SetCommand());
        commandList.add(new ToggleCommand());
    }
}
