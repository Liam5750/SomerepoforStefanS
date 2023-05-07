package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.version.Version;
import static ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt.print;

public class VersionCommand extends Command {

    public VersionCommand() {
        super("version", "tells you what build of "+ Client.name+" you are using", 0, 0, new String[] {}, new String[] {"v", "ver", "which", "build", "b"});
    }

    @Override
    public void onCall(String[] args) {
        Version clientVersion = Client.versionManager.getClientVersion();
        Version latestVersion = Client.versionManager.getLatestVersion();

        print("Your build: " + clientVersion);
        print("Latest version: " + latestVersion);
    }

}