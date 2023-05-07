package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import java.lang.reflect.Field;

public class DelayRemover extends Module {

   private final Field leftClickCounterField;

   public DelayRemover() {
      super("Delay Remover", ModuleCategory.combat, "Gives you 1.7 hitreg");
      this.leftClickCounterField = ReflectionHelper.findField(Minecraft.class, "field_71429_W", "leftClickCounter");
      if (this.leftClickCounterField != null) this.leftClickCounterField.setAccessible(true);
   }

   @Override
   public boolean canBeEnabled() {
      return this.leftClickCounterField != null;
   }

   @SubscribeEvent
   public void playerTickEvent(PlayerTickEvent event) {
      if (this.inGame() && this.leftClickCounterField != null) {
         if (!this.inFocus() || mc.thePlayer.capabilities.isCreativeMode) {
            return;
         }

         try {
            this.leftClickCounterField.set(mc, 0);
         } catch (IllegalAccessException | IndexOutOfBoundsException ex) {
            ex.printStackTrace();
            this.disable();
         }
      }
   }

}