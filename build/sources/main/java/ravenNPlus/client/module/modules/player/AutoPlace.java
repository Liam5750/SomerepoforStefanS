package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.module.*;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoPlace extends Module {

   public static DescriptionSetting ds;
   public static TickSetting hold, b;
   public static SliderSetting mode, delay;
   private double lfd = 0.0D;
   private long l = 0L;
   private int f = 0;
   private MovingObjectPosition lm = null;
   private BlockPos lp = null;

   public AutoPlace() {
      super("AutoPlace", ModuleCategory.player, "Automatically spams right click (when bridging like Telly)");
      this.addSetting(ds = new DescriptionSetting("FD: FPS/80"));
      this.addSetting(delay = new SliderSetting("Frame delay", 8.0D, 0.0D, 50.0D, 1.0D));
      this.addSetting(mode = new SliderSetting("Mode", 1.0D, 1.0D, 2.0D, 1.0D));
      this.addSetting(hold = new TickSetting("Hold right", true));
   }

   public void guiUpdate() {
      if (this.lfd != delay.getValue()) {
         this.rv();
      }

      this.lfd = delay.getValue();
   }

   public void onDisable() {
      if (hold.isToggled()) {
         this.rd(4);
      }

      this.rv();
   }

   public void update() {
      if (mode.getValue() == 2) {
         Module fastPlace = Client.moduleManager.getModuleByClazz(FastPlace.class);
         if (hold.isToggled() && Mouse.isButtonDown(1) && !this.player().capabilities.isFlying && fastPlace != null && !fastPlace.isEnabled()) {
            ItemStack i = this.player().getHeldItem();
            if (i == null || !(i.getItem() instanceof ItemBlock)) {
               return;
            }

            this.rd(this.player().motionY > 0.0D ? 1 : 1000);
         }
      }
   }

   @SubscribeEvent
   public void bh(DrawBlockHighlightEvent ev) {
      if (mode.getValue() == 2) {
         if (this.inGame()) {
            if (mc.currentScreen == null && !this.player().capabilities.isFlying) {
               ItemStack i = this.player().getHeldItem();
               if (i != null && i.getItem() instanceof ItemBlock) {
                  MovingObjectPosition m = mc.objectMouseOver;
                  if (m != null && m.typeOfHit == MovingObjectType.BLOCK && m.sideHit != EnumFacing.UP && m.sideHit != EnumFacing.DOWN) {
                     if (this.lm != null && (double) this.f < delay.getValue()) {
                        ++this.f;
                     } else {
                        this.lm = m;
                        BlockPos pos = m.getBlockPos();
                        if (this.lp == null || pos.getX() != this.lp.getX() || pos.getY() != this.lp.getY() || pos.getZ() != this.lp.getZ()) {
                           Block b = mc.theWorld.getBlockState(pos).getBlock();
                           if (b != null && b != Blocks.air && !(b instanceof BlockLiquid)) {
                              if (!hold.isToggled() || Mouse.isButtonDown(1)) {
                                 long n = System.currentTimeMillis();
                                 if (n - this.l >= 25L) {
                                    this.l = n;
                                    if (mc.playerController.onPlayerRightClick(this.player(), mc.theWorld, i, pos, m.sideHit, m.hitVec)) {
                                       Utils.Client.setMouseButtonState(1, true);
                                       Utils.Player.legitSwing();
                                       mc.getItemRenderer().resetEquippedProgress();
                                       Utils.Client.setMouseButtonState(1, false);
                                       this.lp = pos;
                                       this.f = 0;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void rd(int i) {
      try {
         if (FastPlace.rightClickDelayTimerField != null) {
            FastPlace.rightClickDelayTimerField.set(mc, i);
         }
      } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
      }
   }

   private void rv() {
      this.lp = null;
      this.lm = null;
      this.f = 0;
   }

}