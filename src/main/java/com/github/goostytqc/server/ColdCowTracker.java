package com.github.goostytqc.server;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ColdCowTracker {
    private static final Set<UUID> coldCows = new HashSet<>();

    public static boolean isColdCow(CowEntity entity) {
        UUID uuid = entity.getUuid();

        // Check memory first
        if (coldCows.contains(uuid)) {
            return true;
        }

        // Check NBT if not found in memory
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        return nbt.contains("ColdCow") && nbt.getBoolean("ColdCow");
    }

    public static void addColdCow(CowEntity entity) {
        coldCows.add(entity.getUuid());

        // Save to NBT
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        nbt.putBoolean("ColdCow", true);
        entity.readNbt(nbt);
    }

    public static void removeColdCow(CowEntity entity) {
        coldCows.remove(entity.getUuid());

        // Save to NBT
        NbtCompound nbt = entity.writeNbt(new NbtCompound());
        nbt.putBoolean("ColdCow", false);
        entity.readNbt(nbt);
    }

    public static void clear() {
        coldCows.clear();
    }
}
