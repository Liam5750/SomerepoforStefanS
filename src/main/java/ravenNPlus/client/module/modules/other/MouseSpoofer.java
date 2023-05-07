package ravenNPlus.client.module.modules.other;

import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.MouseManager;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MouseSpoofer extends Module {

   public static ModeSetting mode;
   public static SliderSetting delay;

   public MouseSpoofer() {
      super("MouseSpoof", ModuleCategory.other, "Spoofs your CPS");
      this.addSetting(mode = new ModeSetting("Mode", MouseSpoofer.modes.Both));
      this.addSetting(delay = new SliderSetting("Delay", 2, 0, 50, 1));
   }

   @SubscribeEvent
   public void e(TickEvent.RenderTickEvent e) {
      if (!this.inGame()) return;

      if (Timer.hasTimeElapsed(delay.getValueToLong(), true)) {

         if (mode.getMode() == MouseSpoofer.modes.Right) {
            MouseManager.addRightClick();
         }

         if (mode.getMode() == MouseSpoofer.modes.Left) {
            MouseManager.addLeftClick();
         }

         if (mode.getMode() == MouseSpoofer.modes.Both) {
            MouseManager.addRightClick();
            MouseManager.addLeftClick();
         }

      }
   }

   public enum modes {
      Right, Left, Both
   }

}