package ravenNPlus.client.module.modules.client;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import ravenNPlus.client.clickgui.RavenNPlus.components.CategoryComponent;

public class GuiClick extends Module {

   public static final int bind = 54;
   public static TickSetting showPlayer, blur, monkeee, backGround, time, date, notifications, cleanUp, categoryBackground, rounded, logo;
   public static DescriptionSetting guiThemeDesc;
   public static SliderSetting guiTheme, backgroundOpacity, roundedPerc;

   public GuiClick() {
      super("ClickGUI", ModuleCategory.client, "");
      withKeycode(bind);

      this.addSetting(guiTheme = new SliderSetting("Theme", 6, 1, 7, 1));
      this.addSetting(guiThemeDesc = new DescriptionSetting(Utils.th + ""));
      this.addSetting(backgroundOpacity = new SliderSetting("Category Opacity %", 85, 0, 100, 1));
      this.addSetting(categoryBackground = new TickSetting("Category Background", false));
      this.addSetting(backGround = new TickSetting("Background", false));
      this.addSetting(cleanUp = new TickSetting("Clean", false));
      this.addSetting(rounded = new TickSetting("Rounded Corners", true));
      this.addSetting(roundedPerc = new SliderSetting("Rounded Corners %", 8, 1, 90, 1));
      this.addSetting(logo = new TickSetting("ClickGUI Logo", true));
      this.addSetting(showPlayer = new TickSetting("Show Player", true));
      this.addSetting(blur = new TickSetting("Blur", true));
      this.addSetting(monkeee = new TickSetting("Discord Monke icon", false));
      this.addSetting(date = new TickSetting("Date", true));
      this.addSetting(time = new TickSetting("Clock", true));
      this.addSetting(notifications = new TickSetting("Notifications", true));
   }

   @Override
   public void guiButtonToggled(TickSetting setting) {
      if (setting == cleanUp) {
         cleanUp.disable();
         for (CategoryComponent cc : Client.clickGui.getCategoryList()) {
            cc.setX((cc.getX() / 50 * 50) + (cc.getX() % 50 > 25 ? 50 : 0));
            cc.setY((cc.getY() / 50 * 50) + (cc.getY() % 50 > 25 ? 50 : 0));
         }
      }
   }

   public void onEnable() {
      if (this.inGame() && mc.currentScreen != Client.clickGui) {
         mc.displayGuiScreen(Client.clickGui);
         Client.clickGui.initMain();
      }

      this.disable();
   }

   public void guiUpdate() {
      if (guiTheme.getValueToInt() == 6 && backgroundOpacity.getValue() < 74) {
         backgroundOpacity.setValue(75);
      }

      // modes : 1=b1, 2=b2, 3=b3, 4=b+, 5=n+, 6=Vape, 7=NoModulesFake
      switch (guiTheme.getValueToInt()) {
         case 1:
            guiThemeDesc.setDesc(Utils.th + "b1");
            break;
         case 2:
            guiThemeDesc.setDesc(Utils.th + "b2");
            break;
         case 3:
            guiThemeDesc.setDesc(Utils.th + "b3");
            break;
         case 4:
            guiThemeDesc.setDesc(Utils.th + "b+");
            break;
         case 5:
            guiThemeDesc.setDesc(Utils.th + "N+");
            break;
         case 6:
            guiThemeDesc.setDesc(Utils.th + "Vape");
            break;
         case 7:
            guiThemeDesc.setDesc(Utils.th + "NoModulesFake");
            break;
      }
   }

}