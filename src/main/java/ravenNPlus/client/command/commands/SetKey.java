package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.Utils;

public class SetKey extends Command {
    public SetKey() {
        super("setkey", "Sets Hypixel's API key. To get a new key, run `/api new`", 2, 2, new String[] {"key"}, new String[] {"apikey"});
    }

    @Override
    public void onCall(String[] args) {
        if(args.length == 0) {
            this.incorrectArgs();
            return;
        }

        CommandPrompt.print("Setting...");
        String n;
        n = args[0];
        Client.getExecutor().execute(() -> {
            if (Utils.URLS.isHypixelKeyValid(n)) {
                Utils.URLS.hypixelApiKey = n;
                CommandPrompt.print("Success!");
                Client.clientConfig.saveConfig();
            } else {
                CommandPrompt.print("Invalid key.");
            }

        });

    }

}