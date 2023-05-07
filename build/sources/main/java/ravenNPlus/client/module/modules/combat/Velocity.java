package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

   public static SliderSetting a, b, c, ap, bp, cp, dt;
   public static TickSetting d, e, f;
   public static ModeSetting g;
   public Mode mode;

   public Velocity() {
      super("Velocity", ModuleCategory.combat, "Modifies your Knockback");
      this.mode = Velocity.Mode.Distance;
      this.addSetting(a = new SliderSetting("Horizontal", 90.0, -100.0, 100.0, 1.0));
      this.addSetting(b = new SliderSetting("Vertical", 100.0, -100.0, 100.0, 1.0));
      this.addSetting(c = new SliderSetting("Chance", 100.0, 0.0, 100.0, 1.0));
      this.addSetting(d = new TickSetting("Only while targeting", false));
      this.addSetting(e = new TickSetting("Disable while holding S", false));
      this.addSetting(f = new TickSetting("Different Velocity for projectiles", false));
      this.addSetting(g = new ModeSetting("Projectiles Mode", this.mode));
      this.addSetting(ap = new SliderSetting("Horizontal projectiles", 90.0, -100.0, 100.0, 1.0));
      this.addSetting(bp = new SliderSetting("Vertical projectiles", 100.0, -100.0, 100.0, 1.0));
      this.addSetting(cp = new SliderSetting("Chance projectiles", 100.0, 0.0, 100.0, 1.0));
      this.addSetting(dt = new SliderSetting("Distance projectiles", 3.0, 0.0, 20.0, 0.1));
   }

   @SubscribeEvent
   public void onLivingUpdate(Event fe) {
      if (!(fe instanceof LivingEvent)) return;

      if (this.inGame() && this.player().maxHurtTime > 0 && this.player().hurtTime == this.player().maxHurtTime) {
         if (d.isToggled() && (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null))
            return;

         if (e.isToggled() && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()))
            return;

         if (this.player().getLastAttacker() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) this.player().getLastAttacker();
            Item item = attacker.getCurrentEquippedItem() != null ? attacker.getCurrentEquippedItem().getItem() : null;
            if ((item instanceof ItemEgg || item instanceof ItemBow || item instanceof ItemSnow || item instanceof ItemFishingRod) && this.mode == Mode.ItemHeld) {
               this.x();
               return;
            }

            if ((double) attacker.getDistanceToEntity(this.player()) > dt.getValue()) {
               this.x();
               return;
            }
         }

         if (c.getValue() != 100.0) {
            double ch = Math.random();
            if (ch >= c.getValue() / 100.0) {
               return;
            }
         }

         if (a.getValue() != 100.0) {
            this.player().motionX *= a.getValue() / 100.0;
            this.player().motionZ *= a.getValue() / 100.0;
         }

         if (b.getValue() != 100.0) {
            this.player().motionY *= b.getValue() / 100.0;
         }
      }
   }

   public void x() {
      if (cp.getValue() != 100.0) {
         double ch = Math.random();
         if (ch >= cp.getValue() / 100.0) {
            return;
         }
      }

      if (ap.getValue() != 100.0) {
         this.player().motionX *= ap.getValue() / 100.0;
         this.player().motionZ *= ap.getValue() / 100.0;
      }

      if (bp.getValue() != 100.0) {
         this.player().motionY *= bp.getValue() / 100.0;
      }
   }

   public enum Mode {
      Distance, ItemHeld
   }

}