package me.thef1xer.gateclient;

import me.thef1xer.gateclient.managers.HUDManager;
import me.thef1xer.gateclient.managers.*;
import me.thef1xer.gateclient.util.Reference;
import me.thef1xer.gateclient.handlers.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class GateClient {
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ConfigManager configManager;
    public PresetManager presetManager;
    public GuiManager guiManager;
    public HUDManager hudManager;

    public EventHandler eventHandler;

    @Mod.Instance
    private static GateClient gate;

    public static GateClient getGate() {
        return GateClient.gate;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        presetManager = new PresetManager();
        guiManager = new GuiManager();
        hudManager = new HUDManager();

        eventHandler = new EventHandler();

        MinecraftForge.EVENT_BUS.register(eventHandler);
        MinecraftForge.EVENT_BUS.register(hudManager);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        moduleManager.init();
        commandManager.init();
        guiManager.init();
        configManager.init();
        presetManager.init();
        hudManager.init();
    }
}
