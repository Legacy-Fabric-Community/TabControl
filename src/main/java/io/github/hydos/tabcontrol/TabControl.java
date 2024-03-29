package io.github.hydos.tabcontrol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hydos.tabcontrol.command.TCReloadCommand;
import io.github.hydos.tabcontrol.config.Config;
import io.github.hydos.tabcontrol.util.NetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import net.legacyfabric.fabric.api.command.CommandSide;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.legacyfabric.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.legacyfabric.fabric.api.registry.CommandRegistry;

@Environment(EnvType.SERVER)
public class TabControl implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("tabcontrol.json");
    private static final Config DEFAULT_CONFIG = new Config();
    private static Config config;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static long lastTimeMillis = -1L;
    private static long waitTicks = 0;

    public void onInitializeServer() {
        LOGGER.info("Starting TabControl");
        try {
            reload();
        } catch (IOException e) {
            throw new RuntimeException("Error loading TabControl config!", e);
        }
        CommandRegistry.INSTANCE.register(new TCReloadCommand(), CommandSide.DEDICATED);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NetworkUtils.sendToSender(sender);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (config.isEnabled()) return;
            long timeMillis = MinecraftServer.getTimeMillis();
            if (!(lastTimeMillis == -1L)) {
                long diff = timeMillis - lastTimeMillis;
                if (diff > 60) {
                    NetworkUtils.tps = Math.round((1000.0/diff) * 100.0) / 100.0;
                }
            }
            lastTimeMillis = timeMillis;
            if (waitTicks == 0) {
                NetworkUtils.sendToPlayers(server);
                waitTicks = config.getWaitTicks();
            } else {
                waitTicks--;
            }
        });
    }

    public static void reload() throws IOException {
        if (!Files.exists(CONFIG_PATH)) {
            Files.createFile(CONFIG_PATH);
            Files.write(CONFIG_PATH, GSON.toJson(DEFAULT_CONFIG).getBytes());
        }
        config = GSON.fromJson(Files.newBufferedReader(CONFIG_PATH), Config.class);
    }

    public static Config getConfig() {
        return config;
    }
}
