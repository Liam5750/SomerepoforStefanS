package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.version.Version;
import java.net.MalformedURLException;
import java.net.URL;

public class Update extends Command {

    public Update() {
        super("update", "Assists you in updating the client", 0, 0, new String[] {}, new String[] {"upgrade", "updating"});
    }

    @Override
    public void onCall(String[] args) {
        Version clientVersion = Client.versionManager.getClientVersion();
        Version latestVersion = Client.versionManager.getLatestVersion();

        if (latestVersion.isNewerThan(clientVersion)) {
            CommandPrompt.print("Opening page...");
            URL url = null;
            try {
                url = new URL(Client.sourceLocation);
                Utils.Client.openWebpage(url);
                Utils.Client.openWebpage(new URL(Client.downloadLocation));
                CommandPrompt.print("Opened page successfully!");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                CommandPrompt.print("Failed to open page! Please report this bug in "+ Client.name+" discord!");

            }
        } else {
            CommandPrompt.print("No need to upgrade, You are on the latest build");
        }
    }

}