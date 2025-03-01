package com.github.goostytqc.mixin;

import com.github.goostytqc.data.ModAttachmentTypes;
import com.github.goostytqc.data.ModCowVariantData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class CowEntityMixin {
    private static final Random random = new Random();

    @Unique
    private int mobVariantCheckTicks = 0; // Counter for delay

    // Run biome assignment in the constructor but only schedule it
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onMobEntityConstruct(EntityType<? extends MobEntity> entityType, World world, CallbackInfo ci) {
        if ((Object) this instanceof CowEntity) {
            mobVariantCheckTicks = 1; // Reduced delay (10 ticks = 0.5s)
        }
    }

    // Inject into tick() to run biome check after delay
    @Inject(method = "tick", at = @At("HEAD"))
    private void onMobTick(CallbackInfo ci) {
        if ((Object) this instanceof CowEntity cowEntity) {
            if (cowEntity.getWorld().isClient()) return; // Avoid running on the client side

            if (mobVariantCheckTicks > 0) {
                mobVariantCheckTicks--; // Decrement counter
                if (mobVariantCheckTicks == 0) {
                    assignCowBiome(cowEntity); // Run biome check once
                }
            }
        }
    }

    private void assignCowBiome(CowEntity entity) {
        if (entity.getAttached(ModAttachmentTypes.COW_VARIANT) != null) {
            return; // Already assigned
        }

        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();

        // Ensure valid position
        if (pos.getY() < world.getBottomY() || pos.getY() > world.getTopY()) {
            return;
        }

        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) return;

        Biome biome = biomeEntry.value();
        Identifier biomeId = world.getRegistryManager().get(RegistryKeys.BIOME).getId(biome);
        if (biomeId == null) return;

        String variant = determineCowVariant(entity.getUuid(), biomeId);

        // Save the variant permanently
        entity.setAttached(ModAttachmentTypes.COW_VARIANT, new ModCowVariantData(variant));
    }

    private String determineCowVariant(UUID uuid, Identifier biomeId) {
        if (isColdBiome(biomeId)) return "cold";
        if (isWarmBiome(biomeId)) return "warm";
        return "temperate"; // Default variant
    }

    private boolean isColdBiome(Identifier biomeId) {
        return biomeId != null && switch (biomeId.toString()) {
            case "minecraft:old_growth_pine_taiga",
                 "minecraft:old_growth_spruce_taiga",
                 "minecraft:taiga",
                 "minecraft:snowy_taiga",
                 "minecraft:snowy_plains",
                 "minecraft:windswept_hills",
                 "minecraft:windswept_gravelly_hills",
                 "minecraft:windswept_forest" -> true;
            default -> false;
        };
    }

    private boolean isWarmBiome(Identifier biomeId) {
        return biomeId != null && switch (biomeId.toString()) {
            case "minecraft:savanna",
                 "minecraft:savanna_plateau",
                 "minecraft:windswept_savanna",
                 "minecraft:jungle",
                 "minecraft:sparse_jungle",
                 "minecraft:bamboo_jungle",
                 "minecraft:badlands",
                 "minecraft:eroded_badlands",
                 "minecraft:wooded_badlands",
                 "minecraft:desert",
                 "minecraft:mangrove_swamp" -> true;
            default -> false;
        };
    }
}
