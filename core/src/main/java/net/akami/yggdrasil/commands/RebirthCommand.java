package net.akami.yggdrasil.commands;

import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class RebirthCommand implements CommandExecutor {

    private final AbstractYggdrasilPlayerManager manager;

    public RebirthCommand(AbstractYggdrasilPlayerManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if(!(src instanceof Player)) {
            return CommandResult.empty();
        }
        Player target = (Player) src;
        manager.createNewPlayer(target.getUniqueId());
        return CommandResult.success();
    }
}
