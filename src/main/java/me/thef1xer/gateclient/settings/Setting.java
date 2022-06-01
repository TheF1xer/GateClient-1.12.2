package me.thef1xer.gateclient.settings;

public class Setting {
    private final String name;
    private final String id;

    public Setting(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCommandSyntax() {
        return "";
    }
}
