package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import net.minecraft.client.Minecraft;

public class GamemodeC extends Command {
    public GamemodeC() {
        super("gmc", "Makes you gamemode creative", 0, 0, new String[]{}, new String[]{"gmc"});
    }

    @Override
    public void onCall(String[] args) {
        if(Minecraft.getMinecraft().isSingleplayer()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/gamemode creative");
        } else {
            Command.addChatMessage("This is only available in Single-player");
        }
    }

}