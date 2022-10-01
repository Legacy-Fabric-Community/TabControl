package io.github.hydos.tabcontrol.util;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.function.Supplier;

import com.google.common.collect.Iterators;
import io.github.hydos.tabcontrol.TabControl;
import io.github.hydos.tabcontrol.mixin.PlayerListHeaderS2CPacketAccessor;

import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.networking.v1.PacketSender;

public final class NetworkUtils {
    public static double tps = 20;

    public static final HashMap<String, Supplier<String>> PLACEHOLDERS = new HashMap<>();

    static {
        PLACEHOLDERS.put("ip", () -> MinecraftServer.getServer().getServerIp());
        PLACEHOLDERS.put("gameVersion", () -> String.valueOf(MinecraftServer.getServer().getServerMetadata().getVersion().getGameVersion()));
        PLACEHOLDERS.put("playerCount", () -> String.valueOf(MinecraftServer.getServer().getCurrentPlayerCount()));
        PLACEHOLDERS.put("motd", () -> MinecraftServer.getServer().getMotd());
        PLACEHOLDERS.put("modname", () -> MinecraftServer.getServer().getServerModName());
        PLACEHOLDERS.put("tps", () -> String.valueOf(tps));
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

    private static String format(String text) {
        StringTokenizer tokens = new StringTokenizer(text, "$}", false);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            System.out.println(token);
        }
        return text;
    }
}
