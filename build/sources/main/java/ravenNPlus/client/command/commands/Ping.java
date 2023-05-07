package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import ravenNPlus.client.utils.ChatHelper;

public class Ping extends Command {
    public Ping() {
        super("ping", "Gets your ping", 0, 0, new String[] {}, new String[] {"p", "connection", "lag"});
    }

    @Override
    public void onCall(String[] args) {
        ChatHelper.checkPing();
    }

}