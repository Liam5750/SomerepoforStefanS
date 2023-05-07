package ravenNPlus.client.module.modules.minigames.sumo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.profile.UUID;
import ravenNPlus.client.utils.profile.PlayerProfile;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.utils.Utils.Profiles.DuelsStatsMode;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SumoStats extends Module {

    public SliderSetting ws, wlr;
    private final List<String> queue = new ArrayList<>();

    public SumoStats() {
        super("Sumo Stats", ModuleCategory.minigame, "Shows sumo stats");
        this.addSetting(wlr = new SliderSetting("WLR", 2, 0, 10, 0.1));
        this.addSetting(ws = new SliderSetting("WS", 4, 0, 30, 1));
    }

    public void onEnable() {
        if (mc.thePlayer != null) {
        } else {
            this.disable();
        }
    }

    public void onDisable() {
        this.queue.clear();
    }

    public void update() {
        if (!this.isDuel()) return;

        // Thanks to https://github.com/Scherso for the code from https://github.com/Scherso/Seraph
        for (ScorePlayerTeam team : Minecraft.getMinecraft().theWorld.getScoreboard().getTeams()) {
            for (String playerName : team.getMembershipCollection()) {
                if (!queue.contains(playerName) && team.getColorPrefix().equals("§7§k") && !playerName.equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayNameString())) {
                    this.queue.add(playerName);
                    Client.getExecutor().execute(() -> {
                        String id = getPlayerUUID(playerName);
                        if (!id.isEmpty()) {
                            getAndDisplayStatsForPlayer(id, playerName);
                        }
                    });
                }
            }
        }
    }

    private void getAndDisplayStatsForPlayer(String uuid, String playerName) {
        if (Utils.URLS.hypixelApiKey.isEmpty()) {
            Utils.Player.sendMessageToSelf("&cAPI Key is empty!");
        } else {
            Utils.Profiles.DuelsStatsMode dm = (Utils.Profiles.DuelsStatsMode) DuelsStatsMode.SUMO;
            Client.getExecutor().execute(() -> {
                PlayerProfile playerProfile = new PlayerProfile(new UUID(uuid), (Utils.Profiles.DuelsStatsMode) DuelsStatsMode.SUMO);
                playerProfile.populateStats();

                if (!playerProfile.isPlayer) return;

                if (playerProfile.nicked) {
                    Utils.Player.sendMessageToSelf("&3" + playerName + " " + "&eis nicked!");
                    return;
                }

                double wlr = playerProfile.losses != 0 ? Utils.Java.round((double) playerProfile.wins / (double) playerProfile.losses, 2) : (double) playerProfile.wins;
                Utils.Player.sendMessageToSelf("&7&m-------------------------");
                if (dm != Utils.Profiles.DuelsStatsMode.OVERALL) {
                    Utils.Player.sendMessageToSelf("&e" + Utils.md + "&3" + dm.name());
                }

                Utils.Player.sendMessageToSelf("&eOpponent: &3" + playerName);
                Utils.Player.sendMessageToSelf("&eWins: &3" + playerProfile.wins);
                Utils.Player.sendMessageToSelf("&eLosses: &3" + playerProfile.losses);
                Utils.Player.sendMessageToSelf("&eWLR: &3" + wlr);
                Utils.Player.sendMessageToSelf("&eWS: &3" + playerProfile.winStreak);
                Utils.Player.sendMessageToSelf("&7&m-------------------------");
                if (wlr > this.wlr.getValue() || playerProfile.winStreak > this.ws.getValue()) {
                    SumoBot sb = (SumoBot) Client.moduleManager.getModuleByName("Sumo Bot");
                    sb.reQueue();
                }
            });
        }
    }

    private boolean isDuel() {
        if (Utils.Client.isHyp()) {
            int l = 0;

            for (String s : Utils.Client.getPlayersFromScoreboard()) {
                if (s.contains("Map:")) {
                    ++l;
                } else if (s.contains("Players:") && s.contains("/2")) {
                    ++l;
                }
            }

            return l == 2;
        } else {
            return false;
        }
    }

    private String getPlayerUUID(String username) {
        String playerUUID = "";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(String.format("https://api.mojang.com/users/profiles/minecraft/%s", username));
            try (InputStream is = client.execute(request).getEntity().getContent()) {
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
                playerUUID = object.get("id").getAsString();

            } catch (NullPointerException ex) {
                System.out.println("Null or invalid player provided by the server.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return playerUUID;
    }

}