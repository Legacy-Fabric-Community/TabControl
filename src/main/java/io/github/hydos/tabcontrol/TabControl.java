package io.github.hydos.tabcontrol;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.tabcontrol.config.Config;
import io.github.hydos.tabcontrol.config.JanksonOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class TabControl implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("tabcontrol.json");
	private static Config config;
	private static final Jankson JANKSON = Jankson.builder().build();

	public void onInitializeServer() {
		LOGGER.info("Starting TabControl");
		try {
			reload();
		} catch (IOException | SyntaxError e) {
			throw new RuntimeException("Error loading TabControl config!", e);
		}
	}

	public static void reload() throws IOException, SyntaxError {
		if (!Files.exists(CONFIG_PATH)) {
			Files.createFile(CONFIG_PATH);
			String x = "{\n" +
					"\t\"enabled\": false \n" +
					"}";
			Files.write(CONFIG_PATH, x.getBytes(StandardCharsets.UTF_8));
		}
		JsonObject object = JANKSON.load(CONFIG_PATH.toFile());
		DataResult<Pair<Config, JsonElement>> configDataResult = Config.CODEC.decode(JanksonOps.INSTANCE, object);
		config = configDataResult.getOrThrow(false, System.err::println).getFirst();
	}

	public static Config getConfig() {
		return config;
	}
}
