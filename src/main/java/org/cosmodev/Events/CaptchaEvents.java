package org.cosmodev.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.cosmodev.Plugin;
import org.cosmodev.Status.CaptchaStatus;
import org.cosmodev.Status.StatusManager;
import org.cosmodev.Utils.GiveSets;
import org.cosmodev.Utils.WebhookSender;

import java.util.*;

public class CaptchaEvents implements Listener {
    List<String> kits = new ArrayList<>(List.of("diamond", "iron", "gold"));
    private static int totalCaptchasPassed = 0;
    private static final Map<String, Long> ipCooldowns = new HashMap<>();
    private static final long COOLDOWN_DURATION = 15 * 60 * 1000;

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StatusManager.setPlayerStatus(player, CaptchaStatus.OFFLINE);
        CaptchaStatus status = StatusManager.getPlayerStatus(player);
        String ipAddress = player.getAddress().getAddress().getHostAddress();
        if (status == CaptchaStatus.OFFLINE && !isIpWithinCooldown(ipAddress)) {
            Location locationBack = player.getLocation().clone();
            StatusManager.setBackLocation(player, locationBack);
            player.getInventory().clear();
            String worldName = Plugin.getInstance().getConfig().getString("teleportLocation.world");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location locationTp = new Location(world,
                        Plugin.getInstance().getConfig().getDouble("teleportLocation.x"),
                        Plugin.getInstance().getConfig().getDouble("teleportLocation.y"),
                        Plugin.getInstance().getConfig().getDouble("teleportLocation.z"));

                Random random = new Random();
                int randomIndex = random.nextInt(kits.size());
                String randomKit = kits.get(randomIndex);
                GiveSets.giveKit(player, randomKit);
                StatusManager.setPlayerStatus(player, CaptchaStatus.IN_PROCESS);
                player.teleport(locationTp);
            } else {
                player.sendMessage("Извините, произошла ошибка во время генерации капчи. Сообщите администрации за вознаграждение!");
                System.out.println("Ошибка телепортации игрока, проверьте ваш config.yml");
                StatusManager.setPlayerStatus(player, CaptchaStatus.COMPLETED);
            }
        }
    }

    @EventHandler
    public void leaveEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        player.getInventory().clear();
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) throws Exception {
        Player player = (Player) event.getWhoClicked();
        ItemStack craftedItem = event.getCurrentItem();
        Material itemToCraft = null;

        if (GiveSets.getKit(player).equals("default")) {
            itemToCraft = Material.DIAMOND_SWORD;
        } else if (GiveSets.getKit(player).equals("iron")) {
            itemToCraft = Material.IRON_SWORD;
        } else if (GiveSets.getKit(player).equals("gold"))  {
            itemToCraft = Material.GOLDEN_SWORD;
        }

        if (craftedItem != null && itemToCraft != null && craftedItem.getType() == itemToCraft) {
            if (StatusManager.getPlayerStatus(player) == CaptchaStatus.IN_PROCESS) {
                Location backLocation = StatusManager.getBackLocation(player);
                String ipAddress = player.getAddress().getAddress().getHostAddress();
                if (backLocation != null) {
                    player.teleport(backLocation);
                } else {
                    player.sendMessage("Произошла ошибка, пожалуйста сообщите администрации!");
                }
                player.getInventory().clear();
                StatusManager.setPlayerStatus(player, CaptchaStatus.COMPLETED);
                player.getInventory().clear();
                totalCaptchasPassed++;
                ipCooldowns.put(ipAddress, System.currentTimeMillis());
                WebhookSender.sendWebhook(player);
            }
        }
    }
    @EventHandler
    public void chatEvent(PlayerChatEvent event){
        Player player = event.getPlayer();
        CaptchaStatus status = StatusManager.getPlayerStatus(player);
        if(status == CaptchaStatus.IN_PROCESS){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void attackPlayer(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if(StatusManager.getPlayerStatus(attacker) == CaptchaStatus.IN_PROCESS){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (StatusManager.getPlayerStatus(event.getPlayer()) == CaptchaStatus.IN_PROCESS) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Вы не можете использовать команды во время прохождения капчи! Скрафтите алмазный меч");
        }
    }
    public static int getTotalCaptchasPassed() {
        return totalCaptchasPassed;
    }
    private boolean isIpWithinCooldown(String ipAddress) {
        if (ipCooldowns.containsKey(ipAddress)) {
            long lastPassTime = ipCooldowns.get(ipAddress);
            return System.currentTimeMillis() - lastPassTime < COOLDOWN_DURATION;
        }
        return false;
    }
    public static void cleanupExpiredIpCooldowns() {
        long currentTime = System.currentTimeMillis();
        ipCooldowns.entrySet().removeIf(entry -> currentTime - entry.getValue() > COOLDOWN_DURATION);
    }
}
