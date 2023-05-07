package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;

public class Cls extends Command {
    public Cls() {
        super("cls", "Clears the Command Prompt", 0,0, new String[] {}, new String[] {"clr", "cls"});
    }

    @Override
    public void onCall(String[] args) {
        CommandPrompt.clear();
    }

}