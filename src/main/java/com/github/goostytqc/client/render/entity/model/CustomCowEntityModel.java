package com.github.goostytqc.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class CustomCowEntityModel<T extends Entity> extends CowEntityModel<T> {
    public CustomCowEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // Custom head with an extra box
        modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F) // Main head
                        .uv(0, 32)
                        .cuboid(-3.0F, 0.99F, -7.0F, 6.0F, 3.0F, 2.0F) // Extra snout part
                        .uv(22, 0)
                        .cuboid(EntityModelPartNames.RIGHT_HORN, -5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F)
                        .uv(22, 0)
                        .cuboid(EntityModelPartNames.LEFT_HORN, 4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F),
                ModelTransform.pivot(0.0F, 4.0F, -8.0F)
        );

        // Custom body
        modelPartData.addChild(
                EntityModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(18, 4)
                        .cuboid(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                        .uv(52, 0)
                        .cuboid(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F),
                ModelTransform.of(0.0F, 5.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F)
        );

        // Custom legs
        ModelPartBuilder legBuilder = ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, legBuilder, ModelTransform.pivot(-4.0F, 12.0F, 7.0F));
        modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, legBuilder, ModelTransform.pivot(4.0F, 12.0F, 7.0F));
        modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, legBuilder, ModelTransform.pivot(-4.0F, 12.0F, -6.0F));
        modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, legBuilder, ModelTransform.pivot(4.0F, 12.0F, -6.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }
}
