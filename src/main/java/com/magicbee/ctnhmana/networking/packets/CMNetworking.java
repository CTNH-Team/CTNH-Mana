package com.magicbee.ctnhmana.networking.packets;

import static com.lowdragmc.lowdraglib.networking.LDLNetworking.NETWORK;

public class CMNetworking {

    public static void init() {
        NETWORK.registerC2S(CaduceusPacket.class);
        NETWORK.registerC2S(IndexFortunaPacket.class);
        NETWORK.registerS2C(IndexTargetParticlePacket.class);
        NETWORK.registerS2C(IndexTargetBlockPacket.class);
        NETWORK.registerS2C(AntagonismPacket.class);
        NETWORK.registerS2C(ZenithInvadePacket.class);
    }
}
