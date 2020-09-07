package io.github.hydos.tabcontrol.command;

import java.io.IOException;

import blue.endless.jankson.impl.SyntaxError;
import io.github.hydos.tabcontrol.TabControl;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class TCReloadCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "tcreload";
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "command.tcreload.usage";
    }

    @Override
    public void execute(CommandSource source, String[] args) throws CommandException {
        try {
            TabControl.reload();
        } catch (IOException | SyntaxError e) {
            e.printStackTrace();
            throw new CommandException("command.tcreload.error");
        }
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }
}
