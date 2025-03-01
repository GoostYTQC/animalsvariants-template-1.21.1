package com.github.goostytqc.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ColdCowTracker {
    private static final Set<UUID> coldCows = new HashSet<>();


    public static boolean isColdCow(UUID uuid, boolean coldBiome) {
        return coldCows.contains(uuid) || coldBiome;
    }

    public static void addColdCow(UUID uuid) {
        coldCows.add(uuid);
    }

    public static void removeColdCow(UUID uuid) {
        coldCows.remove(uuid);
    }

    public static void clear() {
        coldCows.clear();
    }
}
