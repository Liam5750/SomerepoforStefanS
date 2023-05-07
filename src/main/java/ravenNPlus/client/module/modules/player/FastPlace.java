package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import ravenNPlus.client.utils.Utils;

import java.lang.reflect.Field;

public class FastPlace extends Module {

   public static SliderSetting delaySlider;
   public static TickSetting blockOnly, autoJump, holdDown;
   public final static Field rightClickDelayTimerField;

   static {
      rightClickDelayTimerField = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");

      if (rightClickDelayTimerField != null) {
         rightClickDelayTimerField.setAccessible(true);
      }
   }

   public FastPlace() {
      super("FastPlace", ModuleCategory.player, "Place fast");
      this.addSetting(delaySlider = new SliderSetting("Delay", 0.0D, 0.0D, 4.0D, 1.0D));
      this.addSetting(blockOnly = new TickSetting("Blocks only", x));
      this.addSetting(holdDown = new TickSetting("Hold Mouse", x));
      this.addSetting(autoJump = new TickSetting("Auto Jump", o));
   }

   @Override
   public boolean canBeEnabled() {
      return rightClickDelayTimerField != null;
   }

   @SubscribeEvent
   public void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         if (this.inGame() && this.inFocus() && rightClickDelayTimerField != null) {
            if (blockOnly.isToggled()) {
               ItemStack item = this.player().getHeldItem();
               if (item == null || !(item.getItem() instanceof ItemBlock)) {
                  return;
               }
            }

            if (autoJump.isToggled())
               if (this.onGround()) this.player().jump();

            try {
               int c = (int) delaySlider.getValue();

               Utils.Client.setMouseButtonState(1, !holdDown.isToggled() && this.isMoving());

               if (c == 0) {
                  rightClickDelayTimerField.set(mc, 0);
               } else {
                  if (c == 4) {
                     return;
                  }

                  int d = rightClickDelayTimerField.getInt(mc);
                  if (d == 4) {
                     rightClickDelayTimerField.set(mc, c);
                  }
               }
            } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
            }
         }
      }
   }

}