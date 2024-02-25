package org.cosmodev.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GiveSets {
    private static Map<Player, String> playerKits = new HashMap<>();

    public static void giveKit(Player player, String kit) {
        // Реализуйте выдачу предметов для каждого кита
        switch (kit) {
            case "default":
                ItemStack itemStack = new ItemStack(Material.DIAMOND, 2);
                ItemStack itemStack1 = new ItemStack(Material.STICK, 1);
                player.getInventory().addItem(itemStack);
                player.getInventory().addItem(itemStack1);
                break;
            case "iron":
                ItemStack ironStack = new ItemStack(Material.IRON_INGOT, 2);
                ItemStack ironStack1 = new ItemStack(Material.STICK, 1);
                player.getInventory().addItem(ironStack);
                player.getInventory().addItem(ironStack1);
                break;
            case "gold":
                ItemStack goldStack = new ItemStack(Material.GOLD_INGOT, 2);
                ItemStack goldStack1 = new ItemStack(Material.STICK, 1);
                player.getInventory().addItem(goldStack);
                player.getInventory().addItem(goldStack1);
                break;
            default:
                break;
        }

        // Записываем информацию о ките для игрока
        playerKits.put(player, kit);
        player.sendMessage("Вы получили набор: " + kit);
    }

    public static String getKit(Player player) {
        return playerKits.getOrDefault(player, "unknown");
    }
}
