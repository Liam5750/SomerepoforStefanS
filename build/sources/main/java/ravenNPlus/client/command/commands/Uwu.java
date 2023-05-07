package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;

public class Uwu extends Command {

    private static boolean u;
    public Uwu() {
        super("uwu", "hevex/blowsy added this lol", 0, 0,  new String[] {},  new String[] {"hevex", "blowsy", "weeb", "torture", "noplsno", "gay"});
        u = false;
    }

    @Override
    public void onCall(String[] args) {
        if (u) {
            return;
        }

        Client.getExecutor().execute(() -> {
            u = true;

            for(int i = 0; i < 4; ++i) {
                if (i == 0) {
                    CommandPrompt.print("nya");
                } else if (i == 1) {
                    CommandPrompt.print("ichi ni san");
                } else if (i == 2) {
                    CommandPrompt.print("nya");
                } else {
                    CommandPrompt.print("arigatou!");
                }

                try {
                    Thread.sleep(500L);
                } catch (InterruptedException ignored) {
                }
            }

            u = false;
        });
    }

}