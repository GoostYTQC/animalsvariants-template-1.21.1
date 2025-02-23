package com.github.goostytqc.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ColdCowTracker {
    private static final Set<UUID> coldCows = new HashSet<>();

    public static boolean isColdCow(UUID uuid, boolean coldBiome) {
        boolean result = coldCows.contains(uuid) || coldBiome;
        System.out.println("UUID : " + uuid + " | Déjà enregistré : " + coldCows.contains(uuid) + " | Biome froid : " + coldBiome + " | Résultat : " + result);
        return result;
    }


    public static void addColdCow(UUID uuid) {
        coldCows.add(uuid);
        System.out.println("Vache ajoutée au tracker : " + uuid);
    }

    public static void removeColdCow(UUID uuid) {
        coldCows.remove(uuid);
    }

    public static void clear() {
        coldCows.clear();
    }
}
