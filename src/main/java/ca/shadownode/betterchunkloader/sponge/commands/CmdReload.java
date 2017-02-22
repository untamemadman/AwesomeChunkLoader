package ca.shadownode.betterchunkloader.sponge.commands;

import ca.shadownode.betterchunkloader.sponge.BetterChunkLoader;
import ca.shadownode.betterchunkloader.sponge.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;


public class CmdReload implements CommandExecutor {

    private final BetterChunkLoader plugin;
    
    public CmdReload(BetterChunkLoader plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {

        if (!commandSource.hasPermission("betterchunkloader.reload")) {
            commandSource.sendMessage(Utilities.parseMessage(plugin.getConfig().msgPrefix + plugin.getConfig().cmdNoPermission));
            return CommandResult.empty();
        }

        plugin.getConfig().loadConfig();
        plugin.getDataStoreManager().load();
        
        commandSource.sendMessage(Utilities.parseMessage(plugin.getConfig().msgPrefix + plugin.getConfig().cmdReloadSuccess));

        return CommandResult.success();
    }
}
