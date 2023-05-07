package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;

public class Debug extends Command {

    public Debug() {
        super("debug", "Toggles "+ Client.name+" debugger", 0, 0,  new String[] {},  new String[] {"dbg", "log"});
    }

    @Override
    public void onCall(String[] args) {
        Client.debugger = !Client.debugger;
        CommandPrompt.print((Client.debugger ? "Enabled" : "Disabled") + " debugging.");
    }

}