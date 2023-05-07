package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.config.Config;
import ravenNPlus.client.main.Client;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config", "Manages configs", 0, 3, new String[] {"load,save,list,remove,clear", "config's name"}, new String[] {"cfg", "profiles", "profile", "config"});
    }

    @Override
    public void onCall(String[] args) {
        if(Client.clientConfig != null) {
            Client.clientConfig.saveConfig();
            Client.configManager.save(); // as now configs only save upon exiting the gui, this is required
        }

        if (args.length == 0) {
            CommandPrompt.print("Current config: " + Client.configManager.getConfig().getName());
        }

        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                this.listConfigs();
            } else if(args[0].equalsIgnoreCase("clear")) {
                CommandPrompt.print("Are you sure you want to reset the config " + Client.configManager.getConfig().getName() + "? If so, run \"config clear confirm\"");
            }
            else {
                this.incorrectArgs();
            }
        }

        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list")) {
                this.listConfigs();
            }

            else if (args[0].equalsIgnoreCase("load")) {
                boolean found = false;
                for (Config config : Client.configManager.getConfigs()) {
                    if (config.getName().equalsIgnoreCase(args[1])) {
                        found = true;
                        CommandPrompt.print("Found config with the name " + args[1] + "!");
                        Client.configManager.setConfig(config);
                        CommandPrompt.print("Loaded config!");
                    }
                }

                if (!found) {
                    CommandPrompt.print("Unable to find a config with the name " + args[1]);
                }

            }
            else if (args[0].equalsIgnoreCase("save")) {
                CommandPrompt.print("Saving...");
                Client.configManager.copyConfig(Client.configManager.getConfig(), args[1] + ".RavenNPlus");
                CommandPrompt.print("Saved as \"" + args[1] + "\"! To load the config, run \"config load " + args[1] + "\"");

            }
            else if (args[0].equalsIgnoreCase("remove")) {
                boolean found = false;
                CommandPrompt.print("Removing " + args[1] + "...");
                for(Config config : Client.configManager.getConfigs()) {
                    if(config.getName().equalsIgnoreCase(args[1])) {
                        Client.configManager.deleteConfig(config);
                        found = true;
                        CommandPrompt.print("Removed " + args[1] + " successfully! Current config: " + Client.configManager.getConfig().getName());
                        break;
                    }
                }

                if(!found) {
                    CommandPrompt.print("Failed to delete " + args[1] + ". Unable to find a config with the name or an error occurred during removal");
                }

            } else if(args[0].equalsIgnoreCase("clear")) {
                if(args[1].equalsIgnoreCase("confirm")) {
                    Client.configManager.resetConfig();
                    Client.configManager.save();
                    CommandPrompt.print("Cleared config!");
                } else {
                    CommandPrompt.print("It is confirm, not " + args[1]);
                }

            }else {
                this.incorrectArgs();
            }
        }
    }

    public void listConfigs() {
       CommandPrompt.print("Available configs: ");
        for (Config config : Client.configManager.getConfigs()) {
            if (Client.configManager.getConfig().getName().equals(config.getName()))
                CommandPrompt.print("Current config: " + config.getName());
            else
                CommandPrompt.print(config.getName());
        }
    }

}