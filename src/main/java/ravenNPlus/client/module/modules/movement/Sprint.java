package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Sprint extends Module {

   public static TickSetting a;

   public Sprint() {
      super("Sprint", ModuleCategory.movement, "Automatically Sprints");
      this.addSetting(a = new TickSetting("Omni Sprint", false));
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (this.inGame() && this.inFocus()) {
         if (a.isToggled()) {
            if (Utils.Player.isMoving() && this.player().getFoodStats().getFoodLevel() > 6) {
               this.player().setSprinting(true);
            }
         } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
         }
      }
   }

}