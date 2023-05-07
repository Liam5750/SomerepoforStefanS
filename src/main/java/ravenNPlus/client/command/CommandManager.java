package ravenNPlus.client.command;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.commands.*;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.modules.HUD;
import ravenNPlus.client.utils.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandManager {

    public List<Command> commandList;
    public List<Command> sortedCommandList;

    public CommandManager() {
        this.commandList = new ArrayList<>();
        this.sortedCommandList = new ArrayList<>();
        this.addCommand(new Update());
        this.addCommand(new Help());
        this.addCommand(new SetKey());
        this.addCommand(new Discord());
        this.addCommand(new ConfigCommand());
        this.addCommand(new Cls());
        this.addCommand(new Cname());
        this.addCommand(new Debug());
        this.addCommand(new Duels());
        this.addCommand(new Nick());
        this.addCommand(new Ping());
        this.addCommand(new Uwu());
        this.addCommand(new Friends());
        this.addCommand(new VersionCommand());
        this.addCommand(new F3Name());
        this.addCommand(new Day());
        this.addCommand(new Night());
        this.addCommand(new GamemodeC());
        this.addCommand(new GamemodeS());
        this.addCommand(new ClearChat());
        this.addCommand(new Author());
        this.addCommand(new Color());
    }

    public void addCommand(Command c) { this.commandList.add(c); }
    public List<Command> getCommandList() { return this.commandList; }

    public Command getCommandByName(String name) {
        for (Command command : this.commandList) {
            if (command.getName().equalsIgnoreCase(name))
                return command;
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(name))
                    return command;
            }
        } return null;
    }

    public void noSuchCommand(String name) { CommandPrompt.print("Command '" + name + "' not found!"); }

    public void executeCommand(String commandName, String[] args) {
        Command command = Client.commandManager.getCommandByName(commandName);

        if (command == null) {
            this.noSuchCommand(commandName);
            return;
        }

        command.onCall(args);
    }

    public void sort() {
        if (HUD.alphabeticalSort.isToggled()) {
            this.sortedCommandList.sort(Comparator.comparing(Command::getName));
        } else {
            this.sortedCommandList.sort((o1, o2) -> Utils.mc.fontRendererObj.getStringWidth(o2.getName()) - Utils.mc.fontRendererObj.getStringWidth(o1.getName()));
        }
    }

}