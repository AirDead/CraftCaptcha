package org.cosmodev.Utils;

import org.bukkit.entity.Player;
import org.cosmodev.Plugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebhookSender {
    private static final String webhookUrl = Plugin.getInstance().getConfig().getString("webhook.url");
    private static final int webhookStatus = Plugin.getInstance().getConfig().getInt("webhook.status");

    public static void sendWebhook(Player player) throws Exception {
        if (webhookStatus == 1) {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");

            String jsonPayload = "{\"content\": \"" + "Пользователь " + player.getName() + " прошел капчу!" + "\"}";

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Статус отправки сообщения через вебхук: " + responseCode);

            connection.disconnect();
        }
    }
}