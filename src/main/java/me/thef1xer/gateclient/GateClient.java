package me.thef1xer.gateclient;

import me.thef1xer.gateclient.handlers.ModuleEventHandler;
import me.thef1xer.gateclient.managers.*;
import me.thef1xer.gateclient.util.Reference;
import me.thef1xer.gateclient.handlers.CommonEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class GateClient {
    public CommandManager COMMAND_MANAGER;
    public ConfigManager CONFIG_MANAGER;
    public FriendsManager FRIENDS_MANAGER;
    public GuiManager GUI_MANAGER;
    public ModuleManager MODULE_MANAGER;
    public PresetManager PRESET_MANAGER;

    public CommonEventHandler commonEventHandler;
    public ModuleEventHandler moduleEventHandler;

    @Mod.Instance
    private static GateClient gate;

    public static GateClient getGate() {
        return GateClient.gate;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        COMMAND_MANAGER = new CommandManager();
        CONFIG_MANAGER = new ConfigManager();
        FRIENDS_MANAGER = new FriendsManager();
        GUI_MANAGER = new GuiManager();
        MODULE_MANAGER = new ModuleManager();
        PRESET_MANAGER = new PresetManager();

        commonEventHandler = new CommonEventHandler();
        moduleEventHandler = new ModuleEventHandler();

        MinecraftForge.EVENT_BUS.register(commonEventHandler);
        MinecraftForge.EVENT_BUS.register(moduleEventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MODULE_MANAGER.init();
        COMMAND_MANAGER.init();
        GUI_MANAGER.init();
        CONFIG_MANAGER.init();
        PRESET_MANAGER.init();
        FRIENDS_MANAGER.init();
    }
}
