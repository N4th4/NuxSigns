package com.bukkit.N4th4.NuxSigns;

import java.util.logging.Logger;
import java.util.logging.Level;

public class NSLogger {
    private static Logger log;

    public static void initialize() {
        log = Logger.getLogger("Minecraft");
    }

    public static void info(String message) {
        log.log(Level.INFO, "[NuxSigns] " + message);
    }

    public static void severe(String message) {
        log.log(Level.SEVERE, "[NuxSigns] " + message);
    }
}
