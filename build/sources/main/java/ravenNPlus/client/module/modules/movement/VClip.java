package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.SoundUtil;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;

public class VClip extends Module {

   public static SliderSetting distance;
   public static TickSetting sound;

   public VClip() {
      super("VClip", ModuleCategory.movement, "TP Up blocks");
      this.addSetting(distance = new SliderSetting("Distance", 2.0D, -10.0D, 10.0D, 0.5D));
      this.addSetting(sound = new TickSetting("Sound", true));
   }

   @Override
   public void onEnable() {
      if (distance.getValue() != 0.0D) {
         this.player().setPosition(this.player().posX, this.player().posY + distance.getValue(), this.player().posZ);

         if (sound.isToggled()) {
            SoundUtil.play(SoundUtil.endermenPortal, 1F, 1F);
         }
      }

      this.disable();
   }

}