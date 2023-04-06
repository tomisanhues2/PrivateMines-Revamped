package me.tomisanhues2.pmines.utils;

import me.tomisanhues2.pmines.data.UpgradeLevel;

import java.util.List;

public class ConfigUtils {

    private static List<UpgradeLevel> upgradeLevels;

    public ConfigUtils() {
        upgradeLevels = List.of(
                new UpgradeLevel(1, 1000000, List.of("COBBLESTONE")),
                new UpgradeLevel(2, 1000000, List.of("COBBLESTONE", "STONE")),
                new UpgradeLevel(3, 1000000, List.of("COBBLESTONE", "STONE", "STONE_BRICKS")),
                new UpgradeLevel(4, 5000000, List.of("ANDESITE")),
                new UpgradeLevel(5, 5000000, List.of("ANDESITE", "GRANITE")),
                new UpgradeLevel(6, 5000000, List.of("ANDESITE", "GRANITE", "DIORITE")),
                new UpgradeLevel(7, 10000000, List.of("POLISHED_ANDESITE")),
                new UpgradeLevel(8, 10000000, List.of("POLISHED_ANDESITE", "POLISHED_GRANITE")),
                new UpgradeLevel(9, 10000000, List.of("POLISHED_ANDESITE", "POLISHED_GRANITE", "POLISHED_DIORITE")),
                new UpgradeLevel(10, 15000000, List.of("BLACKSTONE")),
                new UpgradeLevel(11, 15000000, List.of("BLACKSTONE", "POLISHED_BLACKSTONE")),
                new UpgradeLevel(12, 15000000, List.of("BLACKSTONE", "POLISHED_BLACKSTONE", "GILDED_BLACKSTONE")),
                new UpgradeLevel(13, 20000000, List.of("PRISMARINE")),
                new UpgradeLevel(14, 25000000, List.of("PRISMARINE", "PRISMARINE_BRICKS")),
                new UpgradeLevel(15, 30000000, List.of("PRISMARINE", "PRISMARINE_BRICKS", "DARK_PRISMARINE")),
                new UpgradeLevel(16, 35000000, List.of("DEEPSLATE")),
                new UpgradeLevel(17, 40000000, List.of("DEEPSLATE", "DEEPSLATE_TILES")),
                new UpgradeLevel(18, 45000000, List.of("DEEPSLATE", "DEEPSLATE_TILES", "DEEPSLATE_DIAMOND_ORE")),
                new UpgradeLevel(19, 50000000, List.of("DEEPSLATE", "DEEPSLATE_TILES", "DEEPSLATE_EMERALD_ORE")),
                new UpgradeLevel(20, 60000000, List.of("MOSSY_COBBLESTONE")),
                new UpgradeLevel(21, 70000000, List.of("MOSSY_COBBLESTONE", "MOSSY_STONE_BRICKS")),
                new UpgradeLevel(22, 80000000, List.of("MOSSY_COBBLESTONE", "MOSSY_STONE_BRICKS", "CHISELED_STONE_BRICKS")),
                new UpgradeLevel(23, 90000000, List.of("NETHERRACK")),
                new UpgradeLevel(24, 100000000, List.of("NETHERRACK", "NETHER_BRICKS")),
                new UpgradeLevel(25, 110000000, List.of("NETHERRACK", "NETHER_BRICKS", "NETHER_QUARTZ_ORE")),
                new UpgradeLevel(26, 120000000, List.of("END_STONE")),
                new UpgradeLevel(27, 130000000, List.of("END_STONE", "END_STONE_BRICKS")),
                new UpgradeLevel(28, 140000000, List.of("COAL_BLOCK")),
                new UpgradeLevel(29, 150000000, List.of("COAL_BLOCK", "REDSTONE_BLOCK")),
                new UpgradeLevel(30, 200000000, List.of("COAL_BLOCK", "REDSTONE_BLOCK", "LAPIS_BLOCK")),
                new UpgradeLevel(31, 225000000, List.of("RAW_IRON_BLOCK")),
                new UpgradeLevel(32, 250000000, List.of("RAW_IRON_BLOCK", "IRON_BLOCK")),
                new UpgradeLevel(33, 275000000, List.of("RAW_GOLD_BLOCK")),
                new UpgradeLevel(34, 300000000, List.of("RAW_GOLD_BLOCK", "GOLD_BLOCK")),
                new UpgradeLevel(35, 400000000, List.of("DEEPSLATE_DIAMOND_ORE")),
                new UpgradeLevel(36, 500000000, List.of("DEEPSLATE_DIAMOND_ORE", "DIAMOND_BLOCK")),
                new UpgradeLevel(37, 750000000, List.of("DEEPSLATE_DIAMOND_ORE", "DIAMOND_BLOCK", "DEEPSLATE_EMERALD_ORE")),
                new UpgradeLevel(38, 1000000000, List.of("DEEPSLATE_DIAMOND_ORE", "DIAMOND_BLOCK", "DEEPSLATE_EMERALD_ORE", "EMERALD_BLOCK"))
        );
    }

    public static List<UpgradeLevel> getUpgradeLevels() {
        return upgradeLevels;
    }

    public static UpgradeLevel getUpgradeLevel(int tier) {
        return upgradeLevels.get(tier - 1);
    }
}
