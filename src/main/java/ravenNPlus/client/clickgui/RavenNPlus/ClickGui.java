package ravenNPlus.client.clickgui.RavenNPlus;

import ravenNPlus.client.utils.*;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.version.Version;
import ravenNPlus.client.module.modules.client.GuiClick;
import ravenNPlus.client.utils.ColorUtil;
import ravenNPlus.client.module.modules.client.ModSettings;
import ravenNPlus.client.module.modules.client.CategorySett;
import ravenNPlus.client.clickgui.RavenNPlus.components.CategoryComponent;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.time.format.DateTimeFormatter;

public class ClickGui extends GuiScreen {

   private ScheduledFuture<?> sf;
   private Timer aT, aL, aE,  aR;
   protected boolean allowBlur = false;
   private final  ArrayList<CategoryComponent> categoryList;
   public final CommandPrompt commandPrompt;
   private int mouseX, mouseY;

   public ClickGui() {
      this.commandPrompt = new CommandPrompt();
      this.categoryList = new ArrayList<>();
      int topOffset = 5;
      Module.ModuleCategory[] values;
      int categoryAmount = (values = Module.ModuleCategory.values()).length;

      for(int category = 0; category < categoryAmount; ++category) {
         Module.ModuleCategory moduleCategory = values[category];
         CategoryComponent currentModuleCategory = new CategoryComponent(moduleCategory);
         currentModuleCategory.setY(topOffset);
         categoryList.add(currentModuleCategory);
         topOffset += 20;
      }

      commandPrompt.setLocation(5, topOffset);
      commandPrompt.setSize((int) (92 * 1.5), (int) ((92 * 1.5) * 0.75));
   }

   public void initMain() {
      (this.aT = this.aE = this.aR = new Timer(500.0F)).start();
      this.sf = Client.getExecutor().schedule(() -> (
              this.aL = new Timer(650.0F)
      ).start(), 650L, TimeUnit.MILLISECONDS);

      if(GuiClick.blur.isToggled() && allowBlur) {
         BlurUtils.blur(true);
         allowBlur = false;
      }
   }

   public void initGui() { super.initGui(); }

   public void drawScreen(int x, int y, float p) {

      mouseX = x; mouseY = y;
      Version clientVersion = Client.versionManager.getClientVersion();
      Version latestVersion = Client.versionManager.getLatestVersion();

      //background
      if(GuiClick.backGround.isToggled()) {
         drawRect(0, 0, this.width, this.height, (int)(this.aR.getValueFloat(0.0F, 0.7F, 2) * 255.0F) << 24);
      }

      int sH = this.height / 4;
      int sW = this.width / 2;
      int w_c = 30 - this.aT.getValueInt(0, 30, 3);

      /* ---- Old ----
      this.drawCenteredString(this.fontRendererObj, "r", sW + 1 - w_c, sH - 23 + off , Utils.Client.rainbowDraw(2L, 900L+300L));
      this.drawCenteredString(this.fontRendererObj, "a", sW - w_c, sH - 13 + off , Utils.Client.rainbowDraw(2L, 900L+150L));
      this.drawCenteredString(this.fontRendererObj, "v", sW - w_c, sH - 4  + off , ColorUtil.color_clickGui2);
      this.drawCenteredString(this.fontRendererObj, "e", sW - w_c, sH + 7  + off , Utils.Client.rainbowDraw(2L, 900L-150L));
      this.drawCenteredString(this.fontRendererObj, "n", sW - w_c, sH + 17 + off , Utils.Client.rainbowDraw(2L, 900L-300L));
      */

      float speed = 4890;

      if (latestVersion.isNewerThan(clientVersion)) {
         int margin = 2;
         int rows = 1;
         for (int i = Client.updateText.length-1; i >= 0; i--) {
            String up = Client.updateText[i];
            mc.fontRendererObj.drawString(up, sW - this.fontRendererObj.getStringWidth(up) / 2, this.height - this.fontRendererObj.FONT_HEIGHT * rows - margin, Utils.Client.astolfoColorsDraw(10, 28, speed));
            rows++;
            margin += 2;
         }
      } else {
         mc.fontRendererObj.drawStringWithShadow("["+ Client.name+ "] | Config: " + Client.configManager.getConfig().getName()+"",
            4, this.height - 3 - mc.fontRendererObj.FONT_HEIGHT, Utils.Client.astolfoColorsDraw(10, 14, speed));
      }

      int a = 10;  //30
      int b = 10;  //10
      int c = 23;  //43
      int aniProg;

      int color_clickGui1 = ravenNPlus.client.utils.Utils.Client.rainbowDraw(2L, 900L - 300L);
      int color_clickGui2 = ravenNPlus.client.utils.Utils.Client.rainbowDraw(2L, 900L);

      if (GuiClick.logo.isToggled()) {
         if (!ModSettings.text_underline.isToggled())
            this.drawCenteredString(this.fontRendererObj, "N+", sW + 1 - w_c, sH + 4, color_clickGui1);
         else
            FontUtils.drawStringWithShadowWithUnderline("N+", sW - 5 - w_c, sH + 4, color_clickGui1);

         this.drawVerticalLine(sW - b - w_c, sH - a, sH + c, color_clickGui2);
         this.drawVerticalLine(sW + b + w_c, sH - a, sH + c, color_clickGui2);

         if (this.aL != null) {
            aniProg = this.aL.getValueInt(0, 20, 2);

            this.drawHorizontalLine(sW - b, sW - b + aniProg, sH - a - 1, color_clickGui2);
            this.drawHorizontalLine(sW + b, sW + b - aniProg, sH + c + 1, color_clickGui2);
         }
      }

      for (CategoryComponent category : categoryList) {
         category.rf(this.fontRendererObj);
         category.up(x, y);

         for (Component module : category.getModules()) {
            module.update(x, y);
         }
      }

      // time
      if(GuiClick.time.isToggled())
         mc.fontRendererObj.drawStringWithShadow(Calendar.getInstance().getTime().getHours()
                 + ":" + Calendar.getInstance().getTime().getMinutes() + ":" + Calendar.getInstance().getTime().getSeconds(), 8, 5, color_clickGui2);

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      LocalDateTime now = LocalDateTime.now();

      // date
      if(GuiClick.date.isToggled())
         Utils.HUD.fontRender.drawStringWithShadow(GuiClick.time.isToggled() ? " | "+dtf.format(now) : ""+dtf.format(now), GuiClick.time.isToggled() ?
            8+fontRendererObj.getStringWidth(Calendar.getInstance().getTime().getHours() + ":" + Calendar.getInstance().getTime().getMinutes()
            + ":" + Calendar.getInstance().getTime().getSeconds()) : 5, 5, color_clickGui2);

      // clean player color
      if(GuiClick.date.isToggled() || GuiClick.time.isToggled())
         ColorUtil.color_clear(true);

      // render player
      if(GuiClick.showPlayer.isToggled())
         GuiInventory.drawEntityOnScreen(this.width + 15 - this.aE.getValueInt(0, 40, 2), this.height - 19
            - this.fontRendererObj.FONT_HEIGHT,40, (float) (this.width - 25 - x), (float) (this.height - 50 - y), this.mc.thePlayer);

      // clean player color
      if(GuiClick.showPlayer.isToggled())
         ColorUtil.color_clear(true);

      commandPrompt.update(x, y);
      commandPrompt.draw();
   }

   public void mouseClicked(int x, int y, int mouseButton) {
      Iterator<CategoryComponent> btnCat = categoryList.iterator();

      commandPrompt.mouseDown(x, y, mouseButton);
      if(commandPrompt.overPosition(x, y)) return;

      while(true) {
         CategoryComponent category;
         do {
            do {
               if (!btnCat.hasNext()) {
                  return;
               }

               category = btnCat.next();
               if (category.insideArea(x, y) && !category.i(x, y) && !category.mousePressed(x, y) && mouseButton == 0) {
                  category.mousePressed(true);
                  category.xx = x - category.getX();
                  category.yy = y - category.getY();
               }

               if (category.mousePressed(x, y) && mouseButton == 0) {
                  category.setOpened(!category.isOpened());
               }

               if (category.i(x, y) && mouseButton == 0) {
                  category.cv(!category.p());

                  if(CategorySett.sounds.isToggled()) {
                     if (CategorySett.category_mode.getValueToInt() == 1)
                        SoundUtil.play(SoundUtil.click1, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());

                     if (CategorySett.category_mode.getValueToInt() == 2)
                        SoundUtil.play(SoundUtil.bowhit, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());

                     if (CategorySett.category_mode.getValueToInt() == 3)
                        SoundUtil.play(SoundUtil.playerHurt, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());

                     if (CategorySett.category_mode.getValueToInt() == 4)
                        SoundUtil.play(SoundUtil.playerDie, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());

                     if (CategorySett.category_mode.getValueToInt() == 5)
                        SoundUtil.play(SoundUtil.chestOpen, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());

                     if (CategorySett.category_mode.getValueToInt() == 6)
                        SoundUtil.play(SoundUtil.chestClose, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());

                     if (CategorySett.category_mode.getValueToInt() == 7)
                        SoundUtil.play(SoundUtil.tntExplosion, CategorySett.category_volume.getValueToFloat(), CategorySett.category_pitch.getValueToFloat());
                  }
               }
            } while(!category.isOpened());
         } while(category.getModules().isEmpty());

         for (Component c : category.getModules()) {
            c.mouseDown(x, y, mouseButton);
         }
      }
   }

   public void mouseReleased(int x, int y, int s) {
      commandPrompt.mouseReleased(x, y, s);
      if(commandPrompt.overPosition(x, y)) return;

      if (s == 0) {
         Iterator<CategoryComponent> btnCat = categoryList.iterator();

         CategoryComponent c4t;
         while(btnCat.hasNext()) {
            c4t = btnCat.next();
            c4t.mousePressed(false);
         }

         btnCat = categoryList.iterator();

         while(true) {
            do {
               do {
                  if (!btnCat.hasNext()) {
                     return;
                  }

                  c4t = btnCat.next();
               } while(!c4t.isOpened());
            } while(c4t.getModules().isEmpty());

            for (Component c : c4t.getModules()) {
               c.mouseReleased(x, y, s);
            }
         }
      }
      if(Client.clientConfig != null) {
         Client.clientConfig.saveConfig();
      }
   }

   public void func_73869_a(char t, int k) {
      commandPrompt.keyTyped(t, k);
      if (k == 1) {
         this.mc.displayGuiScreen(null);
      } else {
         Iterator<CategoryComponent> btnCat = categoryList.iterator();

         while(true) {
            CategoryComponent cat;
            do {
               do {
                  if (!btnCat.hasNext()) {
                     return;
                  }

                  cat = btnCat.next();
               } while(!cat.isOpened());
            } while(cat.getModules().isEmpty());

            for (Component c : cat.getModules()) {
               c.keyTyped(t, k);
            }
         }
      }
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      for (CategoryComponent c : visableCategoryList()) {
         if (c.insideAllArea(mouseX, mouseY)) {
            int i = org.lwjgl.input.Mouse.getEventDWheel();
            i = Integer.compare(i, 0);
            c.scroll(i * 5f);
         }
      }
   }

   public void onGuiClosed() {
      this.aL = null;

      if (this.sf != null) {
         this.sf.cancel(true);
         this.sf = null;
      }

      Client.configManager.save();
      Client.clientConfig.saveConfig();


      if(GuiClick.blur.isToggled() && !allowBlur) {
         BlurUtils.blur(false);
         allowBlur = true;
      }

      if(!GuiClick.blur.isToggled() && !allowBlur) {
         BlurUtils.blur(false);
      }
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public ArrayList<CategoryComponent> getCategoryList() {
      return categoryList;
   }

   public ArrayList<CategoryComponent> visableCategoryList() {
      ArrayList<CategoryComponent> newList = (ArrayList<CategoryComponent>) categoryList.clone();
      newList.removeIf(obj -> !obj.isVisable());
      return newList;
   }

}