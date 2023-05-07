package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import net.minecraft.client.Minecraft;

public class Night extends Command {
    public Night() {
        super("night", "Makes night", 0,0, new String[] {}, new String[] {"night"});
    }

    @Override
    public void onCall(String[] args) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/time set night");
        } else {
            Command.addChatMessage("This is only available in Singleplayer");
        }
    }

}