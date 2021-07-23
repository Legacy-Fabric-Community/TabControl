package io.github.hydos.tabcontrol.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Config {
    private final boolean enabled;
    private final List<String> header;
    private final List<String> footer;
    private final boolean updateEveryTick;

    public static JsonObject toJson(Config config) {
        JsonObject object = new JsonObject();
        object.addProperty("enabled", config.enabled);
        JsonArray header = new JsonArray();
        config.header.stream().map(JsonPrimitive::new).forEach(header::add);
        JsonArray footer = new JsonArray();
        config.footer.stream().map(JsonPrimitive::new).forEach(footer::add);
        object.add("header", header);
        object.add("footer", footer);
        object.addProperty("updateEveryTick", config.updateEveryTick);
        return object;
    }

    public static Config fromJson(JsonObject json) {
        boolean enabled = json.get("enabled").getAsBoolean();
        List<String> header = StreamSupport.stream(json.get("header").getAsJsonArray().spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
        List<String> footer = StreamSupport.stream(json.get("footer").getAsJsonArray().spliterator(), false).map(JsonElement::getAsString).collect(Collectors.toList());
        boolean updateEveryTick = json.get("updateEveryTick").getAsBoolean();
        return new Config(enabled, header, footer, updateEveryTick);
    }

    private Config(boolean enabled, List<String> header, List<String> footer, boolean updateEveryTick) {
        this.enabled = enabled;
        this.header = header;
        this.footer = footer;
        this.updateEveryTick = updateEveryTick;
    }

    public List<String> getFooter() {
        return this.footer;
    }

    public List<String> getHeader() {
        return this.header;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean shouldUpdateEveryTick() {
        return this.updateEveryTick;
    }
}
