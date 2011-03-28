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
                    help(sender);
                } else {
                    Sign sign = getSign(senderP);
                    if (args[0].equalsIgnoreCase("clear")) {
                        int index = getIndex(args[1], sender);
                        if (index != -1) {
                            sign.setLine(index - 1, "");
                            sign.update();
                        }
                    } else {
                        int index = getIndex(args[0], sender);
                        if (index != -1) {
                            String string = "";
                            for (int i = 1; i < args.length; i++) {
                                string = string.concat(args[i]).concat(" ");
                            }
                            string = string.substring(0, string.length() - 1);
                            if (string.length() <= 15) {
                                sign.setLine(index - 1, string);
                                sign.update();
                            } else {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] String too long");
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            sender.sendMessage("[NuxSigns] Only commands in chat are supported");
            return true;
        }
    }

    private Sign getSign(Player player) {
        Block block = player.getTargetBlock(null, 5);
        if (block.getState() instanceof Sign) {
            return (Sign) block.getState();
        } else {
            player.sendMessage(ChatColor.RED + "[NuxSigns] You don't point a Sign");
            return null;
        }
    }

    private int getIndex(String _index, CommandSender sender) {
        int index = Integer.valueOf(_index).intValue();
        if (index > 0 && index < 5) {
            return index;
        } else {
            sender.sendMessage(ChatColor.RED + "[NuxSigns] Invalid line number");
            return -1;
        }
    }

    private void help(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "Commands :");
        sender.sendMessage(ChatColor.AQUA + "    /NuxSigns clear [line]");
        sender.sendMessage(ChatColor.AQUA + "    /NuxSigns [line] [text]");
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
