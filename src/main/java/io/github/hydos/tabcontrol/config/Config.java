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
       })).apply(instance, Config::new);
    });

    private final boolean enabled;
    private final List<String> header;
    private final List<String> footer;

    private Config(boolean enabled, List<String> header, List<String> footer) {
        this.enabled = enabled;
        this.header = header;
        this.footer = footer;
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
}
