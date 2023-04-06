package me.tomisanhues2.pmines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.tomisanhues2.pmines.PrivateMines;
import org.bukkit.entity.Player;

@CommandAlias("pm|pmines")
public class PlayerCommands extends BaseCommand {

    private final PrivateMines plugin = PrivateMines.getInstance();
    @Subcommand("tp")
    public void tp(Player sender) {
        if (plugin.mineManager.getMine(sender.getUniqueId()) == null) {
            sender.sendMessage("§6§lPrivateMines §8» §7You do not have a mine!");
            return;
        }
        sender.teleport(plugin.mineManager.getMine(sender.getUniqueId()).teleportLocation);
    }

    @Subcommand("reset")
    public void reset(Player sender) {
        if (plugin.mineManager.getMine(sender.getUniqueId()) == null) {
            sender.sendMessage("§6§lPrivateMines §8» §7You do not have a mine!");
            return;
        }
        plugin.mineManager.getMine(sender.getUniqueId()).resetMine();
        sender.sendMessage("§6§lPrivateMines §8» §7You have reset your mine!");
    }

    @Subcommand("upgrade")
    public void upgrade(Player sender) {
        if (plugin.mineManager.getMine(sender.getUniqueId()) == null) {
            sender.sendMessage("§6§lPrivateMines §8» §7You do not have a mine!");
            return;
        }
        plugin.mineManager.getMine(sender.getUniqueId()).upgradeMine();
        sender.sendMessage("§6§lPrivateMines §8» §7You have upgraded your mine!");
    }
}
