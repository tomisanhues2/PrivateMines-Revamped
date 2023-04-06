package me.tomisanhues2.pmines.utils;

import me.tomisanhues2.pmines.data.UpgradeLevel;

import java.util.List;

public class ConfigUtils {

    private static List<UpgradeLevel> upgradeLevels;

    public ConfigUtils() {
        upgradeLevels = List.of(
                new UpgradeLevel(1, 100, List.of("DIRT"))
        );
    }

    public static List<UpgradeLevel> getUpgradeLevels() {
        return upgradeLevels;
    }

    public static UpgradeLevel getUpgradeLevel(int tier) {
        return upgradeLevels.get(tier - 1);
    }
}
