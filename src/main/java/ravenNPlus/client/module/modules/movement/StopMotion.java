package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;

public class StopMotion extends Module {

   public static TickSetting xStop, yStop, zStop;

   public StopMotion() {
      super("Stop Motion", ModuleCategory.movement, "Stop your motion");
      this.addSetting(xStop = new TickSetting("Stop X", true));
      this.addSetting(yStop = new TickSetting("Stop Y", true));
      this.addSetting(zStop = new TickSetting("Stop Z", true));
   }

   public void onEnable() {
      if (!this.inGame()) {
         this.disable();
         return;
      }

      if (xStop.isToggled())
         this.player().motionX = 0;

      if (yStop.isToggled())
         this.player().motionY = 0;

      if (zStop.isToggled())
         this.player().motionZ = 0;

      this.disable();
   }

}