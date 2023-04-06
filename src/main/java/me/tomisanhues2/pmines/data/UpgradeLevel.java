package me.tomisanhues2.pmines.data;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class UpgradeLevel {

    private int tier;
    private int cost;
    private List<String> materials;

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
}
