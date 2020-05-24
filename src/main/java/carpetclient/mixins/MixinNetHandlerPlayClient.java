package carpetclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import carpetclient.pluginchannel.CarpetPluginChannel;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = "handleCustomPayload", at = @At("RETURN"))
    private void onCustomPayload(SPacketCustomPayload packet, CallbackInfo ci) {
        String channel = packet.getChannelName();

        if (CarpetPluginChannel.CARPET_CHANNEL_NAME.equals(channel)) {
            PacketBuffer data = packet.getBufferData();
            CarpetPluginChannel.packatReceiver(channel, data);
        }
    }
}
