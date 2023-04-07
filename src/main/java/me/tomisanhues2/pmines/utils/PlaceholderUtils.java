package me.tomisanhues2.pmines.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tomisanhues2.pmines.PrivateMines;
import me.tomisanhues2.pmines.data.UpgradeLevel;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderUtils extends PlaceholderExpansion {

    private final PrivateMines plugin;

    public PlaceholderUtils(PrivateMines plugin) {
        this.plugin = plugin;

    }
    @Override
    public @NotNull String getIdentifier() {
        return "pmine";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Tomisanhues2";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return null;
        }
        if (params.equalsIgnoreCase("mine_currentupgrade")) {
            if (plugin.mineManager.getMine(player.getUniqueId()) == null) {
                return "No data";
            }
            return String.valueOf(plugin.mineManager.getMine(player.getUniqueId()).upgradeLevel.getTier());
        }
        if (params.equalsIgnoreCase("mine_upgradecost")) {
            if (plugin.mineManager.getMine(player.getUniqueId()) == null) {
                return "No data";
            }
            UpgradeLevel level = ConfigUtils.getUpgradeLevel(plugin.mineManager.getMine(player.getUniqueId()).upgradeLevel.getTier() + 1);
            return String.valueOf(level.getCost());
        }
        return null;
    }
}
