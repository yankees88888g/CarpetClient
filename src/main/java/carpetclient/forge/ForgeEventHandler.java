package carpetclient.forge;

import net.minecraft.client.Minecraft;
import carpetclient.Config;
import carpetclient.Hotkeys;
import carpetclient.MainRender;
import carpetclient.coders.EDDxample.PistonHelper;
import carpetclient.coders.EDDxample.ShowBoundingBoxes;
import carpetclient.coders.EDDxample.VillageMarker;
import carpetclient.gui.chunkgrid.Controller;
import carpetclient.gui.chunkgrid.GuiChunkGrid;
import carpetclient.random.RandomtickDisplay;
import carpetclient.rules.CarpetRules;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ForgeEventHandler
{
    private boolean gameRunning = false;
    private boolean loggedOut = false;
    private int renderErrorCount;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            this.gameRunning = mc.isIntegratedServerRunning() || mc.getCurrentServerData() != null;

            if (this.gameRunning) {
                Hotkeys.onTick(mc);
                Controller.tick();
                this.loggedOut = true;
            } else if (this.loggedOut) {
                this.loggedOut = false;
                CarpetRules.resetToDefaults();
                Config.resetToDefaults();
                VillageMarker.clearLists(0);
                ShowBoundingBoxes.clear();
                GuiChunkGrid.instance = new GuiChunkGrid();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (this.gameRunning) {
            try {
                float partialTicks = event.getPartialTicks();
                PistonHelper.draw(partialTicks);
                RandomtickDisplay.draw(partialTicks);

                // This was in onPostRenderEntities() in LiteLoader
                MainRender.mainRender(partialTicks);
            } catch (Exception e) {
                if (++this.renderErrorCount < 20) {
                    System.out.println(e);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            // This was in onPostRenderHUD() in LiteLoader
            if (GuiChunkGrid.instance.getMinimapType() != 0) {
                int width = event.getResolution().getScaledWidth();
                int height = event.getResolution().getScaledHeight();
                GuiChunkGrid.instance.renderMinimap(width, height);
            }
        }
    }
}
