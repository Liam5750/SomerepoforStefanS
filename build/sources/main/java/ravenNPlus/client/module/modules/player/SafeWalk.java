package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.module.*;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import java.awt.*;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {

   public static TickSetting blocksOnly, doShift, shiftOnJump, onHold, showBlockAmount, lookDown;
   public static DoubleSliderSetting pitchRange;
   public static SliderSetting blockShowMode;
   public static DescriptionSetting blockShowModeDesc;
   public static DoubleSliderSetting shiftTime;
   private static boolean shouldBridge;
   private static boolean isShifting;
   private boolean allowedShift;
   private final CoolDown shiftTimer = new CoolDown(0);

   public SafeWalk() {
      super("SafeWalk", ModuleCategory.player, "Dont fall of blocks (Fix without shift never lmao)");
      this.addSetting(doShift = new TickSetting("Shift", false));
      this.addSetting(onHold = new TickSetting("On shift hold", false));
      this.addSetting(shiftOnJump = new TickSetting("Shift during jumps", false));
      this.addSetting(shiftTime = new DoubleSliderSetting("Shift time: (s)", 60, 100, 0, 280, 5));
      this.addSetting(blocksOnly = new TickSetting("Blocks only", true));
      this.addSetting(showBlockAmount = new TickSetting("Show amount of blocks", true));
      this.addSetting(blockShowMode = new SliderSetting("Block display info:", 2D, 1D, 2D, 1D));
      this.addSetting(blockShowModeDesc = new DescriptionSetting("Mode: "));
      this.addSetting(lookDown = new TickSetting("Only when looking down", true));
      this.addSetting(pitchRange = new DoubleSliderSetting("Pitch min range:", 70D, 85, 0D, 90D, 1D));
   }

   public void onDisable() {
      if (doShift.isToggled() && Utils.Player.playerOverAir(1)) {
         this.setShift(false);
      }

      shouldBridge = false;
      isShifting = false;
   }

   public void guiUpdate() {
      blockShowModeDesc.setDesc(Utils.md + BlockAmountInfo.values()[(int) blockShowMode.getValue() - 1]);
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (!Utils.Client.currentScreenMinecraft())
         return;

      if (!this.inGame()) {
         return;
      }

      boolean shiftTimeSettingActive = shiftTime.getInputMax() > 0;

      if (doShift.isToggled()) {
         if (lookDown.isToggled()) {
            if (this.player().rotationPitch < pitchRange.getInputMin()
                    || this.player().rotationPitch > pitchRange.getInputMax()) {
               shouldBridge = false;
               if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                  setShift(true);
               }
               return;
            }
         }
         if (onHold.isToggled()) {
            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
               shouldBridge = false;
               return;
            }
         }

         if (blocksOnly.isToggled()) {
            ItemStack i = this.player().getHeldItem();
            if (i == null || !(i.getItem() instanceof ItemBlock)) {
               if (isShifting) {
                  isShifting = false;
                  this.setShift(false);
               }

               return;
            }
         }

         if (this.onGround()) {
            if (Utils.Player.playerOverAir(1)) {
               if (shiftTimeSettingActive) { // making sure that the player has set the value so some number
                  shiftTimer.setCooldown(
                          Utils.Java.randomInt(shiftTime.getInputMin(), shiftTime.getInputMax() + 0.1));
                  shiftTimer.start();
               }

               isShifting = true;
               this.setShift(true);
               shouldBridge = true;
            } else if (this.player().isSneaking() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())
                    && onHold.isToggled()) { // if player is sneaking and shiftDown and holdSetting turned on
               isShifting = false;
               shouldBridge = false;
               this.setShift(false);
            } else if (onHold.isToggled() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) { // if
               isShifting = false;
               shouldBridge = false;
               this.setShift(false);
            } else if (this.player().isSneaking()
                    && (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled())
                    && (!shiftTimeSettingActive || shiftTimer.hasFinished())) {
               isShifting = false;
               this.setShift(false);
               shouldBridge = true;
            } else if (this.player().isSneaking() && !onHold.isToggled()
                    && (!shiftTimeSettingActive || shiftTimer.hasFinished())) {
               isShifting = false;
               this.setShift(false);
               shouldBridge = true;
            }
         } else if (shouldBridge && this.player().capabilities.isFlying) {
            this.setShift(false);
            shouldBridge = false;
         } else if (shouldBridge && Utils.Player.playerOverAir(1) && shiftOnJump.isToggled()) {
            isShifting = true;
            this.setShift(true);
         } else {
            // rn we are in the air, and we are not flying, meaning that we are in a jump.
            // and since shiftOnJump is turned off, we just un-shift and uhh...
            isShifting = false;
            this.setShift(false);
         }
      }
   }

   @SubscribeEvent
   public void r(TickEvent.RenderTickEvent e) {
      if (!showBlockAmount.isToggled() || !this.inGame())
         return;
      if (mc.currentScreen == null) {
         if (shouldBridge) {

            int totalBlocks = 0;
            if (BlockAmountInfo.values()[(int) blockShowMode.getValue()
                    - 1] == BlockAmountInfo.BLOCKS_IN_CURRENT_STACK) {
               totalBlocks = Utils.Player.getBlockAmountInCurrentStack(this.player().inventory.currentItem);
            } else {
               for (int slot = 0; slot < 36; slot++) {
                  totalBlocks += Utils.Player.getBlockAmountInCurrentStack(slot);
               }
            }

            if (totalBlocks <= 0) {
               return;
            }

            int rgb;
            if (totalBlocks < 3)
               rgb = new java.awt.Color(238, 0, 0).getRGB();
            else if (totalBlocks < 6)
               rgb = new java.awt.Color(215, 25, 0).getRGB();
            else if (totalBlocks < 9)
               rgb = new java.awt.Color(203, 37, 0).getRGB();
            else if (totalBlocks < 12)
               rgb = new java.awt.Color(192, 49, 0).getRGB();
            else if (totalBlocks < 15)
               rgb = new java.awt.Color(181, 61, 0).getRGB();
            else if (totalBlocks < 18)
               rgb = new java.awt.Color(170, 74, 0).getRGB();
            else if (totalBlocks < 21)
               rgb = new java.awt.Color(158, 86, 0).getRGB();
            else if (totalBlocks < 24)
               rgb = new java.awt.Color(147, 98, 0).getRGB();
            else if (totalBlocks < 27)
               rgb = new java.awt.Color(136, 110, 0).getRGB();
            else if (totalBlocks < 30)
               rgb = new java.awt.Color(124, 122, 0).getRGB();
            else if (totalBlocks < 33)
               rgb = new java.awt.Color(113, 134, 0).getRGB();
            else if (totalBlocks < 36)
               rgb = new java.awt.Color(102, 146, 0).getRGB();
            else if (totalBlocks < 39)
               rgb = new java.awt.Color(90, 158, 0).getRGB();
            else if (totalBlocks < 42)
               rgb = new java.awt.Color(79, 170, 0).getRGB();
            else if (totalBlocks < 45)
               rgb = new java.awt.Color(68, 182, 0).getRGB();
            else if (totalBlocks < 48)
               rgb = new java.awt.Color(56, 194, 0).getRGB();
            else if (totalBlocks < 51)
               rgb = new java.awt.Color(45, 207, 0).getRGB();
            else if (totalBlocks < 54)
               rgb = new java.awt.Color(34, 219, 0).getRGB();
            else if (totalBlocks < 57)
               rgb = new java.awt.Color(23, 231, 0).getRGB();
            else if (totalBlocks < 60)
               rgb = new java.awt.Color(11, 243, 0).getRGB();
            else if (totalBlocks < 65)
               rgb = new java.awt.Color(0, 255, 0).getRGB();
            else rgb = new java.awt.Color(0, 255, 0).getRGB();

            String t;
            if (totalBlocks == 1) {
               t = totalBlocks + " block";
            } else {
               t = totalBlocks + " blocks";
            }

            int x = (int) (this.screenWidth() - mc.fontRendererObj.getStringWidth(t) / 2);
            int y;
            if (Client.debugger) {
               y = (int) (this.screenHeight() / 2 + 17 + mc.fontRendererObj.FONT_HEIGHT);
            } else {
               y = (int) (this.screenHeight() / 2 + 15);
            }
            mc.fontRendererObj.drawString(t, (float) x, (float) y, rgb, false);
         }
      }
   }

   private void setShift(boolean sh) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), sh);
   }

   public enum BlockAmountInfo {
      BLOCKS_IN_TOTAL, BLOCKS_IN_CURRENT_STACK
   }

}