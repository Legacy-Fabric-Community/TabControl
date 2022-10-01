package io.github.hydos.tabcontrol.util;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.function.Supplier;

import io.github.hydos.tabcontrol.TabControl;
import io.github.hydos.tabcontrol.mixin.PlayerListHeaderS2CPacketAccessor;

import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;

import net.legacyfabric.fabric.api.networking.v1.PacketSender;

public final class NetworkUtils {
    public static double tps = 20;

    public static final HashMap<String, Supplier<String>> SERVER_PLACEHOLDERS = new HashMap<>();

    static {
        SERVER_PLACEHOLDERS.put("ip", () -> MinecraftServer.getServer().getServerIp());
        SERVER_PLACEHOLDERS.put("gameVersion", () -> String.valueOf(MinecraftServer.getServer().getServerMetadata().getVersion().getGameVersion()));
        SERVER_PLACEHOLDERS.put("playerCount", () -> String.valueOf(MinecraftServer.getServer().getCurrentPlayerCount()));
        SERVER_PLACEHOLDERS.put("motd", () -> MinecraftServer.getServer().getMotd());
        SERVER_PLACEHOLDERS.put("modname", () -> MinecraftServer.getServer().getServerModName());
        SERVER_PLACEHOLDERS.put("tps", () -> String.valueOf(tps));
    }

    private NetworkUtils() {
    }

    public static void sendToSender(PacketSender sender) {
        sender.sendPacket(transform(new PlayerListHeaderS2CPacket()));
    }

    public static void sendToPlayers(MinecraftServer server) {
        server.getPlayerManager().sendToAll(transform(new PlayerListHeaderS2CPacket()));
    }

    private static PlayerListHeaderS2CPacket transform(PlayerListHeaderS2CPacket packet) {
        ((PlayerListHeaderS2CPacketAccessor) packet).setHeader(new LiteralText(format(String.join("\n", TabControl.getConfig().getHeader()))));
        ((PlayerListHeaderS2CPacketAccessor) packet).setFooter(new LiteralText(format(String.join("\n", TabControl.getConfig().getFooter()))));
        return packet;
    }

    public static String format(String text) {
        StringTokenizer tokens = new StringTokenizer(text, "$}", false);
        StringBuilder builder = new StringBuilder();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.startsWith("{")) {
                String key = token.substring(1);
                StringTokenizer subTokens = new StringTokenizer(key, ".", false);
                String subKey = subTokens.nextToken();
                if (subKey.equals("server")) {
                    String placeHolder = subTokens.nextToken();
                    if (SERVER_PLACEHOLDERS.containsKey(placeHolder)) {
                        builder.append(SERVER_PLACEHOLDERS.get(placeHolder).get());
                    } else {
                        builder.append("%INVALIDTOKEN%");
                    }
                } else {
                    builder.append("%INVALIDTOKEN%");
                }
            } else {
                builder.append(token);
            }
        }
        return builder.toString();
    }
}
