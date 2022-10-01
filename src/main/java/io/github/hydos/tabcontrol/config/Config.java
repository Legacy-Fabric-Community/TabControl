package io.github.hydos.tabcontrol.config;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class Config {
    private boolean enabled;
    private List<String> header;
    private List<String> footer;
    private int waitTicks;

    // For GSON
    public Config() {
        this.enabled = false;
        this.header = Collections.emptyList();
        this.footer = Collections.emptyList();
        this.waitTicks = 100;
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

    public int getWaitTicks() {
        return this.waitTicks;
    }
}
