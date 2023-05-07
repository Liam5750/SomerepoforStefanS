package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.TimerTask;

public class Xray extends Module {

   public static SliderSetting range;
   public static TickSetting iron, gold, diamond, emerald, lapis, redstone, coal, spawner;
   private java.util.Timer t;
   private List<BlockPos> ren;

   public Xray() {
      super("XRay", ModuleCategory.render, "Draw a outline around ores");
      this.addSetting(range = new SliderSetting("Range", 80.0D, 5.0D, 200.0D, 1.0D));
      this.addSetting(spawner = new TickSetting("Spawner", true));
      this.addSetting(diamond = new TickSetting("Diamond", true));
      this.addSetting(emerald = new TickSetting("Emerald", true));
      this.addSetting(gold = new TickSetting("Gold", true));
      this.addSetting(lapis = new TickSetting("Lapis", true));
      this.addSetting(iron = new TickSetting("Iron", true));
      this.addSetting(coal = new TickSetting("Coal", true));
      this.addSetting(redstone = new TickSetting("Redstone", true));
   }

   @Override
   public void onEnable() {
      this.ren = new ArrayList<>();
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 200L);
   }

   @Override
   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }
   }

   private TimerTask t() {
      return new TimerTask() {
         public void run() {
            Xray.this.ren.clear();
            int ra = (int) Xray.range.getValue();

            for (int y = ra; y >= -ra; --y) {
               for (int x = -ra; x <= ra; ++x) {
                  for (int z = -ra; z <= ra; ++z) {
                     if (Utils.Player.isPlayerInGame()) {
                        BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double) x, Module.mc.thePlayer.posY + (double) y, Module.mc.thePlayer.posZ + (double) z);
                        Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                        if (Xray.iron.isToggled() && bl.equals(Blocks.iron_ore) || Xray.gold.isToggled() && bl.equals(Blocks.gold_ore) ||
                                Xray.diamond.isToggled() && bl.equals(Blocks.diamond_ore) || Xray.emerald.isToggled() && bl.equals(Blocks.emerald_ore) ||
                                Xray.lapis.isToggled() && bl.equals(Blocks.lapis_ore) || Xray.redstone.isToggled() && bl.equals(Blocks.redstone_ore) ||
                                Xray.coal.isToggled() && bl.equals(Blocks.coal_ore) || Xray.spawner.isToggled() && bl.equals(Blocks.mob_spawner)) {
                           Xray.this.ren.add(p);
                        }
                     }
                  }
               }
            }
         }
      };
   }

   @SubscribeEvent
   public void orl(RenderWorldLastEvent ev) {
      if (this.inGame() && !this.ren.isEmpty()) {
         List<BlockPos> tRen = new ArrayList<>(this.ren);

         for (BlockPos p : tRen) {
            this.dr(p);
         }
      }
   }

   private void dr(BlockPos p) {
      int[] rgb = this.c(mc.theWorld.getBlockState(p).getBlock());
      if (rgb[0] + rgb[1] + rgb[2] != 0) {
         Utils.HUD.renderBlockOutlines(p, (new Color(rgb[0], rgb[1], rgb[2])).getRGB(), true);
      }
   }

   private int[] c(Block b) {
      int red = 0;
      int green = 0;
      int blue = 0;

      if (b.equals(Blocks.iron_ore)) {
         red = 255;
         green = 255;
         blue = 255;
      } else if (b.equals(Blocks.gold_ore)) {
         red = 255;
         green = 255;
      } else if (b.equals(Blocks.diamond_ore)) {
         green = 220;
         blue = 255;
      } else if (b.equals(Blocks.emerald_ore)) {
         red = 35;
         green = 255;
      } else if (b.equals(Blocks.lapis_ore)) {
         green = 50;
         blue = 255;
      } else if (b.equals(Blocks.redstone_ore)) {
         red = 255;
      } else if (b.equals(Blocks.mob_spawner)) {
         red = 30;
         blue = 135;
      }

      return new int[]{red, green, blue};
   }

}