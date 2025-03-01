package com.github.goostytqc.mixin;

import com.github.goostytqc.server.ColdCowTracker;
import com.github.goostytqc.server.WarmCowTracker;
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
import java.util.UUID;

@Mixin(CowEntity.class)
public class CowEntityMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCowSpawn(CallbackInfo ci) {
        CowEntity entity = (CowEntity) (Object) this;
        World world = entity.getWorld();
        UUID uuid = entity.getUuid();

        if (world == null || world.isClient()) return;

        BlockPos pos = entity.getBlockPos();
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        Biome biome = biomeEntry.value();
        Identifier biomeId = world.getRegistryManager().get(RegistryKeys.BIOME).getId(biome);

        if (isColdBiome(biomeId)) {
            ColdCowTracker.addColdCow(uuid);
        }
        if (isWarmBiome(biomeId)) {
            WarmCowTracker.addWarmCow(uuid);
        }
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
