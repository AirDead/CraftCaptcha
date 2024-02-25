package org.cosmodev.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cosmodev.Events.CaptchaEvents;

public class StatCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("stats")) {
            sender.sendMessage("Общее количество пройденных капч: " + CaptchaEvents.getTotalCaptchasPassed());
            return true;
        }
        return false;
    }
}
