package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {

   public static DescriptionSetting a, c;
   public static SliderSetting b, m;

   public NoSlow() {
      super("NoSlow", ModuleCategory.movement, "Default is 80% motion reduction");
      this.addSetting(c = new DescriptionSetting("Hypixel max: 22%, Default : 80&"));
      this.addSetting(m = new SliderSetting("Mode", 3, 1, 3, 1));
      this.addSetting(a = new DescriptionSetting(Utils.md + ""));
      this.addSetting(b = new SliderSetting("Slow %", 2, 1, 80, 1));
   }

   @Override
   public void onDisable() {
      if (Utils.Client.getTimer().timerSpeed != Utils.Client.timer_default) {
         Utils.Client.resetTimer();
      }
   }

   @SubscribeEvent
   public void r(TickEvent.PlayerTickEvent e) {
      if (!this.inGame()) return;

      boolean useKeyDown = mc.gameSettings.keyBindUseItem.isKeyDown();

      if ((InvUtils.isPlayerHoldingSword() || InvUtils.isPlayerHoldingFood() || InvUtils.isPlayerHoldingBow()) && useKeyDown && this.isMoving() && this.onGround()) {
         if (this.onLadder() || Utils.Player.isInLiquid()) return;

         float val = (100.0F - (float) b.getValue()) / 100.0F;

         if (m.getValueToInt() == 1) {
            if (!this.onGround()) return;
            this.player().moveForward *= val;
            this.player().moveStrafing *= val;
         }

         if (m.getValueToInt() == 2) {
            if (!this.onGround()) return;
            this.player().movementInput.moveForward *= val;
            this.player().movementInput.moveStrafe *= val;
         }

         if (m.getValueToInt() == 3) {
            if (!this.onGround()) return;
            if (b.getValueToInt() > 5) b.setValue(5);
            Utils.Client.getTimer().timerSpeed = b.getValueToFloat();
         }

      } else if ((!useKeyDown || !this.onGround()) && Utils.Client.getTimer().timerSpeed != Utils.Client.timer_default) {
         Utils.Client.resetTimer();
      }
   }

   public void guiUpdate() {
      switch ((int) m.getValue()) {
         case 1:
            a.setDesc(Utils.md + "MoveInput");
            break;
         case 2:
            a.setDesc(Utils.md + "MovementInput");
            break;
         case 3:
            a.setDesc(Utils.md + "Timer");
            break;
      }
   }

}