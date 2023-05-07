package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AntiVoid extends Module {

   public static ModeSetting mode;
   public static TickSetting x, y, z, noCreative;
   public static SliderSetting xS, yS, zS, distance, chance;

   public AntiVoid() {
      super("AntiVoid", ModuleCategory.movement, "Avoids you from falling in the void");
      this.addSetting(mode = new ModeSetting("Mode", AntiVoid.modes.TPBack));
      this.addSetting(distance = new SliderSetting("Distance", 4.0D, 2.0D, 10.0D, 1.0D));
      this.addSetting(chance = new SliderSetting("Chance %", 100, 0, 100, 1));
      this.addSetting(noCreative = new TickSetting("Cancel if in Creative", true));
      this.addSetting(x = new TickSetting("Motion X", true));
      this.addSetting(xS = new SliderSetting("Z Value: ", 1.0D, 1.0D, 10.0D, 1.0D));
      this.addSetting(y = new TickSetting("Motion Y", false));
      this.addSetting(yS = new SliderSetting("Y Value: ", 1.0D, 1.0D, 10.0D, 1.0D));
      this.addSetting(z = new TickSetting("Motion Z", false));
      this.addSetting(zS = new SliderSetting("Z Value: ", 1.0D, 1.0D, 10.0D, 1.0D));
   }

   private boolean flagged = false, tried = false;
   private double posX = 0.0, posY = 0.0, posZ = 0.0, lastY = 0.0;

   @Override
   public void onEnable() {
      if (this.player() != null) {
         lastY = this.player().posY;
      } else {
         lastY = 0.0;
      }

      tried = false;
      flagged = false;

      if (this.isSingleplayer()) {
         if (lastY == 0.0) {
            lastY = this.player().posY;
         }
      }
   }

   @Override
   public void onDisable() {
      tried = false;
      flagged = false;
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (this.inGame() && this.inFocus()) {

         if (!(chance.getValue() == 100 || Math.random() <= chance.getValue() / 100))
            return;

         if (noCreative.isToggled())
            if (this.player().capabilities.isCreativeMode)
               return;

         if (mode.getMode() == AntiVoid.modes.Motion) {
            if (this.player().fallDistance > distance.getValue() && !tried) {
               this.player().motionY += xS.getValue();
               this.player().fallDistance = 0.0F;
               tried = true;
            } else
               tried = false;
         }

         if (mode.getMode() == AntiVoid.modes.Packets) {
            if (this.player().fallDistance > distance.getValue() && !tried) {
               this.sendPacketNetHandler(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(this.player().posX + 1, this.player().posY + 1, this.player().posZ + 1, false));
               tried = true;
            } else
               tried = false;
         }

         if (mode.getMode() == AntiVoid.modes.TPBack) {
            if (this.player().onGround && ravenNPlus.client.utils.Utils.Player.playerOnBlock()) {
               posX = this.player().prevPosX;
               posY = this.player().prevPosY;
               posZ = this.player().prevPosZ;
            }

            if (this.player().fallDistance > distance.getValue() && !tried) {
               this.player().setPositionAndUpdate(posX, posY, posZ);
               this.player().fallDistance = 0F;
               this.player().motionX = 0.0;
               this.player().motionY = 0.0;
               this.player().motionZ = 0.0;
               tried = true;
            } else
               tried = false;
         }

         if (mode.getMode() == AntiVoid.modes.Jartex) {
            if (this.player().fallDistance > distance.getValue() && this.player().posY < lastY + 0.01 && this.player().motionY <= 0 && !this.player().onGround && !flagged) {
               this.player().motionY = 0.0;
               this.player().motionZ *= 0.838;
               this.player().motionX *= 0.838;
            }

            lastY = this.player().posY;
         }

         if (mode.getMode() == AntiVoid.modes.OldCubecraft) {
            if (this.player().fallDistance > distance.getValue() && this.player().posY < lastY + 0.01 && this.player().motionY <= 0 && !this.player().onGround && !flagged) {
               this.player().motionY = 0.0;
               this.player().motionZ = 0.0;
               this.player().motionX = 0.0;
               this.player().jumpMovementFactor = 0.00f;

               if (!tried) {
                  tried = true;
                  this.sendPacketNetHandler(new net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition(this.player().posX, (32000.0), this.player().posZ, false));
               }
            } else
               tried = false;

            lastY = this.player().posY;
         }

         if (mode.getMode() == AntiVoid.modes.AdvMotion) {
            if (!this.player().onGround && this.player().fallDistance > distance.getValue() && this.player().isEntityAlive() && this.player().getHealth() > 0) {

               if (x.isToggled())
                  this.player().motionX = xS.getValue();

               if (y.isToggled())
                  this.player().motionY = yS.getValue();

               if (z.isToggled())
                  this.player().motionZ = zS.getValue();
            }
         }
      }
   }

   public enum modes {
      Motion, Packets, TPBack,
      Jartex, OldCubecraft, AdvMotion
   }

}