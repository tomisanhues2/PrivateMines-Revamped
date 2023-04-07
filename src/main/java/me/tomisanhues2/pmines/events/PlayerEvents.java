package me.tomisanhues2.pmines.events;

import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.utils.MineUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class PlayerEvents implements Listener {

    private final PrivateMines plugin;

    public PlayerEvents(PrivateMines plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.mineManager.getMine(player.getUniqueId()) != null) {
            player.sendTitle("§6§lPrivateMines", "§7Your mine has been loaded!", 10, 40, 10);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.mineManager.getMine(player.getUniqueId()) != null) {
            plugin.mineManager.getMine(player.getUniqueId()).stopTask();
        }
    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPreCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().contains("pmine") || event.getMessage().contains("pmines") || event.getMessage().contains("mine") || event.getMessage().contains("mines")) {
            if (plugin.mineManager.getMine(event.getPlayer().getUniqueId()) == null) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§6§lPrivateMines §8» §7You cannot use this command until you have a mine!");
            }
        }
    }
}
