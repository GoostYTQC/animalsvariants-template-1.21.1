package com.github.goostytqc.mixin;

import com.github.goostytqc.data.ModAttachmentTypes;
import com.github.goostytqc.data.ModCowVariantData;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowEntity.class)
public class CowEntityMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCowSpawn(CallbackInfo ci) {
        CowEntity entity = (CowEntity) (Object) this;
        World world = entity.getWorld();

        if (world == null || world.isClient()) return;

        world.getServer().execute(() -> assignCowBiome(entity));
    }

    private void assignCowBiome(CowEntity entity) {
        if (entity.getAttached(ModAttachmentTypes.COW_VARIANT) != null) {
            return; // ✅ If the cow already has a variant, don't change it!
        }

        World world = entity.getWorld();
        BlockPos pos = entity.getBlockPos();

        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        if (biomeEntry == null || !biomeEntry.hasKeyAndValue()) return;

        Biome biome = biomeEntry.value();
        Identifier biomeId = world.getRegistryManager().get(RegistryKeys.BIOME).getId(biome);
        if (biomeId == null) return;

        String variant;
        if (isColdBiome(biomeId)) {
            variant = "cold";
        } else if (isWarmBiome(biomeId)) {
            variant = "warm";
        } else {
            variant = "temperate";
        }

        // ✅ Save the variant permanently
        entity.setAttached(ModAttachmentTypes.COW_VARIANT, new ModCowVariantData(variant));
        System.out.println("[DEBUG] Assigned variant: " + variant + " to " + entity.getUuid());
    }


    private boolean isColdBiome(Identifier biomeId) {
        return biomeId != null && (
                biomeId.equals(Identifier.of("minecraft", "old_growth_pine_taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "old_growth_spruce_taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "snowy_taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "snowy_plains")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_hills")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_gravelly_hills")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_forest"))
        );
    }

    private boolean isWarmBiome(Identifier biomeId) {
        return biomeId != null && (
                biomeId.equals(Identifier.of("minecraft", "savanna")) ||
                        biomeId.equals(Identifier.of("minecraft", "savanna_plateau")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_savanna")) ||
                        biomeId.equals(Identifier.of("minecraft", "jungle")) ||
                        biomeId.equals(Identifier.of("minecraft", "sparse_jungle")) ||
                        biomeId.equals(Identifier.of("minecraft", "bamboo_jungle")) ||
                        biomeId.equals(Identifier.of("minecraft", "badlands")) ||
                        biomeId.equals(Identifier.of("minecraft", "eroded_badlands")) ||
                        biomeId.equals(Identifier.of("minecraft", "wooded_badlands")) ||
                        biomeId.equals(Identifier.of("minecraft", "desert")) ||
                        biomeId.equals(Identifier.of("minecraft", "mangrove_swamp"))
        );
    }
}
