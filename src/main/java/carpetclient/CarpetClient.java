package carpetclient;

import carpetclient.forge.ForgeEventHandler;
import carpetclient.gui.chunkgrid.GuiChunkGrid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = "Carpet Client", version = Reference.MOD_VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:forge@[14.23.3,);")
public class CarpetClient
{
    @Mod.Instance(Reference.MOD_ID)
    public static CarpetClient instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ClientRegistry.registerKeyBinding(Hotkeys.chunkDebug);
        ClientRegistry.registerKeyBinding(Hotkeys.randomtickChunkUpdates);
        ClientRegistry.registerKeyBinding(Hotkeys.toggleBlockFacing);
        ClientRegistry.registerKeyBinding(Hotkeys.toggleBlockFlip);
        ClientRegistry.registerKeyBinding(Hotkeys.toggleBoundingBoxMarkers);
        ClientRegistry.registerKeyBinding(Hotkeys.toggleSnapAim);
        ClientRegistry.registerKeyBinding(Hotkeys.toggleSnapAimKeyLocker);
        ClientRegistry.registerKeyBinding(Hotkeys.toggleVillageMarkers);

        Config.load();
        GuiChunkGrid.instance = new GuiChunkGrid();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
    }
}
