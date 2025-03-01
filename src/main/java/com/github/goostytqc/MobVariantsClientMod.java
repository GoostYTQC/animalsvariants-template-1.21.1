package com.github.goostytqc;

import com.github.goostytqc.client.render.entity.model.ModEntityModelLayers;
import com.github.goostytqc.client.render.entity.CustomCowEntityRenderer;
import com.github.goostytqc.data.ModAttachmentTypes;
import com.github.goostytqc.server.CowDataStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;

public class MobVariantsClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // ✅ Load stored cow data on mod startup
        CowDataStorage.load();
        ModAttachmentTypes.init(); // ✅ Register attachment types early!

        // ✅ Register Model Layers
        ModEntityModelLayers.register();

        // ✅ Register Entity Renderer
        EntityRendererRegistry.register(EntityType.COW, CustomCowEntityRenderer::new);
    }
}
