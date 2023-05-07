package ravenNPlus.client.utils.packet.server;

import ravenNPlus.client.utils.packet.Packet;

public class PacketTime extends Packet {
    public long totalWorldTime, worldTime;

    public PacketTime() { }

    public PacketTime(long totalWorldTime, long worldTime) {
        this.totalWorldTime = totalWorldTime; this.worldTime = worldTime;
    }

    public long getTotalWorldTime() { return this.totalWorldTime; }
    public void setTotalWorldTime(long totalWorldTime) { this.totalWorldTime = totalWorldTime; }
    public long getWorldTime() { return this.worldTime; }
    public void setWorldTime(long worldTime) { this.worldTime = worldTime; }
}