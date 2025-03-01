package com.github.goostytqc.server;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WarmCowTracker {
    private static final Set<UUID> warmCows = new HashSet<>();

    public static boolean isWarmCow(CowEntity entity) {
        UUID uuid = entity.getUuid();

        // Check memory first
        if (warmCows.contains(uuid)) {
            return true;
        }

        // Check NBT if not found in memory
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        return nbt.contains("WarmCow") && nbt.getBoolean("WarmCow");
    }

    public static void addWarmCow(CowEntity entity) {
        warmCows.add(entity.getUuid());

        // Save to NBT
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        nbt.putBoolean("WarmCow", true);
        entity.readNbt(nbt);
    }

    public static void removeWarmCow(CowEntity entity) {
        warmCows.remove(entity.getUuid());

        // Save to NBT
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        nbt.putBoolean("WarmCow", false);
        entity.readNbt(nbt);
    }

    public static void clear() {
        warmCows.clear();
    }
}
