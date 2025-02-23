package com.github.goostytqc.client.render.entity;

import com.github.goostytqc.client.render.entity.model.CustomCowEntityModel;
import com.github.goostytqc.client.render.entity.model.ColdCowModel;
import com.github.goostytqc.client.render.entity.model.ModEntityModelLayers;
import com.github.goostytqc.server.ColdCowTracker;
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

import java.util.*;

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

    private final Map<UUID, Identifier> cachedTextures = new HashMap<>();

    public CustomCowEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW)), 0.7F);
        this.defaultModel = new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW));
        this.coldModel = new ColdCowModel<>(context.getPart(ModEntityModelLayers.COLD_COW));
    }

    @Override
    public Identifier getTexture(CowEntity entity) {
        UUID uuid = entity.getUuid();

        if (cachedTextures.containsKey(uuid)) {
            return cachedTextures.get(uuid);
        }

        boolean isCold = ColdCowTracker.isColdCow(uuid, checkColdBiome(entity));
        Identifier chosenTexture = isCold ? getRandomColdTexture(uuid) : DEFAULT_TEXTURE;

        cachedTextures.put(uuid, chosenTexture);
        return chosenTexture;
    }

    @Override
    public void render(CowEntity entity, float yaw, float tickDelta, net.minecraft.client.util.math.MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light) {
        UUID uuid = entity.getUuid();
        boolean isCold = ColdCowTracker.isColdCow(uuid, checkColdBiome(entity));

        this.model = isCold ? this.coldModel : this.defaultModel;

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
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
}
