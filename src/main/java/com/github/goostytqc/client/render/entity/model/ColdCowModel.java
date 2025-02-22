package com.github.goostytqc.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class ColdCowModel<T extends Entity> extends CowEntityModel<T> {
    public ColdCowModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -4.0F, -6.0F, 8, 8, 6)
                        .uv(8, 32).cuboid(-3.0F, 0.99F, -7.0F, 6, 3, 2),
                ModelTransform.pivot(0.0F, 4.0F, -8.0F));

        ModelPartData head = root.getChild(EntityModelPartNames.HEAD);
        head.addChild("right_horn", ModelPartBuilder.create()
                        .uv(0, 32).cuboid(-1.5F, -4.5F, -0.5F, 2, 6, 2),
                ModelTransform.of(-4.5F, -2.5F, -3.5F, 1.5708F, 0.0F, 0.0F));
        head.addChild("left_horn", ModelPartBuilder.create()
                        .uv(0, 32).cuboid(-1.5F, -3.0F, -0.5F, 2, 6, 2),
                ModelTransform.of(5.5F, -2.5F, -5.0F, 1.5708F, 0.0F, 0.0F));

        root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
                        .uv(20, 32).cuboid(-6.0F, -10.0F, -7.0F, 12, 18, 10, new Dilation(0.5F))
                        .uv(18, 4).cuboid(-6.0F, -10.0F, -7.0F, 12, 18, 10)
                        .uv(52, 0).cuboid(-2.0F, 2.0F, -8.0F, 4, 6, 1),
                ModelTransform.of(0.0F, 5.0F, 2.0F, (float) Math.PI / 2, 0.0F, 0.0F));

        ModelPartBuilder legBuilder = ModelPartBuilder.create()
                .uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4, 12, 4);
        root.addChild(EntityModelPartNames.RIGHT_HIND_LEG, legBuilder, ModelTransform.pivot(-4.0F, 12.0F, 7.0F));
        root.addChild(EntityModelPartNames.LEFT_HIND_LEG, legBuilder, ModelTransform.pivot(4.0F, 12.0F, 7.0F));
        root.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, legBuilder, ModelTransform.pivot(-4.0F, 12.0F, -6.0F));
        root.addChild(EntityModelPartNames.LEFT_FRONT_LEG, legBuilder, ModelTransform.pivot(4.0F, 12.0F, -6.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }
}
