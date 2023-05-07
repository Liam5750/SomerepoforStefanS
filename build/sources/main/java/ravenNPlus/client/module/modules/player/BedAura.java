package ravenNPlus.client.module.modules.player;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.utils.*;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import java.util.TimerTask;

import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
public class BedAura extends Module {

   public static SliderSetting bedRange;
   public static TickSetting renderBed;
   private java.util.Timer t;
   private BlockPos m = null;

   public BedAura() {
      super("BedAura", ModuleCategory.player, "Destroys beds in a blocks distance");
      this.addSetting(bedRange = new SliderSetting("Range", 5.0D, 2.0D, 10.0D, 1.0D));
      this.addSetting(renderBed = new TickSetting("Render Bed", x));
   }

   public void onEnable() {
      long per = 600L;
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, per);
   }

   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }

      this.m = null;
   }

   public TimerTask t() {
      return new TimerTask() {
         public void run() {
            int range = (int) BedAura.bedRange.getValue();

            for (int y = range; y >= -range; --y) {
               for (int x = -range; x <= range; ++x) {
                  for (int z = -range; z <= range; ++z) {
                     if (Utils.Player.isPlayerInGame()) {
                        BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                        boolean bed = Module.mc.theWorld.getBlockState(p).getBlock() == Blocks.bed;
                        if (BedAura.this.m == p) {
                           if (!bed) {
                              BedAura.this.m = null;
                           }
                        } else if (bed) {
                           BedAura.this.mi(p);
                           BedAura.this.m = p;
                           break;
                        }
                     }
                  }
               }
            }
         }
      };
   }

   private void mi(BlockPos p) {
      if (renderBed.isToggled())
         RoundedUtils.drawRoundedRect(p.getX() - 1, p.getY() - 1, p.getX() + 1, p.getY() + 1, 2, ColorUtil.color_int_min);

      this.sendPacketPlayer(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, p, EnumFacing.NORTH));

      if (Timer.hasTimeElapsed(2500L, true))
         this.sendPacketPlayer(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, p, EnumFacing.NORTH));
   }

}