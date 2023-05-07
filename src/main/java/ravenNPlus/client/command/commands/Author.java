package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.Utils;

public class Author extends Command {
    public Author() {
        super("author", "Shows you the author", 0, 3, new String[] {"author"}, new String[] {"author", "a", "b", "c", "d", "e", "t"});
    }

    @Override
    public void onCall(String[] args) {
        boolean opened = false;
        boolean copied = false;
        boolean showed = false;
        int argCurrent = 0;
        if(args.length == 0) {
            CommandPrompt.print("3 Opening: " + Client.discord);
            Utils.Client.openWebpage(Client.discord);
            opened = true;
            return;
        }

        for (String argument : args) {
            if(argument.equalsIgnoreCase("copy")) {
                if (!copied) {
                    Utils.Client.copyToClipboard(Client.discord);
                    copied = true;
                    CommandPrompt.print("Copied: " + Client.discord + " to clipboard!");
                }
            } else if(argument.equalsIgnoreCase("open")) {
                if (!opened) {
                    Utils.Client.openWebpage(Client.discord);
                    opened = true;
                    CommandPrompt.print("Opened invite link!");
                }
            } else if(argument.equalsIgnoreCase("print")) {
                if (!showed) {
                    CommandPrompt.print(Client.discord);
                    showed = true;
                } } else {
                if (argCurrent != 0)
                    this.incorrectArgs();
            }
            argCurrent++;
        }
    }

}