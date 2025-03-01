package com.github.goostytqc.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import java.util.Optional;

public record ModCowVariantData(String variant) {
    // Codec for saving/loading the data (NBT, JSON, etc.)
    public static final Codec<ModCowVariantData> CODEC = Codec.STRING.xmap(ModCowVariantData::new, ModCowVariantData::variant);

    // Packet codec for syncing over the network
    public static final PacketCodec<ByteBuf, ModCowVariantData> PACKET_CODEC = PacketCodecs.codec(CODEC);

    // Default value (cows start with this if no biome is assigned)
    public static final ModCowVariantData DEFAULT = new ModCowVariantData("temperate");
}
