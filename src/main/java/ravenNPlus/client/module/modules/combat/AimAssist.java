package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.modules.player.RightClicker;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import org.lwjgl.input.Mouse;

public class AimAssist extends Module {

   public static SliderSetting speed, compliment, fov, distance;
   public static TickSetting clickAim, weaponOnly, aimInvis, breakBlocks, blatantMode, ignoreFriends, sortBots;

   public AimAssist() {
      super("AimAssist", ModuleCategory.combat, "Making your aim very good");
      this.addSetting(speed = new SliderSetting("Speed 1", 45.0D, 5.0D, 100.0D, 1.0D));
      this.addSetting(compliment = new SliderSetting("Speed 2", 40.0D, 2D, 97.0D, 1.0D));
      this.addSetting(fov = new SliderSetting("FOV", 60.0D, 15.0D, 360.0D, 1.0D));
      this.addSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.addSetting(sortBots = new TickSetting("Sort bots", o));
      this.addSetting(clickAim = new TickSetting("Click aim", o));
      this.addSetting(breakBlocks = new TickSetting("Break blocks", x));
      this.addSetting(ignoreFriends = new TickSetting("Ignore Friends", x));
      this.addSetting(weaponOnly = new TickSetting("Weapon only", o));
      this.addSetting(aimInvis = new TickSetting("Aim invis", o));
      this.addSetting(blatantMode = new TickSetting("Aimlock", o));
   }

   public void update() {
      if (!Utils.Client.currentScreenMinecraft()) return;
      if (!this.inGame()) return;

      if (breakBlocks.isToggled() && mc.objectMouseOver != null) {
         BlockPos p = mc.objectMouseOver.getBlockPos();
         if (p != null) {
            Block bl = mc.theWorld.getBlockState(p).getBlock();
            if (bl != Blocks.air && !(bl instanceof BlockLiquid) && bl instanceof Block) {
               return;
            }
         }
      }

      if (!weaponOnly.isToggled() || InvUtils.isPlayerHoldingWeapon()) {
         Module autoClicker = Client.moduleManager.getModuleByClazz(RightClicker.class);
         if ((clickAim.isToggled() && Utils.Client.autoClickerClicking()) || (Mouse.isButtonDown(0) && autoClicker != null && !autoClicker.isEnabled()) || !clickAim.isToggled()) {
            Entity en = this.getEnemy();
            if (en != null) {
               if (Client.debugger) {
                  Utils.Player.sendMessageToSelf(this.getName() + " &e" + en.getName());
               }

               if (blatantMode.isToggled()) {
                  Utils.Player.aim(en, 0.0F, false, false);
               } else {
                  double n = Utils.Player.fovFromEntity(en);
                  if (n > 1.0D || n < -1.0D) {
                     double complimentSpeed = n * (ThreadLocalRandom.current().nextDouble(compliment.getValue() - 1.47328, compliment.getValue() + 2.48293) / 100);
                     float val = (float) (-(complimentSpeed + n / (101.0D - (float) ThreadLocalRandom.current().nextDouble(speed.getValue() - 4.723847, speed.getValue()))));
                     mc.thePlayer.rotationYaw += val;
                  }
               }
            }
         }
      }
   }

   public Entity getEnemy() {
      int fov = (int) AimAssist.fov.getValue();
      Iterator var2 = mc.theWorld.playerEntities.iterator();

      EntityPlayer en;
      do {
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var2.hasNext()) {
                              return null;
                           }
                           en = (EntityPlayer) var2.next();
                        } while (ignoreFriends.isToggled() && Utils.FriendUtils.isAFriend(en));
                     } while (en == mc.thePlayer);
                  } while (en.isDead);
               } while (!aimInvis.isToggled() && en.isInvisible());
            } while ((double) mc.thePlayer.getDistanceToEntity(en) > distance.getValue());
         } while (sortBots.isToggled() && NewAntiBot.isBot(en));
      } while (!blatantMode.isToggled() && !Utils.Player.fov(en, (float) fov));

      return en;
   }

}