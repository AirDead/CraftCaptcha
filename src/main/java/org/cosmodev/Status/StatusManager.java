package org.cosmodev.Status;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatusManager {
    private static final Map<UUID, CaptchaStatus> playerStatusMap = new HashMap<>();
    private static final Map<UUID, Location> backLocationMap = new HashMap<>();

    public static void setPlayerStatus(Player player, CaptchaStatus status) {
        playerStatusMap.put(player.getUniqueId(), status);
    }

    public static CaptchaStatus getPlayerStatus(Player player) {
        return playerStatusMap.getOrDefault(player.getUniqueId(), CaptchaStatus.OFFLINE);
    }

    public static void setBackLocation(Player player, Location backLocation) {
        backLocationMap.put(player.getUniqueId(), backLocation);
    }

    public static Location getBackLocation(Player player) {
        return backLocationMap.get(player.getUniqueId());
    }
}
