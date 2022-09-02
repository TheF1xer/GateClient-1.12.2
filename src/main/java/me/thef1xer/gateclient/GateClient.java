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
    public CommandManager commandManager;
    public ConfigManager configManager;
    public FriendsManager friendsManager;
    public GuiManager guiManager;
    public ModuleManager moduleManager;
    public PresetManager presetManager;
    public ThreadManager threadManager;

    public CommonEventHandler commonEventHandler;
    public ModuleEventHandler moduleEventHandler;

    @Mod.Instance
    private static GateClient gate;

    public static GateClient getGate() {
        return GateClient.gate;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        friendsManager = new FriendsManager();
        guiManager = new GuiManager();
        moduleManager = new ModuleManager();
        presetManager = new PresetManager();
        threadManager = new ThreadManager();

        commonEventHandler = new CommonEventHandler();
        moduleEventHandler = new ModuleEventHandler();

        MinecraftForge.EVENT_BUS.register(commonEventHandler);
        MinecraftForge.EVENT_BUS.register(moduleEventHandler);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        moduleManager.init();
        commandManager.init();
        guiManager.init();
        configManager.init();
        presetManager.init();
        friendsManager.init();
        threadManager.init();
    }
}
