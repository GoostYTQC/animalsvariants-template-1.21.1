package com.github.goostytqc; // Change this to match your package

import com.github.goostytqc.client.render.entity.model.ModEntityModelLayers;
import com.github.goostytqc.client.render.entity.CustomCowEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;

public class MobVariantsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register Model Layers
        ModEntityModelLayers.register();

        // Register Entity Renderer
        EntityRendererRegistry.register(EntityType.COW, CustomCowEntityRenderer::new);
    }
}
