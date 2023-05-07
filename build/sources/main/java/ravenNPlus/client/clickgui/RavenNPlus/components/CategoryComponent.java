package ravenNPlus.client.clickgui.RavenNPlus.components;

import ravenNPlus.client.clickgui.RavenNPlus.Component;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.modules.client.CategorySett;
import ravenNPlus.client.module.modules.client.GuiClick;
import ravenNPlus.client.utils.ColorUtil;
import ravenNPlus.client.utils.*;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class CategoryComponent {

   public ArrayList<Component> modulesInCategory = new ArrayList<>();
   public Module.ModuleCategory categoryName;
   private boolean categoryOpened;
   private int width;
   private int y;
   private int x;
   private final int bh;
   public boolean inUse;
   public int xx;
   public int yy;
   public static int iconX;
   public static int iconY;
   public int scrollheight;
   private int categoryHeight;
   private boolean visable = true;
   public boolean n4m = false;
   public String pvp;
   public boolean pin = false;
   private final int chromaSpeed;
   private final double marginY, marginX;
   int rotate = 0;

   public CategoryComponent(Module.ModuleCategory category) {

      this.categoryName = category;
      this.width = 92;
      this.x = 5;
      this.y = 5;
      this.bh = 13;
      this.xx = 0;
      this.categoryOpened = false;
      this.inUse = false;
      this.chromaSpeed = 3;
      int tY = this.bh + 3;
      this.marginX = 80;
      this.marginY = 4.5;

      for (Iterator<Module> var3 = Client.moduleManager.getModulesInCategory(this.categoryName).iterator(); var3.hasNext(); tY += 16) {
         Module mod = var3.next();
         ModuleComponent b = new ModuleComponent(mod, this, tY, null);
         this.modulesInCategory.add(b);
      }
   }

   public ArrayList<Component> getModules() {
      return this.modulesInCategory;
   }

   public void setX(int n) {
      this.x = n;
      if (Client.clientConfig != null) {
         Client.clientConfig.saveConfig();
      }
   }

   public void setY(int y) {
      this.y = y;
      if (Client.clientConfig != null) {
         Client.clientConfig.saveConfig();
      }
   }

   public void mousePressed(boolean d) {
      this.inUse = d;
   }

   public boolean p() {
      return this.pin;
   }

   public void cv(boolean on) {
      this.pin = on;
   }

   public boolean isOpened() {
      return this.categoryOpened;
   }

   public void setOpened(boolean on) {
      this.categoryOpened = on;
      if (Client.clientConfig != null) {
         Client.clientConfig.saveConfig();
      }
   }

   public void rf(FontRenderer renderer) {
      this.width = 92;
      if (!this.modulesInCategory.isEmpty() && this.categoryOpened) {
         categoryHeight = 0;
         Component comp;
         for (Iterator<Component> moduleInCategoryIterator = this.modulesInCategory.iterator(); moduleInCategoryIterator.hasNext(); categoryHeight += comp.getHeight()) {
            comp = moduleInCategoryIterator.next();
         }

         if (GuiClick.rounded.isToggled()) {
            if(GuiClick.guiTheme.getValue() == 6.0D)
               RoundedUtils.drawSmoothRoundedRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.bh + categoryHeight + 4,(float) GuiClick.roundedPerc.getValue(), (new Color(26,25,25, (int) (GuiClick.backgroundOpacity.getValue() / 100 * 255))).getRGB());
            else
               RoundedUtils.drawSmoothRoundedRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.bh + categoryHeight + 4,(float) GuiClick.roundedPerc.getValue(), (new Color(0, 0, 0, (int) (GuiClick.backgroundOpacity.getValue() / 100 * 255))).getRGB());
         } else {
            if(GuiClick.guiTheme.getValue() == 6.0D)
               net.minecraft.client.gui.Gui.drawRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.bh + categoryHeight + 4, (new Color(26,25,25, (int) (GuiClick.backgroundOpacity.getValue() / 100 * 255))).getRGB());
            else
               net.minecraft.client.gui.Gui.drawRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.bh + categoryHeight + 4, (new Color(0, 0, 0, (int) (GuiClick.backgroundOpacity.getValue() / 100 * 255))).getRGB());
         }
      }

      if (GuiClick.categoryBackground.isToggled())
         TickComponent.renderMain((float) (this.x - 2), (float) this.y, (float) (this.x + this.width + 2), (float) (this.y + this.bh + 3), ColorUtil.color_white1);

      Color c = ColorUtil.color_categoryComponent_3;
      int iconSize = CategorySett.iconSize.getValueToInt();
      int iconOffX = CategorySett.xIconOffset.getValueToInt();
      int iconOffY = CategorySett.yIconOffset.getValueToInt();

      int iconX = this.x + iconOffX;
      int iconY = this.y + iconOffY;

      int categoryNameOffsett = CategorySett.TextOffset.getValueToInt();

      if (categoryOpened) {
         if (CategorySett.Icon_client.isToggled())
            if (categoryName.name().equals("client"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.client_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/client.png", iconX, iconY, iconSize, iconSize, CategorySett.client_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/client.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_move.isToggled())
            if (categoryName.name().equals("movement"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.move_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/move.png", iconX, iconY, iconSize, iconSize, CategorySett.move_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/move.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_comb.isToggled())
            if (categoryName.name().equals("combat"))
               if (CategorySett.costum_color.isToggled()) {
                  // renderer.drawString("", 0, 0, CategorySett.comb_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/combat.png", iconX, iconY, iconSize, iconSize, CategorySett.comb_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/combat.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_player.isToggled())
            if (categoryName.name().equals("player"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.player_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/player.png", iconX, iconY, iconSize, iconSize, CategorySett.player_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/player.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_hotkey.isToggled())
            if (categoryName.name().equals("hotkey"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.hotkey_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/hotkey.png", iconX, iconY, iconSize, iconSize, CategorySett.hotkey_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/hotkey.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_render.isToggled())
            if (categoryName.name().equals("render"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.render_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/render.png", iconX, iconY, iconSize, iconSize, CategorySett.render_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/render.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_other.isToggled())
            if (categoryName.name().equals("other"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.other_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/other.png", iconX, iconY, iconSize, iconSize, CategorySett.other_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/other.png", iconX, iconY, iconSize, iconSize, c);
               }

         if (CategorySett.Icon_mini.isToggled())
            if (categoryName.name().equals("minigame"))
               if (CategorySett.costum_color.isToggled()) {
                  //   renderer.drawString("", 0, 0, CategorySett.mini_color.getRGB());
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/mini.png", iconX, iconY, iconSize, iconSize, CategorySett.mini_color);
               } else {
                  RenderUtils.draw2DImageByString("ravenNPlus/icon/modules/mini.png", iconX, iconY, iconSize, iconSize, c);
               }

         if ((CategorySett.Icon_client.isToggled() || CategorySett.Icon_comb.isToggled() || CategorySett.Icon_hotkey.isToggled()
                 || CategorySett.Icon_mini.isToggled() || CategorySett.Icon_move.isToggled() || CategorySett.Icon_other.isToggled()
                 || CategorySett.Icon_player.isToggled() || CategorySett.Icon_render.isToggled()) && categoryOpened)
            RenderUtils.drawCircle(0, 0, 0, 0, 0);
      } else {

         RenderUtils.draw2DImage(RenderUtils.stringShadow, this.x + categoryNameOffsett, this.y, renderer.getStringWidth(categoryName.name()) + 4, 17, ColorUtil.color_categoryComponent_3);
      }

      renderer.drawString(this.n4m ? this.pvp : this.categoryName.name(), (float) (this.x + categoryNameOffsett), (float) (this.y + 4), Color.getHSBColor((float) (System.currentTimeMillis() % (7500L / (long) this.chromaSpeed)) / (7500.0F / (float) this.chromaSpeed), 1.0F, 1.0F).getRGB(), false);

      if (!this.n4m) {
         //                                                         - +  |  x -
         //renderer.drawString(this.categoryOpened ? "x" : "-", (float)(this.x + marginX), (float)((double)this.y + marginY), ColorUtil.color_white2, false);

         GL11.glPushMatrix();

         //RenderUtils.drawHover2DImageByString(this.categoryOpened ? "ravenNPlus/icon/vape/ex.png" : "ravenNPlus/icon/vape/exo.png", (int) (this.x + marginX)+0, (int) ((double) this.y + marginY)+0, iconSize+0, iconSize+0, Mouse.getX()+0, Mouse.getY()+0, c);

         int arrowX = (int) (this.x + marginX);
         int arrowY = (int) ((double) this.y + marginY - 2);

         RenderUtils.draw2DImageByString(this.categoryOpened ? "ravenNPlus/icon/modules/exo.png" : "ravenNPlus/icon/modules/ex.png", arrowX, arrowY, iconSize, iconSize, c);
         GL11.glPopMatrix();

         if (this.categoryOpened && !this.modulesInCategory.isEmpty()) {
            for (Component c2 : this.modulesInCategory) {
               c2.draw();
            }
         }
      }
   }

   public void r3nd3r() {
      int o = this.bh + 3;
      Component c;
      for (Iterator<Component> var2 = this.modulesInCategory.iterator(); var2.hasNext(); o += c.getHeight()) {
         c = var2.next();
         c.setComponentStartAt(o);
      }
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getIconX() {
      return iconX;
   }

   public int getIconY() {
      return iconY;
   }

   public int getWidth() {
      return this.width;
   }

   public void up(int x, int y) {
      if (this.inUse) {
         this.setX(x - this.xx);
         this.setY(y - this.yy);
      }
   }

   public boolean i(int x, int y) {
      return x >= this.x + 92 - 13 && x <= this.x + this.width && (float) y >= (float) this.y + 2.0F && y <= this.y + this.bh + 1;
   }

   public boolean mousePressed(int x, int y) {
      return x >= this.x + 77 && x <= this.x + this.width - 6 && (float) y >= (float) this.y + 2.0F && y <= this.y + this.bh + 1;
   }

   public boolean insideArea(int x, int y) {
      return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.bh;
   }

   public String getName() {
      return String.valueOf(modulesInCategory);
   }

   public void setLocation(int parseInt, int parseInt1) {
      this.x = parseInt;
      this.y = parseInt1;
   }

   public int getActualHeight() {
      int h = this.bh + 16 + categoryHeight;
      for (Component c : getModules()) {
         h += c.getHeight();
      }
      return h;
   }

   public boolean insideAllArea(int x, int y) {
      return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.getActualHeight() - this.scrollheight;
   }

   public int getHeight() {
      return this.bh;
   }

   public void scroll(float ss) {
      if (ss > 0 || (getActualHeight() + ss) > 100) {
         scrollheight += ss;
      }
      if (scrollheight <= 0) {
         r3nd3r();
      } else {
         scrollheight = 0;
      }
   }

   public void setVisable(boolean vis) {
      this.visable = vis;
   }

   public boolean isVisable() {
      return visable;
   }

}