package ravenNPlus.client.module.modules.client;

import io.netty.buffer.Unpooled;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ClientNameSpoof extends Module {

    public static DescriptionSetting desc;
    public static String newName = "Lunar-Client";

    public ClientNameSpoof() {
        super("ClientNameSpoofer", ModuleCategory.client, "Edit your F3 Client name");
        this.addSetting(desc = new DescriptionSetting(Utils.Java.capitalizeWord("command") + ": f3name [name]"));
    }

    @Override
    public void onEnable() {
        this.sendPacketNetHandler(new C17PacketCustomPayload(newName, createPacketBuffer(newName, false)));
    }

    private PacketBuffer createPacketBuffer(final String data, final boolean string) {
        if (string)
            return new PacketBuffer(Unpooled.buffer()).writeString(data);
        else
            return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
    }

}