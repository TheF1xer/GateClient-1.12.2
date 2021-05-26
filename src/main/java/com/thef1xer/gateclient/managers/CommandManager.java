package com.thef1xer.gateclient.managers;

import com.thef1xer.gateclient.commands.*;
import com.thef1xer.gateclient.commands.impl.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public String prefix = ".";
    public final List<Command> COMMAND_LIST = new ArrayList<>();

    public void init() {
        COMMAND_LIST.add(new BindCommand());
        COMMAND_LIST.add(new HelpCommand());
        COMMAND_LIST.add(new ModuleListCommand());
        COMMAND_LIST.add(new PresetCommand());
        COMMAND_LIST.add(new SayCommand());
        COMMAND_LIST.add(new SetCommand());
        COMMAND_LIST.add(new ToggleCommand());
    }
}
