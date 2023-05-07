package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {

   public static TickSetting sneak, sprint, jump, move, arrowKeys;
   public static DescriptionSetting desc;

   public InvMove() {
      super("InvMove", ModuleCategory.movement, "Move in Inventory");
      this.addSetting(desc = new DescriptionSetting("Arrow Keys can be Bannable"));
      this.addSetting(arrowKeys = new TickSetting("Arrow Keys", o));
      this.addSetting(move = new TickSetting("Allow Move", x));
      this.addSetting(jump = new TickSetting("Allow Jump", o));
      this.addSetting(sprint = new TickSetting("Allow Sprint", o));
      this.addSetting(sneak = new TickSetting("Allow Sneak", o));
   }

   public void update() {
      if (mc.currentScreen != null && this.isSingleplayer()) {
         if (ravenNPlus.client.utils.Utils.Player.isPlayerInContainer())
            return;

         if (move.isToggled()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
         }

         if (jump.isToggled())
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));

         if (sprint.isToggled())
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()));

         if (sneak.isToggled())
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));

         if (arrowKeys.isToggled()) {
            if (Keyboard.isKeyDown(208) && mc.thePlayer.rotationPitch < 90.0F) {
               this.player().rotationPitch += 6.0F;
            }

            if (Keyboard.isKeyDown(200) && mc.thePlayer.rotationPitch > -90.0F) {
               this.player().rotationPitch -= 6.0F;
            }

            if (Keyboard.isKeyDown(205)) {
               this.player().rotationYaw += 6.0F;
            }

            if (Keyboard.isKeyDown(203)) {
               this.player().rotationYaw -= 6.0F;
            }
         }
      }
   }

}