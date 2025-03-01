package com.github.goostytqc.client.render.entity;

import com.github.goostytqc.client.render.entity.model.ColdCowModel;
import com.github.goostytqc.client.render.entity.model.CustomCowEntityModel;
import com.github.goostytqc.client.render.entity.model.ModEntityModelLayers;
import com.github.goostytqc.client.render.entity.model.WarmCowModel;
import com.github.goostytqc.data.ModAttachmentTypes;
import com.github.goostytqc.data.ModCowVariantData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;

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

    private static final Identifier WARM_TEXTURE = Identifier.of("animalsvariants", "textures/entity/cow/warm_cow.png");

    private final CustomCowEntityModel<CowEntity> defaultModel;
    private final ColdCowModel<CowEntity> coldModel;
    private final WarmCowModel<CowEntity> warmModel;

    public CustomCowEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW)), 0.7F);
        this.defaultModel = new CustomCowEntityModel<>(context.getPart(ModEntityModelLayers.CUSTOM_COW));
        this.coldModel = new ColdCowModel<>(context.getPart(ModEntityModelLayers.COLD_COW));
        this.warmModel = new WarmCowModel<>(context.getPart(ModEntityModelLayers.WARM_COW));
    }

    @Override
    public void render(CowEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Ensure the cow keeps its correct model on game load
        ModCowVariantData data = entity.getAttached(ModAttachmentTypes.COW_VARIANT);
        if (data == null) {
            data = ModCowVariantData.DEFAULT;
        }

        String variant = data.variant();

        // Set the model every frame to ensure it's correct
        if ("cold".equals(variant)) {
            this.model = this.coldModel;
        } else if ("warm".equals(variant)) {
            this.model = this.warmModel;
        } else {
            this.model = this.defaultModel;
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(CowEntity entity) {
        ModCowVariantData data = entity.getAttached(ModAttachmentTypes.COW_VARIANT);
        if (data == null) {
            data = ModCowVariantData.DEFAULT;
        }

        String variant = data.variant();

        if ("cold".equals(variant)) {
            return getRandomColdTexture(entity.getUuid());
        } else if ("warm".equals(variant)) {
            return WARM_TEXTURE;
        } else {
            return DEFAULT_TEXTURE;
        }
    }

    private Identifier getRandomColdTexture(UUID uuid) {
        int seed = uuid.hashCode();
        Random random = new Random(seed);
        int roll = random.nextInt(100);

        if (roll < 5) return COLD_TEXTURES[0]; // 5% Black
        if (roll < 25) return COLD_TEXTURES[1]; // 20% Brown
        if (roll < 75) return COLD_TEXTURES[2]; // 50% Default cold cow
        if (roll < 95) return COLD_TEXTURES[3]; // 20% Light Brown
        return COLD_TEXTURES[4]; // 5% White
    }
}
