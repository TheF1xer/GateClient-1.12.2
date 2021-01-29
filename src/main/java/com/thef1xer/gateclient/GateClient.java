package com.thef1xer.gateclient;

import com.thef1xer.gateclient.gui.hud.HUD;
import com.thef1xer.gateclient.managers.CommandManager;
import com.thef1xer.gateclient.managers.ConfigManager;
import com.thef1xer.gateclient.managers.ModuleManager;
import com.thef1xer.gateclient.managers.PresetManager;
import com.thef1xer.gateclient.util.Reference;
import com.thef1xer.gateclient.util.handlers.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class GateClient {

    public EventHandler eventHandler = new EventHandler();
    public ModuleManager moduleManager = new ModuleManager();
    public CommandManager commandManager = new CommandManager();
    public ConfigManager configManager = new ConfigManager();
    public PresetManager presetManager = new PresetManager();
    public HUD hud = new HUD();

    @Mod.Instance
    public static GateClient gateClient;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager.init();
        commandManager.init();
        hud.init();
        configManager.init();

        MinecraftForge.EVENT_BUS.register(eventHandler);
        MinecraftForge.EVENT_BUS.register(hud);
    }
}
