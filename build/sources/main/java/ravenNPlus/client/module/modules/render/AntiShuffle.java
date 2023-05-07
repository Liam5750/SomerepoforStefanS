package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;

public class AntiShuffle extends Module {
   public static DescriptionSetting a;
   private static final String c = "§k";

   public AntiShuffle() {
      super("AntiShuffle", ModuleCategory.render, "Removes " + c);
      this.addSetting(a = new DescriptionSetting(Utils.Java.capitalizeWord("remove ") + c));
   }

   public static String getUnformattedTextForChat(String s) {
      return s.replace(c, "");
   }

}