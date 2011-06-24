package com.bukkit.N4th4.NuxSigns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class NuxSigns extends JavaPlugin {
    private final HashMap<Player, Boolean>    debugees    = new HashMap<Player, Boolean>();
    private final Hashtable<String, String[]> ht          = new Hashtable<String, String[]>();
    private final HashSet<Byte>               tMaterials  = new HashSet<Byte>();
    private PermissionManager                 permissions = null;

    public NuxSigns() {
        NSLogger.initialize();
        tMaterials.add((byte) 0); // Air
        tMaterials.add((byte) 6); // Sapling
        tMaterials.add((byte) 8); // Water
        tMaterials.add((byte) 9); // Water
        tMaterials.add((byte) 37); // Yellow Flower
        tMaterials.add((byte) 38); // Red Flower
        tMaterials.add((byte) 50); // Torch
        tMaterials.add((byte) 51); // Fire
        tMaterials.add((byte) 55); // Redstone
        tMaterials.add((byte) 66); // Minecart Track
        tMaterials.add((byte) 75); // Redstone Torch
        tMaterials.add((byte) 76); // Redstone Torch
        tMaterials.add((byte) 78); // Snow
        tMaterials.add((byte) 93); // Diode
        tMaterials.add((byte) 94); // Diode
    }

    public void onEnable() {
        if (this.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
            permissions = PermissionsEx.getPermissionManager();
        } else {
            NSLogger.severe("PermissionsEx not found. Disabling");
            this.getServer().getPluginManager().disablePlugin(this);
        }

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
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Usage : /sign clear [line]");
                            } else if (!permissions.has(senderP, "nuxsigns.clear")) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Permission denied");
                            } else {
                                int index = getIndex(args[1], sender);
                                if (index != -1) {
                                    sign.setLine(index - 1, "");
                                    sign.update();
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("copy")) {
                            if (!permissions.has(senderP, "nuxsigns.copy")) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Permission denied");
                            } else {
                                String name = senderP.getName();
                                String[] lines = sign.getLines();
                                ht.put(name, lines);
                                sender.sendMessage(ChatColor.GREEN + "[NuxSigns] Sign copied succefully");
                            }
                        } else if (args[0].equalsIgnoreCase("paste")) {
                            if (!permissions.has(senderP, "nuxsigns.paste")) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Permission denied");
                            } else {
                                String name = senderP.getName();
                                if (ht.containsKey(name)) {
                                    String[] lines = ht.get(name);
                                    for (int i = 0; i < lines.length; i++) {
                                        sign.setLine(i, lines[i]);
                                    }
                                    sign.update();
                                } else {
                                    sender.sendMessage(ChatColor.RED + "[NuxSigns] \"Use /sign copy\" before");
                                }
                            }
                        } else {
                            if (args.length < 2) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Usage : /sign [line] [text]");
                            } else if (!permissions.has(senderP, "nuxsigns.use")) {
                                sender.sendMessage(ChatColor.RED + "[NuxSigns] Permission denied");
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
        Block block = player.getTargetBlock(tMaterials, 5);
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
