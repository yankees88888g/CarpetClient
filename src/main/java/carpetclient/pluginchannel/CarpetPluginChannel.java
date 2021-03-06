package carpetclient.pluginchannel;


import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import carpetclient.bugfix.PistonFix;
import carpetclient.coders.EDDxample.ShowBoundingBoxes;
import carpetclient.coders.EDDxample.VillageMarker;
import carpetclient.coders.skyrising.PacketSplitter;
import carpetclient.coders.zerox53ee71ebe11e.Chunkdata;
import carpetclient.random.RandomtickDisplay;
import carpetclient.rules.CarpetRules;
import carpetclient.rules.TickRate;
import carpetclient.util.CustomCrafting;

/*
Plugin channel class to implement a client server communication between carpet client and carpet server.
 */
public class CarpetPluginChannel {
    public static final String CARPET_CHANNEL_NAME = "carpet:client";
    public static final ImmutableList<String> CARPET_PLUGIN_CHANNEL = ImmutableList.of(CARPET_CHANNEL_NAME);

    public static final int GUI_ALL_DATA = 0;
    public static final int RULE_REQUEST = 1;
    public static final int VILLAGE_MARKERS = 2;
    public static final int BOUNDINGBOX_MARKERS = 3;
    public static final int TICKRATE_CHANGES = 4;
    public static final int CHUNK_LOGGER = 5;
    public static final int PISTON_UPDATES = 6;
    public static final int RANDOMTICK_DISPLAY = 7;
    public static final int CUSTOM_RECIPES = 8;

    /**
     * Packate receiver method to handle incoming messages.
     *
     * @param channel incoming channel or packet name.
     * @param data    incoming data from server.
     */
    public static void packatReceiver(String channel, PacketBuffer data) {
        data.readerIndex(0);
        PacketBuffer buffer = PacketSplitter.receive(CARPET_CHANNEL_NAME, data);

        // Received the complete packet
        if (buffer != null) {
            handleData(buffer);
        }

        data.readerIndex(0);
    }

    /**
     * Handler for the incoming pakets from the server.
     *
     * @param data Data that is recieved from the server.
     */
    private static void handleData(PacketBuffer data) {
        int type = data.readInt();

        if (GUI_ALL_DATA == type) {
            CarpetRules.setAllRules(data);
        }
        if (RULE_REQUEST == type) {
            CarpetRules.ruleData(data);
        }
        if (VILLAGE_MARKERS == type) {
            VillageMarker.villageUpdate(data);
        }
        if (BOUNDINGBOX_MARKERS == type) {
            ShowBoundingBoxes.getStructureComponent(data);
        }
        if (TICKRATE_CHANGES == type) {
            TickRate.setTickRate(data);
        }
        if (CHUNK_LOGGER == type) {
            Chunkdata.processPacket(data);
        }
        if (PISTON_UPDATES == type) {
            PistonFix.processPacket(data);
        }
        if (RANDOMTICK_DISPLAY == type) {
            RandomtickDisplay.processPacket(data);
        }
        if (CUSTOM_RECIPES == type) {
            CustomCrafting.addCustomRecipes(data);
        }
    }

    /**
     * Packet sending method to send data to the server.
     *
     * @param data The data that is being sent to the server.
     */
    public static void packatSender(PacketBuffer data) {
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getConnection();
        if (netHandler != null) {
            PacketSplitter.send(netHandler, new ResourceLocation(CARPET_CHANNEL_NAME), data);
        }
    }
}
