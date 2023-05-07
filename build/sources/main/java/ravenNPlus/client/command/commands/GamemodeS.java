package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import net.minecraft.client.Minecraft;

public class GamemodeS extends Command {
    public GamemodeS() {
        super("gms", "Makes you gamemode survival", 0,0, new String[] {}, new String[] {"gms"});
    }

    @Override
    public void onCall(String[] args) {
        if(Minecraft.getMinecraft().isSingleplayer()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/gamemode survival");
        } else {
            Command.addChatMessage("This is only available in Single-player");
        }
    }

}