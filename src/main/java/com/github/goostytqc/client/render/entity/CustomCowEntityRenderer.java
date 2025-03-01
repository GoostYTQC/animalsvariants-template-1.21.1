package com.github.goostytqc.client.render.entity;

import com.github.goostytqc.client.render.entity.model.CustomCowEntityModel;
import com.github.goostytqc.client.render.entity.model.ColdCowModel;
import com.github.goostytqc.client.render.entity.model.ModEntityModelLayers;
import com.github.goostytqc.client.render.entity.model.WarmCowModel;
import com.github.goostytqc.server.ColdCowTracker;
import com.github.goostytqc.server.WarmCowTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class CustomCowEntityRenderer extends MobEntityRenderer<CowEntity, EntityModel<CowEntity>> {
    private static final Identifier DEFAULT_TEXTURE = Identifier.of("animalsvariants", "textures/entity/cow/temperate_cow.png");

    private static final Identifier[] COLD_TEXTURES = {
            Identifier.of("animalsvariants", "textures/entity/cow/cold_black_cow.png"),
            Identifier.of("animalsvariants", "textures/entity/cow/cold_brown_cow.png"),
            Identifier.of("animalsvariants", "textures/entity/cow/cold_cow.png"),
            Identifier.of("animalsvariants", "textures/entity/cow/cold_light_brown_cow.png"),
            Identifier.of("animalsvariants", "textures/entity/cow/cold_white_cow.png")
    };

    private static final Identifier WARM_TEXTURES = Identifier.of("animalsvariants", "textures/entity/cow/warm_cow.png");

    private final CustomCowEntityModel<CowEntity> defaultModel;
    private final ColdCowModel<CowEntity> coldModel;
    private final WarmCowModel<CowEntity> warmModel;

    private final Map<UUID, Identifier> cachedTextures = new HashMap<>();
    private final Map<UUID, EntityModel<CowEntity>> cachedModels = new HashMap<>();

    public CustomCowEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW)), 0.7F);
        this.defaultModel = new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW));
        this.coldModel = new ColdCowModel<>(context.getPart(ModEntityModelLayers.COLD_COW));
        this.warmModel = new WarmCowModel<>(context.getPart(ModEntityModelLayers.WARM_COW));
    }

    @Override
    public void render(CowEntity entity, float yaw, float tickDelta, net.minecraft.client.util.math.MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        UUID uuid = entity.getUuid();

        // Check once and cache
        boolean isCold = ColdCowTracker.isColdCow(uuid, checkColdBiome(entity));
        boolean isWarm = WarmCowTracker.isWarmCow(uuid, checkWarmBiome(entity));

        if (!cachedModels.containsKey(uuid)) {
            if (isCold) {
                cachedModels.put(uuid, this.coldModel);
            } else if (isWarm) {
                cachedModels.put(uuid, this.warmModel);
            } else {
                cachedModels.put(uuid, this.defaultModel);
            }
        }

        this.model = cachedModels.get(uuid); // Use the cached model

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(CowEntity entity) {
        UUID uuid = entity.getUuid();

        if (cachedTextures.containsKey(uuid)) {
            return cachedTextures.get(uuid);
        }

        boolean isCold = ColdCowTracker.isColdCow(uuid, checkColdBiome(entity));
        boolean isWarm = WarmCowTracker.isWarmCow(uuid, checkWarmBiome(entity));

        Identifier chosenTexture;
        if (isCold) {
            chosenTexture = getRandomColdTexture(uuid);
        } else if (isWarm) {
            chosenTexture = WARM_TEXTURES;
        } else {
            chosenTexture = DEFAULT_TEXTURE;
        }

        cachedTextures.put(uuid, chosenTexture);
        return chosenTexture;
    }

    private Identifier getRandomColdTexture(UUID uuid) {
        int seed = uuid.hashCode();
        Random seededRandom = new Random(seed);
        int roll = seededRandom.nextInt(100);

        if (roll < 5) return COLD_TEXTURES[0];
        if (roll < 25) return COLD_TEXTURES[1];
        if (roll < 75) return COLD_TEXTURES[2];
        if (roll < 95) return COLD_TEXTURES[3];
        return COLD_TEXTURES[4];
    }

    private boolean checkColdBiome(CowEntity entity) {
        BlockPos pos = entity.getBlockPos();
        RegistryEntry<Biome> biomeEntry = entity.getWorld().getBiome(pos);
        Biome biome = biomeEntry.value();
        Identifier biomeId = entity.getWorld()
                .getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biome);

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

    private boolean checkWarmBiome(CowEntity entity) {
        BlockPos pos = entity.getBlockPos();
        RegistryEntry<Biome> biomeEntry = entity.getWorld().getBiome(pos);
        Biome biome = biomeEntry.value();
        Identifier biomeId = entity.getWorld()
                .getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biome);

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
