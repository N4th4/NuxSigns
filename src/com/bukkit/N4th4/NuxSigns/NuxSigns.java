package com.bukkit.N4th4.NuxSigns;

import java.util.HashMap;
import java.util.Hashtable;

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
    private final Hashtable<String, String[]> ht = new Hashtable<String, String[]>();

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
                if (args.length == 0) {
                    help(sender);
                } else {
                    Sign sign = getSign(senderP);
                    if (sign != null) {
                        if (args[0].equalsIgnoreCase("clear")) {
                            if (args.length != 2) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Usage : /NuxSigns clear [line]");
                            } else {
                                int index = getIndex(args[1], sender);
                                if (index != -1) {
                                    sign.setLine(index - 1, "");
                                    sign.update();
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("copy")) {
                            String name = senderP.getName();
                            String[] lines = sign.getLines();
                            ht.put(name, lines);
                            sender.sendMessage(ChatColor.GREEN + "[NuxSigns] Sign copied succefully");
                        } else if (args[0].equalsIgnoreCase("paste")) {
                            String name = senderP.getName();
                            if (ht.containsKey(name)) {
                                String[] lines = ht.get(name);
                                for (int i = 0; i < lines.length; i++) {
                                    sign.setLine(i, lines[i]);
                                }
                                sign.update();
                            } else {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] \"Use /NuxSigns copy\" before");
                            }
                        } else {
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Usage : /NuxSigns [line] [text]");
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
            player.sendMessage(ChatColor.RED + "[NuxSigns] You don't point a sign");
            return null;
        }
    }

    private int getIndex(String _index, CommandSender sender) {
        int index = -1;
        try {
            index = Integer.valueOf(_index).intValue();
        } catch (NumberFormatException e) {
        }
        if (index > 0 && index < 5) {
            return index;
        } else {
            sender.sendMessage(ChatColor.RED + "[NuxSigns] Invalid line number");
            return -1;
        }
    }

    private void help(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "Commands :");
        sender.sendMessage(ChatColor.AQUA + "    /sign clear [line]");
        sender.sendMessage(ChatColor.AQUA + "    /sign [line] [text]");
        sender.sendMessage(ChatColor.AQUA + "    /sign copy");
        sender.sendMessage(ChatColor.AQUA + "    /sign paste");
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
