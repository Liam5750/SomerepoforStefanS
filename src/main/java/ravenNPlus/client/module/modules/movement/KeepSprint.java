package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.module.*;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.modules.combat.Reach;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;

public class KeepSprint extends Module {

   public static SliderSetting b;
   public static TickSetting c;

   public KeepSprint() {
      super("KeepSprint", ModuleCategory.movement, "Default is 40% motion reduction");
      this.addSetting(b = new SliderSetting("Slow %", 40.0D, 0.0D, 100.0D, 1.0D));
      this.addSetting(c = new TickSetting("Only reduce reach hits", false));
   }

   public static void sl(net.minecraft.entity.Entity en) {
      double dist;
      Module reach = Client.moduleManager.getModuleByClazz(Reach.class);
      if (c.isToggled() && reach != null && reach.isEnabled() && !mc.thePlayer.capabilities.isCreativeMode) {
         dist = mc.objectMouseOver.hitVec.distanceTo(mc.getRenderViewEntity().getPositionEyes(1.0F));
         double val;
         if (dist > 3.0D) {
            val = (100.0D - (double)((float)b.getValue())) / 100.0D;
         } else {
            val = 0.6D;
         }

         mc.thePlayer.motionX *= val;
         mc.thePlayer.motionZ *= val;
      } else {
         dist = (100.0D - (double)((float)b.getValue())) / 100.0D;
         mc.thePlayer.motionX *= dist;
         mc.thePlayer.motionZ *= dist;
      }
   }

}