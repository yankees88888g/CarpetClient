/**
 * This is code provided by skyrising for the use of sending large packet sizes then Minecraft usually allows.
 * Ask skyrising for permition before using this code.
 */

package carpetclient.coders.skyrising;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import io.netty.buffer.Unpooled;

public class PacketSplitter {
    public static final int MAX_TOTAL_PER_PACKET = 32767;
    public static final int MAX_PAYLOAD_PER_PACKET = MAX_TOTAL_PER_PACKET - 5;
    public static final int DEFAULT_MAX_RECEIVE_SIZE = Integer.MAX_VALUE;

    private static final Map<String, ReadingSession> readingSessions = new HashMap<>();

    public static void send(final NetHandlerPlayClient networkHandler, final ResourceLocation channel, final PacketBuffer packet) {
        send(packet, MAX_PAYLOAD_PER_PACKET, buf -> networkHandler.sendPacket(new CPacketCustomPayload(channel.toString(), buf)));
    }

    private static boolean send(PacketBuffer packet, int payloadLimit, Consumer<PacketBuffer> sender) {
        int len = packet.writerIndex();
        packet.resetReaderIndex();
        for (int offset = 0; offset < len; offset += payloadLimit) {
            int thisLen = Math.min(len - offset, payloadLimit);
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer(thisLen));
            buf.resetWriterIndex();
            if (offset == 0) buf.writeVarInt(len);
            buf.writeBytes(packet, thisLen);
            sender.accept(buf);
        }
        packet.release();
        return true;
    }

    public static PacketBuffer receive(String channel, PacketBuffer data) {
        return receive(channel, data, DEFAULT_MAX_RECEIVE_SIZE);
    }

    public static PacketBuffer receive(String channel, PacketBuffer data, int maxLength) {
        return readingSessions.computeIfAbsent(channel, ReadingSession::new).receive(data, maxLength);
    }

    private static class ReadingSession {
        private final String key;
        private int expectedSize = -1;
        private PacketBuffer received;

        private ReadingSession(String key) {
            this.key = key;
        }

        private PacketBuffer receive(PacketBuffer data, int maxLength) {
            if (expectedSize < 0) {
                expectedSize = data.readVarInt();
                if (expectedSize > maxLength) throw new IllegalArgumentException("Payload too large");
                received = new PacketBuffer(Unpooled.buffer(expectedSize));
            }
            received.writeBytes(data.readBytes(data.readableBytes()));
            if (received.writerIndex() >= expectedSize) {
                readingSessions.remove(key);
                return received;
            }
            return null;
        }
    }
}