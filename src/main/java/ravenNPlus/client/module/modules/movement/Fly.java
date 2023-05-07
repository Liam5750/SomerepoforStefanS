package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.client.Minecraft;

public class Fly extends Module {

   private final Fly.VanFly vanFly = new VanFly();
   private final Fly.GliFly gliFly = new Fly.GliFly();
   public static DescriptionSetting dc;
   public static SliderSetting a, b;
   private static final String c1 = "Vanilla";
   private static final String c2 = "Glide";

   public Fly() {
      super("Fly", ModuleCategory.movement, "Allows you to Fly");
      this.addSetting(a = new SliderSetting("Value", 2, 1, 2, 1));
      this.addSetting(dc = new DescriptionSetting(Utils.md + c2));
      this.addSetting(b = new SliderSetting("Speed", 2.0D, 1.0D, 5.0D, 0.1D));
   }

   public void onEnable() {
      switch (a.getValueToInt()) {
         case 1:
            this.vanFly.onEnable();
            break;
         case 2:
            this.gliFly.onEnable();
      }
   }

   public void onDisable() {
      switch (a.getValueToInt()) {
         case 1:
            this.vanFly.onDisable();
            break;
         case 2:
            this.gliFly.onDisable();
      }
   }

   public void update() {
      switch (a.getValueToInt()) {
         case 1:
            this.vanFly.update();
            break;
         case 2:
            this.gliFly.update();
      }
   }

   public void guiUpdate() {
      switch (a.getValueToInt()) {
         case 1:
            dc.setDesc(Utils.md + c1);
            break;
         case 2:
            dc.setDesc(Utils.md + c2);
      }
   }

   class GliFly {
      boolean opf = false;

      public void onEnable() {
      }

      public void onDisable() {
         this.opf = false;
      }

      public void update() {
         if (Module.mc.thePlayer.movementInput.moveForward > 0.0F) {

            if (!this.opf) {
               this.opf = true;
               if (Module.mc.thePlayer.onGround) {
                  Module.mc.thePlayer.jump();
               }
            } else {
               if (Module.mc.thePlayer.onGround || Module.mc.thePlayer.isCollidedHorizontally) {
                  Fly.this.disable();
                  return;
               }

               double s = 1.94D * Fly.b.getValue();
               double r = Math.toRadians(Module.mc.thePlayer.rotationYaw + 90.0F);
               Module.mc.thePlayer.motionX = s * Math.cos(r);
               Module.mc.thePlayer.motionZ = s * Math.sin(r);
            }
         }
      }
   }

   static class VanFly {
      private final float dfs = 0.05F;

      public void onEnable() {
      }

      public void onDisable() {
         if (Minecraft.getMinecraft().thePlayer == null)
            return;

         if (Minecraft.getMinecraft().thePlayer.capabilities.isFlying) {
            Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
         }

         Minecraft.getMinecraft().thePlayer.capabilities.setFlySpeed(0.05F);
      }

      public void update() {
         Module.mc.thePlayer.motionY = 0.0D;
         Module.mc.thePlayer.capabilities.setFlySpeed((float) (0.05000000074505806D * Fly.b.getValue()));
         Module.mc.thePlayer.capabilities.isFlying = true;
      }
   }

}