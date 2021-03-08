package com.thef1xer.gateclient.gui.hud;

import com.thef1xer.gateclient.gui.hud.components.CoordsComponent;
import com.thef1xer.gateclient.gui.hud.components.ModulesComponent;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD {
    public boolean noOverlay = false;

    public ModulesComponent modulesComponent = new ModulesComponent();
    public CoordsComponent coordsComponent = new CoordsComponent();

    public void init() {
        modulesComponent.init();
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            modulesComponent.renderComponent();
            coordsComponent.renderComponent();
        }

        if (this.noOverlay) {
            if (event instanceof RenderGameOverlayEvent.Pre) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO || event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
                    event.setCanceled(false);
                    event.setCanceled(false);
                }
            }
            GuiIngameForge.renderObjective = false;
        }
    }
}
