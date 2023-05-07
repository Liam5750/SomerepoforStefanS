package ravenNPlus.client.module.modules.other;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import ravenNPlus.client.module.modules.minigames.DuelsStats;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;

public class NameHider extends Module {

   public static DescriptionSetting a;
   public static String name = "raven n+";
   public static boolean isEnabled = false;

   public NameHider() {
      super("Name Hider", ModuleCategory.other, "Not working ?");
      this.addSetting(a = new DescriptionSetting(Utils.Java.capitalizeWord("command") + ": cname [name]"));
   }

   public static String getUnformattedTextForChat(String s) {
      if (mc.thePlayer != null) {
         s = DuelsStats.playerNick.isEmpty() ? s.replace(mc.thePlayer.getName(), name) : s.replace(DuelsStats.playerNick, name);
      }

      return s;
   }

   @Override
   public void onEnable() {
      isEnabled = true;
   }

   @Override
   public void onDisable() {
      isEnabled = false;
   }

   @SubscribeEvent
   public void onText(final ClientChatReceivedEvent e) {
      if (this.isEnabled()) isEnabled = true;
      if (!this.isEnabled()) isEnabled = false;

      ChatStyle x = new ChatStyle();
      x.setColor(EnumChatFormatting.GOLD);
      ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, Client.discord);
      x.setChatClickEvent(click);
      e.message.setChatStyle(x);

      if (e.message.getUnformattedText().contains(mc.getSession().getUsername())) {
         StringUtils.replace(mc.getNetHandler().getGameProfile().getName(), e.message.getUnformattedText(), x.getChatClickEvent().toString());
         e.setCanceled(true);
      }
   }

}