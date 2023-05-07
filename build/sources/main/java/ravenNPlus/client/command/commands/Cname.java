package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import ravenNPlus.client.module.modules.other.NameHider;
import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;

public class Cname extends Command {

    public Cname() {
        super("cname", "Hides your name client-side", 1, 1, new String[] {"New name"}, new String[] {"cn", "changename"});
    }

    @Override
    public void onCall(String[] args) {
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }

        if (args.toString().equals(" ")) {
            this.incorrectArgs();
            return;
        }

        NameHider.name = args[0];
        CommandPrompt.print("Nick has been set to: " +  NameHider.name);
    }

}