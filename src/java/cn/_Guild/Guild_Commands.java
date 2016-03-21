package cn._Guild;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by key_q on 2016/3/22.
 */
public class Guild_Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            Guild_Setup.log.info("Only players are supported for this Example Plugin, but you should not do this!!!");
            return true;
        }

        Player player = (Player) commandSender;

        if(command.getLabel().equals("test-economy")) {
            // Lets give the player 1.05 currency (note that SOME economic plugins require rounding!)
            commandSender.sendMessage(String.format("You have %s", Guild_Setup.econ.format(Guild_Setup.econ.getBalance(player))));
            EconomyResponse r = Guild_Setup.econ.depositPlayer(player, 1.05);
            if(r.transactionSuccess()) {
                commandSender.sendMessage(String.format("You were given %s and now have %s", Guild_Setup.econ.format(r.amount), Guild_Setup.econ.format(r.balance)));
            } else {
                commandSender.sendMessage(String.format("An error occured: %s", r.errorMessage));
            }
            return true;
        } else if(command.getLabel().equals("test-permission")) {
            // Lets test if user has the node "example.plugin.awesome" to determine if they are awesome or just suck
            if(Guild_Setup.perms.has(player, "example.plugin.awesome")) {
                commandSender.sendMessage("You are awesome!");
            } else {
                commandSender.sendMessage("You suck!");
            }
            return true;
        } else {
            return false;
        }


    }

}
