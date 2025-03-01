package com.github.goostytqc.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class WarmCowModel<T extends Entity> extends CowEntityModel<T> {
    public WarmCowModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Head with horns
        root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F)
                        .uv(0, 32).cuboid(-3.0F, 0.99F, -7.0F, 6.0F, 3.0F, 2.0F)
                        .uv(27, 0).cuboid(-8.0F, -3.0F, -5.0F, 4.0F, 2.0F, 2.0F)
                        .uv(39, 0).cuboid(-8.0F, -5.0F, -5.0F, 2.0F, 2.0F, 2.0F)
                        .uv(27, 0).mirrored().cuboid(4.0F, -3.0F, -5.0F, 4.0F, 2.0F, 2.0F).mirrored(false)
                        .uv(39, 0).mirrored().cuboid(6.0F, -5.0F, -5.0F, 2.0F, 2.0F, 2.0F).mirrored(false)
                        .uv(22, 0).cuboid("right_horn", -5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F)
                        .uv(22, 0).cuboid("left_horn", 4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F),
                ModelTransform.pivot(0.0F, 4.0F, -8.0F));

        // Body
        root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
                        .uv(18, 4).cuboid(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F)
                        .uv(52, 0).cuboid(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F),
                ModelTransform.of(0.0F, 5.0F, 2.0F, (float) Math.PI / 2, 0.0F, 0.0F));

        // Legs
        ModelPartBuilder legRight = ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);
        ModelPartBuilder legLeft = ModelPartBuilder.create().mirrored().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F);

        root.addChild(EntityModelPartNames.RIGHT_HIND_LEG, legRight, ModelTransform.pivot(-4.0F, 12.0F, 7.0F));
        root.addChild(EntityModelPartNames.LEFT_HIND_LEG, legLeft, ModelTransform.pivot(4.0F, 12.0F, 7.0F));
        root.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, legRight, ModelTransform.pivot(-4.0F, 12.0F, -6.0F));
        root.addChild(EntityModelPartNames.LEFT_FRONT_LEG, legLeft, ModelTransform.pivot(4.0F, 12.0F, -6.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

}
