package com.github.goostytqc.client.render.entity.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {
    public static final EntityModelLayer CUSTOM_COW = new EntityModelLayer(Identifier.of("animalsvariants", "custom_cow"), "main");
    public static final EntityModelLayer COLD_COW = new EntityModelLayer(Identifier.of("animalsvariants", "cold_cow"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(CUSTOM_COW, CustomCowEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.COLD_COW, ColdCowModel::getTexturedModelData);
    }
}
