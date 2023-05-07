package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoJump extends Module {

   public static TickSetting cancelWhenShifting;
   private boolean c = false;

   public AutoJump() {
      super("AutoJump", ModuleCategory.player, "Automatically Jumps");
      this.addSetting(cancelWhenShifting = new TickSetting("Cancel when shifting", true));
   }

   @Override
   public void onDisable() {
      this.ju(this.c = false);
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (this.inGame()) {
         if (this.onGround() && (!cancelWhenShifting.isToggled() || !this.isSneaking())) {
            if (mc.theWorld.getCollidingBoundingBoxes(this.player(), this.player().getEntityBoundingBox().offset(this.player().motionX / 3.0D, -1.0D, this.player().motionZ / 3.0D)).isEmpty()) {
               this.ju(this.c = true);
            } else if (this.c) {
               this.ju(this.c = false);
            }
         } else if (this.c) {
            this.ju(this.c = false);
         }
      }
   }

   private void ju(boolean ju) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), ju);
   }

}