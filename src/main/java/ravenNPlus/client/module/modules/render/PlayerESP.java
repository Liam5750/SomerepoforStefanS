package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.Color;
import java.util.Iterator;

public class PlayerESP extends Module {

   public static DescriptionSetting g;
   public static SliderSetting a, b, c, i, j;
   public static TickSetting d, f, h, t1, t2, t3, t4, t5, t6, t7, t8;
   private int rgb_c = 0;

   public PlayerESP() {
      super("PlayerESP", ModuleCategory.render, "Draw a outline around Players");
      this.addSetting(a = new SliderSetting("Red", 0.0D, 0.0D, 255.0D, 1.0D));
      this.addSetting(b = new SliderSetting("Green", 255.0D, 0.0D, 255.0D, 1.0D));
      this.addSetting(c = new SliderSetting("Blue", 0.0D, 0.0D, 255.0D, 1.0D));
      this.addSetting(d = new TickSetting("Rainbow", false));
      this.addSetting(g = new DescriptionSetting("ESP Types"));
      this.addSetting(t3 = new TickSetting("2D", false));
      this.addSetting(t5 = new TickSetting("Arrow", false));
      this.addSetting(t1 = new TickSetting("Box", false));
      this.addSetting(t4 = new TickSetting("Health", true));
      this.addSetting(t6 = new TickSetting("Ring", false));
      this.addSetting(t8 = new TickSetting("Render Friends", false));
      this.addSetting(t2 = new TickSetting("Shaded", false));
      this.addSetting(i = new SliderSetting("Expand", 0.0D, -0.3D, 2.0D, 0.1D));
      this.addSetting(j = new SliderSetting("X-Shift", 0.0D, -35.0D, 10.0D, 1.0D));
      this.addSetting(f = new TickSetting("Show invis", true));
      this.addSetting(h = new TickSetting("Red on damage", true));
      this.addSetting(t7 = new TickSetting("Match Chestplate", false));
   }

   public void onDisable() {
      Utils.HUD.ring_c = false;
   }

   public void guiUpdate() {
      this.rgb_c = (new Color((int) a.getValue(), (int) b.getValue(), (int) c.getValue())).getRGB();
   }

   @SubscribeEvent
   public void r1(RenderWorldLastEvent e) {
      if (!this.inGame()) return;
      int rgb = d.isToggled() ? 0 : this.rgb_c;
      Iterator var3;
      if (Client.debugger) {
         var3 = mc.theWorld.loadedEntityList.iterator();

         while (var3.hasNext()) {
            Entity en = (Entity) var3.next();
            if (en instanceof EntityLivingBase && en != this.player()) {
               this.r(en, rgb);
            }
         }
      } else {
         var3 = mc.theWorld.playerEntities.iterator();
         while (true) {
            EntityPlayer en;
            do {
               do {
                  do {
                     if (!var3.hasNext()) return;

                     en = (EntityPlayer) var3.next();
                  } while (en == this.player());
               } while (en.deathTime != 0);
            } while (!f.isToggled() && en.isInvisible());

            if (!NewAntiBot.isBot(en)) {
               if (t7.isToggled() && getColor(en.getCurrentArmor(2)) > 0) {
                  int E = new Color(getColor(en.getCurrentArmor(2))).getRGB();
                  this.r(en, E);
               } else {
                  this.r(en, rgb);
               }
            }
         }
      }
   }

   public int getColor(ItemStack stack) {
      if (stack == null)
         return -1;
      NBTTagCompound nbt = stack.getTagCompound();
      if (nbt != null) {
         NBTTagCompound nbttagcompound1 = nbt.getCompoundTag("display");
         if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
            return nbttagcompound1.getInteger("color");
         }
      }

      return -2;
   }

   private void r(Entity en, int rgb) {
      if (t8.isToggled() && Utils.FriendUtils.isAFriend(en))
         return;

      if (t1.isToggled())
         Utils.HUD.drawBoxAroundEntity(en, 1, i.getValue(), j.getValue(), rgb, h.isToggled());

      if (t2.isToggled())
         Utils.HUD.drawBoxAroundEntity(en, 2, i.getValue(), j.getValue(), rgb, h.isToggled());

      if (t3.isToggled())
         Utils.HUD.drawBoxAroundEntity(en, 3, i.getValue(), j.getValue(), rgb, h.isToggled());

      if (t4.isToggled())
         Utils.HUD.drawBoxAroundEntity(en, 4, i.getValue(), j.getValue(), rgb, h.isToggled());

      if (t5.isToggled())
         Utils.HUD.drawBoxAroundEntity(en, 5, i.getValue(), j.getValue(), rgb, h.isToggled());

      if (t6.isToggled())
         Utils.HUD.drawBoxAroundEntity(en, 6, i.getValue(), j.getValue(), rgb, h.isToggled());
   }

}