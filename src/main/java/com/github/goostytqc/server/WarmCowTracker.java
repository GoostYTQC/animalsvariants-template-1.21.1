package com.github.goostytqc.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WarmCowTracker {
    private static final Set<UUID> warmCows = new HashSet<>();


    public static boolean isWarmCow(UUID uuid, boolean warmBiome) { return warmCows.contains(uuid) || warmBiome;
    }

    public static void addWarmCow(UUID uuid) {
        warmCows.add(uuid);
    }

    public static void removeWarmCow(UUID uuid) {
        warmCows.remove(uuid);
    }

    public static void clear() {
        warmCows.clear();
    }
}
