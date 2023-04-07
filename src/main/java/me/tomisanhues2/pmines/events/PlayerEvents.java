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
        if (player.getWorld() != Bukkit.getWorld("Spawn"))
            player.teleport(Bukkit.getWorld("Spawn").getSpawnLocation());


        if (plugin.mineManager.getMine(player.getUniqueId()) != null)
            player.sendMessage("§6§lPrivateMines §8» §7Your mine has been loaded!");

        if (new File(plugin.getDataFolder(), "mineData/" + player.getUniqueId() + ".yml").exists()) {
            player.sendMessage("§6§lPrivateMines §8» §7You have a mine! Wait for it to load...");
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(PrivateMines.getInstance(), () -> {
                if (plugin.mineManager.loadMineData(player.getUniqueId())) {
                    player.sendMessage("§6§lPrivateMines §8» §7Your mine has been loaded!");
                }
            });
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
        if (plugin.mineManager.getMine(event.getPlayer().getUniqueId()) == null)
            event.setCancelled(true);
    }
}
