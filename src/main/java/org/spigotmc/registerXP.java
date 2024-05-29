package org.spigotmc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.util.Random;

public class registerXP extends JavaPlugin {

    public class CommandReg implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                String playerName = player.getName();
                FileConfiguration config = getConfig();

                LocalDate today = LocalDate.now();

                String lastSignInDate = config.getString("player." + playerName);
                if (lastSignInDate != null && LocalDate.parse(lastSignInDate).equals(today)) {
                    player.sendMessage(config.getString("message.register"));
                    return true;
                }

                Random random = new Random();
                int minXP = config.getInt("min-xp");
                int maxXP = config.getInt("max-xp");

                int xpo = player.getLevel();
                int xp = random.nextInt(maxXP - minXP + 1) + minXP;
                player.giveExp(xp);
                int xpn = player.getLevel();
                int expi = xpn - xpo;
                player.sendMessage(config.getString("message.title"));
                player.sendMessage(config.getString("message.info") + xp + " Exp");
                player.sendMessage(config.getString("message.exp") + new String(String.valueOf(expi)));

                config.set("player." + playerName, today.toString());
                saveConfig();

                return true;
            }

            return false;
        }
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("register").setExecutor(new CommandReg());
    }

    @Override
    public void onDisable() {
    }
}
