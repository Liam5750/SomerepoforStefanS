package ravenNPlus.client.command.commands;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import ravenNPlus.client.command.Command;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.profile.PlayerProfile;

public class Duels extends Command {

    public Duels()  {
        super("duels", "Fetches a player's stats", 1, 2,  new String[] {"Player name", "overall/uhc/bridge/skywars/sumo/classic/op"},  new String[] {"d", "duel", "stat", "stats", "check"});
    }

    @Override
    public void onCall(String[] args) {
        if (Utils.URLS.hypixelApiKey.isEmpty()) {
            CommandPrompt.print("API Key is empty! Run \"setkey api_key\".");
            return;
        }
        if(args.length == 0) {
            this.incorrectArgs();
            return;
        }

        if(args.length == 1) {
            String n;
            n = args[0];
            CommandPrompt.print("Retrieving data...");
            Client.getExecutor().execute(() -> {
                PlayerProfile playerProfile = new PlayerProfile(n, Utils.Profiles.DuelsStatsMode.OVERALL);
                playerProfile.populateStats();
                if(!playerProfile.isPlayer) {
                    CommandPrompt.print(n + " does not exist");
                } else if (playerProfile.nicked) {
                    CommandPrompt.print(n + " is nicked");
                } else {
                    double wlr = playerProfile.losses != 0 ? Utils.Java.round((double)playerProfile.wins / (double)playerProfile.losses, 2) : (double)playerProfile.wins;
                    CommandPrompt.print(n + " overall stats:");
                    CommandPrompt.print("Wins: " + playerProfile.wins);
                    CommandPrompt.print("Losses: " + playerProfile.losses);
                    CommandPrompt.print("WLR: " + wlr);
                    CommandPrompt.print("Winstreak: " + playerProfile.winStreak);
                }
            });
        } else if (args.length == 2) {
            String stringGamemode = args[1];
            Utils.Profiles.DuelsStatsMode gameMode = null;
            for(Utils.Profiles.DuelsStatsMode mode : Utils.Profiles.DuelsStatsMode.values()) {
                if(String.valueOf(mode).equalsIgnoreCase(stringGamemode))
                    gameMode = mode;
            }

            if(gameMode == null) {
                CommandPrompt.print(stringGamemode + " is not a known gamemode. See \"help duels\" for a known list of gamemode");
            } else {
                String n;
                n = args[0];
                CommandPrompt.print("Retrieving data...");
                Utils.Profiles.DuelsStatsMode finalGameMode = gameMode;
                Client.getExecutor().execute(() -> {
                    PlayerProfile playerProfile = new PlayerProfile(n, finalGameMode);
                    playerProfile.populateStats();
                    if(!playerProfile.isPlayer) {
                        CommandPrompt.print(n + " does not exist");
                    } else if (playerProfile.nicked) {
                        CommandPrompt.print(n + " is nicked");
                    } else {
                        double wlr = playerProfile.losses != 0 ? Utils.Java.round((double)playerProfile.wins / (double)playerProfile.losses, 2) : (double)playerProfile.wins;
                        CommandPrompt.print(n + " " + finalGameMode + " stats:");
                        CommandPrompt.print("Wins: " + playerProfile.wins);
                        CommandPrompt.print("Losses: " + playerProfile.losses);
                        CommandPrompt.print("WLR: " + wlr);
                        CommandPrompt.print("Winstreak: " + playerProfile.winStreak);
                    }
                });
            }
        }
    }

}