package ravenNPlus.client.module.modules.client;

import com.google.gson.JsonObject;
import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.Setting;
import ravenNPlus.client.clickgui.RavenNPlus.ClickGui;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandPrompt extends Module {

   public static boolean visible = false, b = false;
   public static Timer animation;
   public static SliderSetting opacity;

   public CommandPrompt() {
      super("Command Prompt", ModuleCategory.client, "Command Manager");
      withEnabled(true);
      this.addSetting(opacity = new SliderSetting("CommandPromt background opacity", 60, 0, 255, 1));
   }

   public void onEnable() {
      Client.clickGui.commandPrompt.show();
      //keystrokesmod.client.clickgui.raven.CommandLine.setccs();
      //visible = true;
      //b = false;
      (animation = new Timer(500.0F)).start();
   }

   @SubscribeEvent
   public void tick(TickEvent.PlayerTickEvent e) {
      if (this.inGame() && enabled && mc.currentScreen instanceof ClickGui && Client.clickGui.commandPrompt.hidden())
         Client.clickGui.commandPrompt.show();
   }

   public void onDisable() {
      Client.clickGui.commandPrompt.hide();
      if (animation != null) {
         animation.start();
      }
   }

   @Override
   public void applyConfigFromJson(JsonObject data) {
      try {
         this.keycode = data.get("keycode").getAsInt();
         // no need to set this to disabled
         JsonObject settingsData = data.get("settings").getAsJsonObject();
         for (Setting setting : getSettings()) {
            if (settingsData.has(setting.getName())) {
               setting.applyConfigFromJson(
                       settingsData.get(setting.getName()).getAsJsonObject()
               );
            }
         }
      } catch (NullPointerException ignored) {
      }
   }

   @Override
   public void resetToDefaults() {
      this.keycode = defualtKeyCode;
      for (Setting setting : this.settings) {
         setting.resetToDefaults();
      }
   }

}