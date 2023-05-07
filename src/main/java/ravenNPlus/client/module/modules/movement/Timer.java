package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.clickgui.RavenNPlus.ClickGui;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;

public class Timer extends Module {

   public static SliderSetting speed;
   public static TickSetting strafe;

   public Timer() {
      super("Timer", ModuleCategory.movement, "Modifies your world timer");
      this.addSetting(speed = new SliderSetting("Speed", 1.0D, 0.5D, 2.5D, 0.01D));
      this.addSetting(strafe = new TickSetting("Strafe only", false));
   }

   public void update() {
      if (!(mc.currentScreen instanceof ClickGui)) {
         if (strafe.isToggled() && this.player().moveStrafing == 0.0F) {
            Utils.Client.resetTimer();
            return;
         }

         Utils.Client.getTimer().timerSpeed = speed.getValueToFloat();
      } else {
         Utils.Client.resetTimer();
      }
   }

   public void onDisable() {
      Utils.Client.resetTimer();
   }

}