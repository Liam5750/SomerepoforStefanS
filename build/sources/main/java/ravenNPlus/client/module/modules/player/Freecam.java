package ravenNPlus.client.module.modules.player;

import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;
import org.lwjgl.input.Keyboard;
import ravenNPlus.client.utils.notifications.Type;

public class Freecam extends Module {

   public static SliderSetting a;
   public static TickSetting b, c;
   public static EntityOtherPlayerMP en = null;
   private int[] lcc = new int[]{Integer.MAX_VALUE, 0};
   private final float[] sAng = new float[]{0.0F, 0.0F};

   public Freecam() {
      super("Freecam", ModuleCategory.player, "Fly with your camera");
      this.addSetting(a = new SliderSetting("Speed", 2.5D, 0.5D, 10.0D, 0.5D));
      this.addSetting(b = new TickSetting("Disable on damage", true));
      this.addSetting(c = new TickSetting("Disable on TP", false));
   }

   @Override
   public void onEnable() {
      if (!this.inGame()) {
         return;
      }
      if (!this.onGround()) {
         this.disable();
      } else {
         en = new EntityOtherPlayerMP(mc.theWorld, this.player().getGameProfile());
         en.copyLocationAndAnglesFrom(this.player());
         this.sAng[0] = en.rotationYawHead = this.player().rotationYawHead;
         this.sAng[1] = this.player().rotationPitch;
         en.setVelocity(0.0D, 0.0D, 0.0D);
         en.setInvisible(true);
         mc.theWorld.addEntityToWorld(-8008, en);
         mc.setRenderViewEntity(en);
      }
   }

   public void onDisable() {
      if (en != null) {
         mc.setRenderViewEntity(this.player());
         this.player().rotationYaw = this.player().rotationYawHead = this.sAng[0];
         this.player().rotationPitch = this.sAng[1];
         mc.theWorld.removeEntity(en);
         en = null;
      }

      this.lcc = new int[]{Integer.MAX_VALUE, 0};
      int x = this.player().chunkCoordX;
      int z = this.player().chunkCoordZ;

      for (int x2 = -1; x2 <= 1; ++x2) {
         for (int z2 = -1; z2 <= 1; ++z2) {
            int a = x + x2;
            int b = z + z2;
            mc.theWorld.markBlockRangeForRenderUpdate(a * 16, 0, b * 16, a * 16 + 15, 256, b * 16 + 15);
         }
      }
   }

   public void update() {
      if (!this.inGame() || en == null)
         return;

      if (b.isToggled() && this.player().hurtTime != 0) {
         this.notification(Type.INFO, "Disabled cause Damage", 5);
         this.disable();
      } else {
         this.player().setSprinting(false);
         this.player().moveForward = 0.0F;
         this.player().moveStrafing = 0.0F;
         en.rotationYaw = en.rotationYawHead = this.player().rotationYaw;
         en.rotationPitch = this.player().rotationPitch;
         double s = 0.215D * a.getValue();
         EntityOtherPlayerMP otherPlayerMP;
         double rad;
         double dx;
         double dz;

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            rad = (double) en.rotationYawHead * 0.017453292519943295D;
            dx = -1.0D * Math.sin(rad) * s;
            dz = Math.cos(rad) * s;
            otherPlayerMP = en;
            otherPlayerMP.posX += dx;
            otherPlayerMP.posZ += dz;
         }

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
            rad = (double) en.rotationYawHead * 0.017453292519943295D;
            dx = -1.0D * Math.sin(rad) * s;
            dz = Math.cos(rad) * s;
            otherPlayerMP = en;
            otherPlayerMP.posX -= dx;
            otherPlayerMP.posZ -= dz;
         }

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
            rad = (double) (en.rotationYawHead - 90.0F) * 0.017453292519943295D;
            dx = -1.0D * Math.sin(rad) * s;
            dz = Math.cos(rad) * s;
            otherPlayerMP = en;
            otherPlayerMP.posX += dx;
            otherPlayerMP.posZ += dz;
         }

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
            rad = (double) (en.rotationYawHead + 90.0F) * 0.017453292519943295D;
            dx = -1.0D * Math.sin(rad) * s;
            dz = Math.cos(rad) * s;
            otherPlayerMP = en;
            otherPlayerMP.posX += dx;
            otherPlayerMP.posZ += dz;
         }

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
            otherPlayerMP = en;
            otherPlayerMP.posY += 0.93D * s;
         }

         if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            otherPlayerMP = en;
            otherPlayerMP.posY -= 0.93D * s;
         }

         this.player().setSneaking(false);
         if (this.lcc[0] != Integer.MAX_VALUE && (this.lcc[0] != en.chunkCoordX || this.lcc[1] != en.chunkCoordZ)) {
            int x = en.chunkCoordX;
            int z = en.chunkCoordZ;
            mc.theWorld.markBlockRangeForRenderUpdate(x * 16, 0, z * 16, x * 16 + 15, 256, z * 16 + 15);
         }

         this.lcc[0] = en.chunkCoordX;
         this.lcc[1] = en.chunkCoordZ;
      }
   }

   @SubscribeEvent
   public void rr(EnderTeleportEvent e) {
      if (e.entity == this.player() && c.isToggled()) {
         this.notification(Type.INFO, "Disabled cause TP", 5);
         this.disable();
      }
   }

   @SubscribeEvent
   public void re(RenderWorldLastEvent e) {
      if (this.inGame()) {
         this.player().renderArmPitch = this.player().prevRenderArmPitch = 700.0F;
         Utils.HUD.drawBoxAroundEntity(this.player(), 1, 0.0D, 0.0D, Color.green.getRGB(), false);
         Utils.HUD.drawBoxAroundEntity(this.player(), 2, 0.0D, 0.0D, Color.green.getRGB(), false);
      }
   }

   @SubscribeEvent
   public void m(MouseEvent e) {
      if (this.inGame() && e.button != -1) {
         e.setCanceled(true);
      }
   }

}