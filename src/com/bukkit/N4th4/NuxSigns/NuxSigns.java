package com.bukkit.N4th4.NuxSigns;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class NuxSigns extends JavaPlugin {
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

    public NuxSigns() {
        NSLogger.initialize();
    }

    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        NSLogger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String commandName = command.getName();
        if (sender instanceof Player) {
            Player senderP = (Player) sender;
            if (commandName.equalsIgnoreCase("Sign")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "[NuxSigns] Usage : /Sign [line] [text]");
                } else {
                    Block block = senderP.getTargetBlock(null, 5);
                    if (block.getState() instanceof Sign) {
                        Sign sign = (Sign) block.getState();
                        int index = Integer.valueOf(args[0]).intValue();
                        if (index > 0 && index < 5) {
                            String string = "";
                            for (int i=1; i<args.length; i++)
                            {
                                string = string.concat(args[i]).concat(" ");
                            }
                            if (string.length() <= 15)
                            {
                                sign.setLine(index-1, string);
                                sign.update();
                            } else {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] String too long");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "[NuxSigns] Invalid line number");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "[NuxSigns] You don't point a Sign");
                    }
                }
            }
            return true;
        } else {
            sender.sendMessage("[NuxSigns] Only commands in chat are supported");
            return true;
        }
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}
