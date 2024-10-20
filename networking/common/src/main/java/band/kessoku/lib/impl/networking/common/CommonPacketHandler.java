package band.kessoku.lib.impl.networking.common;

public interface CommonPacketHandler {
    void kessokulib$onCommonVersionPacket(int negotiatedVersion);

    void kessokulib$onCommonRegisterPacket(CommonRegisterPayload payload);

    CommonRegisterPayload kessokulib$createRegisterPayload();

    int kessokulib$getNegotiatedVersion();
}
