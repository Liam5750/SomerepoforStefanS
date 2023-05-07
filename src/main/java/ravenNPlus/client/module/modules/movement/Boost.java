package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;

public class Boost extends Module {

   public static DescriptionSetting c;
   public static SliderSetting a, b;
   private int i = 0;
   private boolean t = false;

   public Boost() {
      super("Boost", ModuleCategory.movement, "Boosts the world timer");
      this.addSetting(c = new DescriptionSetting("1 Sec = 20 Ticks"));
      this.addSetting(a = new SliderSetting("Multiplier", 2.0D, 1.0D, 3.0D, 0.05D));
      this.addSetting(b = new SliderSetting("Time (ticks)", 15.0D, 1.0D, 80.0D, 1.0D));
   }

   public void onEnable() {
      Module timer = Client.moduleManager.getModuleByClazz(Timer.class);
      if (timer != null && timer.isEnabled()) {
         this.t = true;
         timer.disable();
      }
   }

   public void onDisable() {
      this.i = 0;
      if (Utils.Client.getTimer().timerSpeed != 1.0F) {
         Utils.Client.resetTimer();
      }

      if (this.t) {
         Module timer = Client.moduleManager.getModuleByClazz(Timer.class);
         if (timer != null) timer.enable();
      }

      this.t = false;
   }

   public void update() {
      if (this.i == 0) {
         this.i = this.player().ticksExisted;
      }

      Utils.Client.getTimer().timerSpeed = (float) a.getValue();
      if ((double) this.i == (double) this.player().ticksExisted - b.getValue()) {
         Utils.Client.resetTimer();
         this.disable();
      }
   }

}