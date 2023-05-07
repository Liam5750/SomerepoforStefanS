package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.settings.KeyBinding;

public class BHop extends Module {

   public static SliderSetting speed, yOff, xOff;
   public static TickSetting jump, sneak;

   public BHop() {
      super("BHop", ModuleCategory.movement, "Jump like a play boy bunny");
      this.addSetting(speed = new SliderSetting("Speed", 2.0D, 0.5D, 20.0D, 0.5D));
      this.addSetting(yOff = new SliderSetting("Offset Y", 0.0D, 0.0D, 1.0D, 0.1D));
      this.addSetting(xOff = new SliderSetting("Offset X", 0.0D, 0.0D, 1.0D, 0.1D));
      this.addSetting(jump = new TickSetting("Jump", true));
      this.addSetting(sneak = new TickSetting("Sneak", false));
   }

   public void update() {
      Module fly = Client.moduleManager.getModuleByClazz(Fly.class);
      if (fly != null && !fly.isEnabled() && this.isMoving() && !this.inWater()) {
         KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
         this.player().noClip = true;

         if (this.onGround() && jump.isToggled()) {
            this.player().jump();
         }

         if (!sneak.isToggled()) {
            this.player().isSneaking();
         }

         this.player().setSprinting(true);
         double spd = 0.0025D * speed.getValue();

         double motionX = this.player().motionX * this.player().motionX + yOff.getValue();
         double motionY = this.player().motionZ * this.player().motionZ + xOff.getValue();

         double m = (float) (Math.sqrt(motionX + motionY) + spd);

         Utils.Player.bop(m);
      }
   }

}