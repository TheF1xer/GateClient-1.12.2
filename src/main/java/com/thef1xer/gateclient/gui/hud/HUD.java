package com.thef1xer.gateclient.gui.hud;

import com.thef1xer.gateclient.gui.hud.components.CoordsComponent;
import com.thef1xer.gateclient.gui.hud.components.ModulesComponent;
import com.thef1xer.gateclient.modules.render.HUDModule;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD {
    //TODO: Add rainbow effect

    public ModulesComponent modulesComponent = new ModulesComponent();
    public CoordsComponent coordsComponent = new CoordsComponent();

    public void init() {
        modulesComponent.init();
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (HUDModule.INSTANCE.isEnabled()) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                modulesComponent.renderComponent();
                coordsComponent.renderComponent();
            }
        }
    }
}
