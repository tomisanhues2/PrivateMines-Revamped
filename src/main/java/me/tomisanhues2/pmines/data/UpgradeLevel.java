package me.tomisanhues2.pmines.data;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeLevel implements ConfigurationSerializable {

    private final int tier;
    private final int cost;
    private final List<String> materials;

    public UpgradeLevel(int tier, int cost, List<String> materials) {
        this.tier = tier;
        this.cost = cost;
        this.materials = materials;
    }


    public int getTier() {
        return tier;
    }

    public List<Material> getMaterials() {
        List<Material> materials = new ArrayList<>();
        for (String material : this.materials) {
            materials.add(Material.valueOf(material));
        }
        return materials;
    }

    public int getCost() {
        return cost;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("tier", tier);
        map.put("cost", cost);
        map.put("materials", materials);
        return map;
    }

    public static UpgradeLevel deserialize(Map<String, Object> map) {
        return new UpgradeLevel((int) map.get("tier"), (int) map.get("cost"), (List<String>) map.get("materials"));
    }
}
