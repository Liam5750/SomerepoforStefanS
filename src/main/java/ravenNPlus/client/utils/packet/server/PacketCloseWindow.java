package ravenNPlus.client.utils.packet.server;

import ravenNPlus.client.utils.packet.Packet;

public class PacketCloseWindow extends Packet {
    public int windowId;
    public PacketCloseWindow() {  }
    public PacketCloseWindow(int windowId) { this.windowId = windowId; }
    public int getWindowId() { return this.windowId; }
    public void setWindowId(int windowId) { this.windowId = windowId; }
}