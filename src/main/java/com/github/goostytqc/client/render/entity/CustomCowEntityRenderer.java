package com.github.goostytqc.client.render.entity;

import com.github.goostytqc.client.render.entity.model.CustomCowEntityModel;
import com.github.goostytqc.client.render.entity.model.ColdCowModel;
import com.github.goostytqc.client.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class CustomCowEntityRenderer extends MobEntityRenderer<CowEntity, EntityModel<CowEntity>> {
    private static final Identifier DEFAULT_TEXTURE = Identifier.of("animalsvariants", "textures/entity/cow/temperate_cow.png");

    private static final Identifier[] COLD_TEXTURES = {
            Identifier.of("animalsvariants", "textures/entity/cow/cold_black_cow.png"),  // 5%
            Identifier.of("animalsvariants", "textures/entity/cow/cold_brown_cow.png"),  // 20%
            Identifier.of("animalsvariants", "textures/entity/cow/cold_cow.png"),    // 50%
            Identifier.of("animalsvariants", "textures/entity/cow/cold_light_brown_cow.png"), // 20%
            Identifier.of("animalsvariants", "textures/entity/cow/cold_white_cow.png")   // 5%
    };

    private final CustomCowEntityModel<CowEntity> defaultModel;
    private final ColdCowModel<CowEntity> coldModel;
    private final Random random = new Random();

    public CustomCowEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW)), 0.7F);

        this.defaultModel = new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW));
        this.coldModel = new ColdCowModel<>(context.getPart(ModEntityModelLayers.COLD_COW));
    }

    @Override
    public Identifier getTexture(CowEntity entity) {
        BlockPos pos = entity.getBlockPos();
        RegistryEntry<Biome> biomeEntry = entity.getWorld().getBiome(pos);
        Biome biome = biomeEntry.value();
        Identifier biomeId = entity.getWorld()
                .getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biome);

        if (isColdBiome(biomeId)) {
            return getRandomColdTexture(entity.getId());
        }
        return DEFAULT_TEXTURE;
    }

    // Choisit une texture aléatoire basée sur l'ID de l'entité (pour que ça reste constant)
    private Identifier getRandomColdTexture(int entityId) {
        int roll = Math.abs(entityId) % 100; // Utilise l'ID pour garder la texture stable
        if (roll < 5) return COLD_TEXTURES[0];   // 5%
        if (roll < 25) return COLD_TEXTURES[1];  // 20%
        if (roll < 75) return COLD_TEXTURES[2];  // 50%
        if (roll < 95) return COLD_TEXTURES[3];  // 20%
        return COLD_TEXTURES[4];                 // 5%
    }

    // Override render pour changer le modèle en fonction du biome
    @Override
    public void render(CowEntity entity, float yaw, float tickDelta, net.minecraft.client.util.math.MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        BlockPos pos = entity.getBlockPos();
        RegistryEntry<Biome> biomeEntry = entity.getWorld().getBiome(pos);
        Biome biome = biomeEntry.value();
        Identifier biomeId = entity.getWorld()
                .getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biome);

        // Change le modèle si la vache est dans un biome froid
        if (isColdBiome(biomeId)) {
            this.model = this.coldModel;
        } else {
            this.model = this.defaultModel;
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    // Vérifie si un biome est froid
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
}
