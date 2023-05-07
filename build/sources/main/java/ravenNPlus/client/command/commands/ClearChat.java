package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;

public class ClearChat extends Command { public ClearChat() { super("clearchat", "cleares the chat", 0,0, new String[] {}, new String[] {"clearchat"}); }

    @Override
    public void onCall(String[] args) {
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage(); Command.addChatMessage();
        Command.addChatMessage("Chat Cleared.");
    }

}