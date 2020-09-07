package io.github.hydos.tabcontrol.config;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

@SuppressWarnings({"Convert2MethodRef", "CodeBlock2Expr"})
public class Config {
    public static final Codec<Config> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.BOOL.fieldOf("enabled").forGetter((config) -> {
            return config.isEnabled();
        }), Codec.list(Codec.STRING).optionalFieldOf("header", ImmutableList.of()).forGetter((config) -> {
            return config.getHeader();
        }), Codec.list(Codec.STRING).optionalFieldOf("footer", ImmutableList.of()).forGetter((config) -> {
            return config.getFooter();
        }), Codec.BOOL.optionalFieldOf("updateEveryTick", false).forGetter((config) -> {
            return config.shouldUpdateEveryTick();
        })).apply(instance, Config::new);
    });

    private final boolean enabled;
    private final List<String> header;
    private final List<String> footer;
    private final boolean updateEveryTick;

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
