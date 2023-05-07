package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;
import java.util.Iterator;

public class Tracers extends Module {

   public static TickSetting showInvis, rainbow, bot, friends;
   public static SliderSetting red, green, blue, lineWidth;
   private boolean bobbing;
   private int rgb_c = 0;

   public Tracers() {
      super("Tracers", ModuleCategory.render, "Draws a line to every player");
      this.addSetting(bot = new TickSetting("Sort Bots", x));
      this.addSetting(friends = new TickSetting("Sort Friends", x));
      this.addSetting(showInvis = new TickSetting("Show invis", x));
      this.addSetting(lineWidth = new SliderSetting("Line Width", 1.0D, 1.0D, 5.0D, 0.2D));
      this.addSetting(red = new SliderSetting("Red", 240.0D, 0.0D, 255.0D, 1.0D));
      this.addSetting(green = new SliderSetting("Green", 0.0D, 0.0D, 255.0D, 1.0D));
      this.addSetting(blue = new SliderSetting("Blue", 255.0D, 0.0D, 255.0D, 1.0D));
      this.addSetting(rainbow = new TickSetting("Rainbow", o));
   }

   @Override
   public void onEnable() {
      this.bobbing = mc.gameSettings.viewBobbing;
      if (this.bobbing) {
         mc.gameSettings.viewBobbing = false;
      }
   }

   @Override
   public void onDisable() {
      mc.gameSettings.viewBobbing = this.bobbing;
   }

   public void update() {
      if (mc.gameSettings.viewBobbing) {
         mc.gameSettings.viewBobbing = false;
      }
   }

   @Override
   public void guiUpdate() {
      this.rgb_c = (new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue())).getRGB();
   }

   @SubscribeEvent
   public void o(RenderWorldLastEvent ev) {
      if (this.inGame()) {
         int rgb = rainbow.isToggled() ? Utils.Client.rainbowDraw(2L, 0L) : this.rgb_c;
         Iterator var3;
         if (Client.debugger) {
            var3 = mc.theWorld.loadedEntityList.iterator();

            while (var3.hasNext()) {
               Entity en = (Entity) var3.next();

               if (en instanceof EntityPlayer && en != this.player()) {
                  Tracers.hud(en, rgb, lineWidth.getValueToFloat());
               }
            }
         } else {
            var3 = mc.theWorld.playerEntities.iterator();

            while (true) {
               EntityPlayer en;
               do {
                  do {
                     do {
                        if (!var3.hasNext()) {
                           return;
                        }
                        en = (EntityPlayer) var3.next();
                     } while (en == this.player());
                  } while (en.deathTime != 0);
               } while (!showInvis.isToggled() && en.isInvisible());

               Tracers.hud(en, rgb, lineWidth.getValueToFloat());
            }
         }
      }
   }

   protected static void hud(Entity en, int color, float width) {
      if ( (bot.isToggled() && !NewAntiBot.isBot(en)) || (friends.isToggled() && !Utils.FriendUtils.isAFriend(en)) )
         Utils.HUD.drawTracerLine(en, color, width);
   }

}