package me.tomisanhues2.pmines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.data.PrivateMine;
import me.tomisanhues2.pmines.utils.MineUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("adminpm|apm")
public class AdminCommands extends BaseCommand {

    @Subcommand("tp")
    public void tp(Player sender) {
        sender.teleport(Bukkit.getWorld("private_mine_world").getSpawnLocation());
        sender.sendMessage("§6§lPrivateMines §8» §7You have teleported to the mine world!");
    }

    @Subcommand("new")
    public void newMine(Player sender, Player target) {
        sender.sendMessage("§6§lPrivateMines §8» §7Started creating a new mine for " + target.getName() + "...");
        //Schedule an async task to create a mine
        Bukkit.getScheduler().runTaskAsynchronously(PrivateMines.getInstance(), () -> {
            PrivateMine pm = MineUtils.createNewMine(target.getUniqueId());
            sender.sendMessage("§6§lPrivateMines §8» §7You have created a new mine for " + target.getName() + "!");
            target.sendMessage("§6§lPrivateMines §8» §7Use /pm tp to teleport to your mine!");
        });
    }

    @Subcommand("list")
    public void listMines(Player sender) {
        sender.sendMessage("§6§lPrivateMines §8» §7Listing all mines...");
        Bukkit.getScheduler().runTaskAsynchronously(PrivateMines.getInstance(), () -> {
            for (PrivateMine pm : PrivateMines.getInstance().mineManager.getMines()) {
                sender.sendMessage("§6§lPrivateMines §8» §7" + pm.uuid + " - " + pm.upgradeLevel.getTier());
            }
        });
    }

}
