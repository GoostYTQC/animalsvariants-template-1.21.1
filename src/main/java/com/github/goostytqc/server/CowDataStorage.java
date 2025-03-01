package com.github.goostytqc.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CowDataStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Set<UUID> coldCows = new HashSet<>();
    private static final Set<UUID> warmCows = new HashSet<>();
    private static final File FILE = new File("config/cow_data.json");

    // ✅ Load data from file on startup
    public static void load() {
        if (!FILE.exists()) {
            System.out.println("[DEBUG] cow_data.json not found, creating new file.");
            save(); // Create an empty file
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            Type type = new TypeToken<CowData>() {}.getType();
            CowData data = GSON.fromJson(reader, type);

            if (data != null) {
                coldCows.clear();
                warmCows.clear();
                coldCows.addAll(data.coldCows);
                warmCows.addAll(data.warmCows);
            }
            System.out.println("[DEBUG] Loaded cow data: " + coldCows.size() + " cold, " + warmCows.size() + " warm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Save the cow data to JSON
    private static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(new CowData(coldCows, warmCows), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isColdCow(UUID uuid) {
        return coldCows.contains(uuid);
    }

    public static boolean isWarmCow(UUID uuid) {
        return warmCows.contains(uuid);
    }

    public static void addColdCow(UUID uuid) {
        coldCows.add(uuid);
        save();
    }

    public static void addWarmCow(UUID uuid) {
        warmCows.add(uuid);
        save();
    }

    public static void removeColdCow(UUID uuid) {
        coldCows.remove(uuid);
        save();
    }

    public static void removeWarmCow(UUID uuid) {
        warmCows.remove(uuid);
        save();
    }

    public static void clear() {
        coldCows.clear();
        warmCows.clear();
        save();
    }

    private static class CowData {
        Set<UUID> coldCows;
        Set<UUID> warmCows;

        CowData(Set<UUID> coldCows, Set<UUID> warmCows) {
            this.coldCows = coldCows;
            this.warmCows = warmCows;
        }
    }
}
