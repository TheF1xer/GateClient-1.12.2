package me.thef1xer.gateclient.handlers;

import me.thef1xer.gateclient.GateClient;
import me.thef1xer.gateclient.events.*;
import me.thef1xer.gateclient.modules.combat.*;
import me.thef1xer.gateclient.modules.hud.ArmorHUD;
import me.thef1xer.gateclient.modules.hud.Coords;
import me.thef1xer.gateclient.modules.hud.ModuleList;
import me.thef1xer.gateclient.modules.hud.Watermark;
import me.thef1xer.gateclient.modules.movement.*;
import me.thef1xer.gateclient.modules.player.*;
import me.thef1xer.gateclient.modules.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleEventHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        // Start of the tick
        if (event.phase == TickEvent.Phase.START) {
            if (AutoTotem.INSTANCE.isEnabled()) {
                AutoTotem.INSTANCE.onClientTick();
            }

            if (Flight.INSTANCE.isEnabled()) {
                Flight.INSTANCE.onClientTick();
            }

            if (Sprint.INSTANCE.isEnabled()) {
                Sprint.INSTANCE.onClientTick();
            }

            if (AutoArmor.INSTANCE.isEnabled()) {
                AutoArmor.INSTANCE.onClientTick();
            }

            if (AutoDisconnect.INSTANCE.isEnabled()) {
                AutoDisconnect.INSTANCE.onClientTick();
            }

            if (Freecam.INSTANCE.isEnabled()) {
                Freecam.INSTANCE.onClientTick();
            }

            if (FullBright.INSTANCE.isEnabled()) {
                FullBright.INSTANCE.onClientTick();
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(SendPacketEvent event) {
        if (Criticals.INSTANCE.isEnabled()) {
            Criticals.INSTANCE.onSendPacket(event);
        }

        if (NoFall.INSTANCE.isEnabled()) {
            NoFall.INSTANCE.onSendPacket(event);
        }

        if (AntiHunger.INSTANCE.isEnabled()) {
            AntiHunger.INSTANCE.onSendPacket(event);
        }

        if (Freecam.INSTANCE.isEnabled()) {
            Freecam.INSTANCE.onSendPacket(event);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(ReceivePacketEvent event) {
        if (Velocity.INSTANCE.isEnabled()) {
            Velocity.INSTANCE.onReceivePacket(event);
        }
    }

    @SubscribeEvent
    public void updateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (CrystalAura.INSTANCE.isEnabled()) {
            CrystalAura.INSTANCE.onUpdateWalkingPlayer(event);
        }

        if (KillAura.INSTANCE.isEnabled()) {
            KillAura.INSTANCE.onUpdateWalkingPlayer(event);
        }

        if (Surround.INSTANCE.isEnabled()) {
            Surround.INSTANCE.onUpdateWalkingPlayer(event);
        }

        if (Flight.INSTANCE.isEnabled()) {
            Flight.INSTANCE.onUpdateWalkingPlayer();
        }

        if (Jesus.INSTANCE.isEnabled()) {
            Jesus.INSTANCE.onUpdateWalkingPlayer(event);
        }

        if (Scaffold.INSTANCE.isEnabled()) {
            Scaffold.INSTANCE.onUpdateWalkingPlayer(event);
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (KillAura.INSTANCE.isEnabled()) {
            KillAura.INSTANCE.onAttackEntity(event);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            if (ModuleList.INSTANCE.isEnabled()) {
                ModuleList.INSTANCE.onRenderGameOverlay(event.getResolution());
            }

            if (Coords.INSTANCE.isEnabled()) {
                Coords.INSTANCE.onRenderGameOverlay(event.getResolution());
            }

            if (Watermark.INSTANCE.isEnabled()) {
                Watermark.INSTANCE.onRenderGameOverlay();
            }

        } else if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if (ArmorHUD.INSTANCE.isEnabled()) {
                ArmorHUD.INSTANCE.onRenderOverlay(event.getResolution());
            }
        }

        if (NoOverlay.INSTANCE.isEnabled()) {
            if (event instanceof RenderGameOverlayEvent.Pre) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO || event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
                    event.setCanceled(false);
                    event.setCanceled(false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        if (GuiMove.INSTANCE.isEnabled()) {
            GuiMove.INSTANCE.onInputUpdate();
        }

        if (NoSlow.INSTANCE.isEnabled()) {
            NoSlow.INSTANCE.onInputUpdate();
        }

        if (Freecam.INSTANCE.isEnabled()) {
            Freecam.INSTANCE.onInputUpdate(event);
        }
    }

    @SubscribeEvent
    public void onPlayerMove(PlayerMoveEvent event) {
        // Order is important

        // This must be the first movement change made
        if (Speed.INSTANCE.isEnabled()) {
            Speed.INSTANCE.onPlayerMove(event);
        }

        if (Jesus.INSTANCE.isEnabled()) {
            Jesus.INSTANCE.onPlayerMove(event);
        }

        // This must come before the last
        if (SafeWalk.INSTANCE.isEnabled()) {
            SafeWalk.INSTANCE.onPlayerMove(event);
        }

        // This must be the last
        if (Flight.INSTANCE.isEnabled()) {
            Flight.INSTANCE.onPlayerMove(event);
        }
    }

    @SubscribeEvent
    public void onGetLiquidCollisionBoundingBox(GetLiquidCollisionBoundingBoxEvent event) {
        if (Jesus.INSTANCE.isEnabled()) {
            Jesus.INSTANCE.onGetLiquidCollisionBoundingBox(event);
        }
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        if (FakePlayer.INSTANCE.isEnabled()) {
            FakePlayer.INSTANCE.onUnloadWorld();
        }
    }

    @SubscribeEvent
    public void onSetBlockState(SetBlockStateEvent event) {
        if (Search.INSTANCE.isEnabled()) {
            Search.INSTANCE.onSetBlockState(event);
        }

        if (HoleESP.INSTANCE.isEnabled()) {
            HoleESP.INSTANCE.onSetBlockState(event);
        }
    }

    @SubscribeEvent
    public void onLoadChunk(ChunkEvent.Load event) {
        GateClient.getGate().threadManager.chunksToCheck.add(event.getChunk());
    }

    @SubscribeEvent
    public void onUnLoadChunk(ChunkEvent.Unload event) {
        GateClient.getGate().threadManager.chunksToCheck.add(event.getChunk());
    }

    @SubscribeEvent
    public void onCheckChunk(CheckChunkEvent event) {
        if (Search.INSTANCE.isEnabled()) {
            Search.INSTANCE.onCheckChunk(event);
        }

        if (HoleESP.INSTANCE.isEnabled()) {
            HoleESP.INSTANCE.onCheckChunk(event);
        }
    }

    @SubscribeEvent
    public void onSearchChunks(SearchChunksEvent event) {
        // Cancel event to allow thread to search through chunks that need to be searched

        if (Search.INSTANCE.isEnabled() || HoleESP.INSTANCE.isEnabled()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCheckBlockInChunk(CheckBlockInChunkEvent event) {
        if (Minecraft.getMinecraft().world == null) return;

        if (Search.INSTANCE.isEnabled()) {
            Search.INSTANCE.onCheckBlockInChunkEvent(event);
        }

        if (HoleESP.INSTANCE.isEnabled()) {
            HoleESP.INSTANCE.onCheckBlockInChunkEvent(event);
        }
    }

    @SubscribeEvent
    public void onPlayerIsUser(PlayerIsUserEvent event) {
        if (Freecam.INSTANCE.isEnabled()) {
            Freecam.INSTANCE.onPlayerIsUser(event);
        }
    }

    @SubscribeEvent
    public void onSetOpaqueCube(SetOpaqueCubeEvent event) {
        if (Freecam.INSTANCE.isEnabled()) {
            Freecam.INSTANCE.onSetOpaqueCube(event);
        }

        if (XRay.INSTANCE.isEnabled()) {
            XRay.INSTANCE.onSetOpaqueCube(event);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.enableRescaleNormal();

        if (Search.INSTANCE.isEnabled()) {
            Search.INSTANCE.onRenderWorldLast(event);
        }

        if (HoleESP.INSTANCE.isEnabled()) {
            HoleESP.INSTANCE.onRenderWorldLast();
        }

        if (StorageESP.INSTANCE.isEnabled()) {
            StorageESP.INSTANCE.onRenderWorldLast();
        }

        if (Tracers.INSTANCE.isEnabled()) {
            Tracers.INSTANCE.onRenderWorldLast(event);
        }

        if (EntityESP.INSTANCE.isEnabled()) {
            EntityESP.INSTANCE.onRenderWorldLast();
        }

        if (Nametags.INSTANCE.isEnabled()) {
            Nametags.INSTANCE.onRenderWorldLast();
        }

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onRenderName(RenderLivingEvent.Specials.Pre<?> event) {
        if (Nametags.INSTANCE.isEnabled()) {
            Nametags.INSTANCE.onRenderName(event);
        }
    }

    @SubscribeEvent
    public void onRenderToolTip(RenderToolTipEvent event) {
        if (ShulkerViewer.INSTANCE.isEnabled()) {
            ShulkerViewer.INSTANCE.onRenderToolTip(event);
        }
    }

    @SubscribeEvent
    public void onRenderBlock(RenderBlockEvent event) {
        if (XRay.INSTANCE.isEnabled()) {
            XRay.INSTANCE.onRenderBlock(event);
        }
    }

    @SubscribeEvent
    public void onShouldSideBeRendered(ShouldSideBeRenderedEvent event) {
        if (XRay.INSTANCE.isEnabled()) {
            XRay.INSTANCE.onShouldSideBeRendered(event);
        }
    }

    @SubscribeEvent
    public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
        if (XRay.INSTANCE.isEnabled()) {
            XRay.INSTANCE.onGetAmbientOcclusionLightValue(event);
        }
    }
}
