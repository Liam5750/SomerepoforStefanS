package ravenNPlus.client.utils.packet.server;

import ravenNPlus.client.utils.packet.Packet;

public class PacketPosition extends Packet {
    public double x, y, z;
    public float yaw, pitch;
    public short flags;

    public PacketPosition() {  }

    public PacketPosition(double x, double y, double z, float yaw, float pitch, short flags) {
        this.x = x; this.y = y; this.z = z; this.yaw = yaw; this.pitch = pitch; this.flags = flags;
    }

    public short getFlags() { return this.flags; }
    public void setFlags(short flags) { this.flags = flags; }
    public double getX() { return this.x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return this.y; }
    public void setY(double y) { this.y = y; }
    public double getZ() { return this.z; }
    public void setZ(double z) { this.z = z; }
    public float getYaw() { return this.yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }
    public float getPitch() { return this.pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }
}