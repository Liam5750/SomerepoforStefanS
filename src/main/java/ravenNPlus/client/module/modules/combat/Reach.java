package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.util.Vec3;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import org.lwjgl.input.Mouse;

public class Reach extends Module {

   public static DoubleSliderSetting reach;
   public static TickSetting weapon_only, moving_only, sprint_only, hit_through_blocks;

   public Reach() {
      super("Reach", ModuleCategory.combat, "Modifies your Reach");
      this.addSetting(reach = new DoubleSliderSetting("Reach (Blocks)", 3.1, 3.3, 3, 12, 0.05));
      this.addSetting(weapon_only = new TickSetting("Weapon only", false));
      this.addSetting(moving_only = new TickSetting("Moving only", false));
      this.addSetting(sprint_only = new TickSetting("Sprint only", false));
      this.addSetting(hit_through_blocks = new TickSetting("Hit through blocks", false));
   }

   @SubscribeEvent
   public void onMouse(MouseEvent ev) {
      // legit event
      if (!this.inGame()) return;
      Module autoClicker = Client.moduleManager.getModuleByClazz(LeftClicker.class);
      if (autoClicker != null && autoClicker.isEnabled() && Mouse.isButtonDown(0)) return;
      if (ev.button >= 0 && ev.buttonstate) {
         call();
      }
   }

   @SubscribeEvent
   public void onRenderTick(TickEvent.RenderTickEvent ev) {
      // autoclick event
      if (!this.inGame()) return;
      Module autoClicker = Client.moduleManager.getModuleByClazz(LeftClicker.class);
      if (autoClicker == null || !autoClicker.isEnabled()) return;

      if (autoClicker.isEnabled() && Mouse.isButtonDown(0)) {
         call();
      }
   }

   public static boolean call() {
      if (!Utils.Player.isPlayerInGame()) return false;
      if (weapon_only.isToggled() && !InvUtils.isPlayerHoldingWeapon()) return false;
      if (moving_only.isToggled() && (double) mc.thePlayer.moveForward == 0.0D && (double) mc.thePlayer.moveStrafing == 0.0D)
         return false;
      if (sprint_only.isToggled() && !mc.thePlayer.isSprinting()) return false;
      if (!hit_through_blocks.isToggled() && mc.objectMouseOver != null) {
         BlockPos p = mc.objectMouseOver.getBlockPos();
         if (p != null && mc.theWorld.getBlockState(p).getBlock() != Blocks.air) {
            return false;
         }
      }

      double r = Utils.Client.ranModuleVal(reach, Utils.Java.rand());
      Object[] o = zz(r, 0.0D);
      if (o == null) {
         return false;
      } else {
         Entity en = (Entity) o[0];
         mc.objectMouseOver = new MovingObjectPosition(en, (Vec3) o[1]);
         mc.pointedEntity = en;
         return true;
      }
   }

   private static Object[] zz(double zzD, double zzE) {
      Module reach = Client.moduleManager.getModuleByClazz(Reach.class);
      if (reach != null && !reach.isEnabled()) {
         zzD = mc.playerController.extendedReach() ? 12.0D : 3.0D;
      }

      Entity entity1 = mc.getRenderViewEntity();
      Entity entity = null;
      if (entity1 == null) {
         return null;
      } else {
         mc.mcProfiler.startSection("pick");
         Vec3 eyes_positions = entity1.getPositionEyes(1.0F);
         Vec3 look = entity1.getLook(1.0F);
         Vec3 new_eyes_pos = eyes_positions.addVector(look.xCoord * zzD, look.yCoord * zzD, look.zCoord * zzD);
         Vec3 zz12 = null;
         List<Entity> zz8 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity1, entity1.getEntityBoundingBox().addCoord(look.xCoord * zzD, look.yCoord * zzD, look.zCoord * zzD).expand(1.0D, 1.0D, 1.0D));
         double zz9 = zzD;

         for (Entity o : zz8) {
            if (o.canBeCollidedWith()) {
               float ex = (float) ((double) o.getCollisionBorderSize() * HitBox.exp(o));
               AxisAlignedBB zz13 = o.getEntityBoundingBox().expand(ex, ex, ex);
               zz13 = zz13.expand(zzE, zzE, zzE);
               MovingObjectPosition zz14 = zz13.calculateIntercept(eyes_positions, new_eyes_pos);
               if (zz13.isVecInside(eyes_positions)) {
                  if (0.0D < zz9 || zz9 == 0.0D) {
                     entity = o;
                     zz12 = zz14 == null ? eyes_positions : zz14.hitVec;
                     zz9 = 0.0D;
                  }
               } else if (zz14 != null) {
                  double zz15 = eyes_positions.distanceTo(zz14.hitVec);
                  if (zz15 < zz9 || zz9 == 0.0D) {
                     if (o == entity1.ridingEntity) {
                        if (zz9 == 0.0D) {
                           entity = o;
                           zz12 = zz14.hitVec;
                        }
                     } else {
                        entity = o;
                        zz12 = zz14.hitVec;
                        zz9 = zz15;
                     }
                  }
               }
            }
         }

         if (zz9 < zzD && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
         }

         mc.mcProfiler.endSection();
         if (entity != null && zz12 != null) {
            return new Object[]{entity, zz12};
         } else {
            return null;
         }
      }
   }

}