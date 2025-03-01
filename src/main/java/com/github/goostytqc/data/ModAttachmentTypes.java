package com.github.goostytqc.data;


import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;

public class ModAttachmentTypes {
    public static final AttachmentType<ModCowVariantData> COW_VARIANT = AttachmentRegistry.create(
            Identifier.of("animalsvariants", "cow_variant"),
            builder -> builder
                    .initializer(() -> ModCowVariantData.DEFAULT) // Default value
                    .persistent(ModCowVariantData.CODEC) // Save/load
                    .syncWith(ModCowVariantData.PACKET_CODEC, AttachmentSyncPredicate.all()) // Sync to all players
    );

    public static void init() {
        // This ensures our attachment type is registered when the mod initializes
    }
}