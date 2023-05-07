package ravenNPlus.client.utils.packet.server;

import ravenNPlus.client.utils.packet.Packet;

public class PacketVelocity extends Packet {
    public int entityID, motionX, motionY, motionZ;

    public PacketVelocity() { }

    public PacketVelocity(int entityID, int motionX, int motionY, int motionZ) {
        this.entityID = entityID; this.motionX = motionX; this.motionY = motionY; this.motionZ = motionZ;
    }

    public int getEntityID() { return this.entityID; }
    public void setEntityID(int entityID) { this.entityID = entityID; }
    public int getMotionX() { return this.motionX; }
    public void setMotionX(int motionX) { this.motionX = motionX; }
    public int getMotionY() { return this.motionY; }
    public void setMotionY(int motionY) { this.motionY = motionY; }
    public int getMotionZ() { return this.motionZ; }
    public void setMotionZ(int motionZ) { this.motionZ = motionZ; }
}