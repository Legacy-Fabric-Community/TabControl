package io.github.hydos.tabcontrol.util;

import io.github.hydos.tabcontrol.TabControl;
import io.github.hydos.tabcontrol.mixin.PlayerListHeaderS2CPacketAccessor;

import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.legacyfabric.fabric.api.networking.v1.PacketSender;

@Environment(EnvType.SERVER)
public final class NetworkUtils {
    public static double tps = 20;

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
        text = text.replaceAll("\\$\\{var.ip}", MinecraftServer.getServer().getServerIp());
        text = text.replaceAll("\\$\\{var.gameVersion}", String.valueOf(MinecraftServer.getServer().getServerMetadata().getVersion().getGameVersion()));
        text = text.replaceAll("\\$\\{var.playerCount}", String.valueOf(MinecraftServer.getServer().getCurrentPlayerCount()));
        text = text.replaceAll("\\$\\{var.motd}", MinecraftServer.getServer().getMotd());
        text = text.replaceAll("\\$\\{var.modname}", MinecraftServer.getServer().getServerModName());
        text = text.replaceAll("\\$\\{var.tps}", String.valueOf(tps));
        return text;
    }
}
