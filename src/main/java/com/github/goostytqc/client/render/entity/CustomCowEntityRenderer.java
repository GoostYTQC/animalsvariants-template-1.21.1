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

@Environment(EnvType.CLIENT)
public class CustomCowEntityRenderer extends MobEntityRenderer<CowEntity, EntityModel<CowEntity>> {
    private static final Identifier DEFAULT_TEXTURE = Identifier.of("animalsvariants", "textures/entity/cow/temperate_cow.png");
    private static final Identifier COLD_TEXTURE = Identifier.of("animalsvariants", "textures/entity/cow/cold_cow.png");

    private final CustomCowEntityModel<CowEntity> defaultModel;
    private final ColdCowModel<CowEntity> coldModel;

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
            return COLD_TEXTURE;
        }
        return DEFAULT_TEXTURE;
    }

    // Override render to switch models based on biome
    @Override
    public void render(CowEntity entity, float yaw, float tickDelta, net.minecraft.client.util.math.MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        BlockPos pos = entity.getBlockPos();
        RegistryEntry<Biome> biomeEntry = entity.getWorld().getBiome(pos);
        Biome biome = biomeEntry.value();
        Identifier biomeId = entity.getWorld()
                .getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biome);

        // Switch model before rendering
        if (isColdBiome(biomeId)) {
            this.model = this.coldModel;
        } else {
            this.model = this.defaultModel;
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    // Utility method to check for cold biomes
    private boolean isColdBiome(Identifier biomeId) {
        return biomeId != null && (
                biomeId.equals(Identifier.of("minecraft", "old_growth_pine_taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "old_growth_spruce_taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "snowy_taiga")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_hills")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_gravelly_hills")) ||
                        biomeId.equals(Identifier.of("minecraft", "windswept_forest"))
        );
    }
}
