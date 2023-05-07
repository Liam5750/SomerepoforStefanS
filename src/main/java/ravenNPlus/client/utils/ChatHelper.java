package ravenNPlus.client.utils;

import ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatHelper {

   private static boolean e = false;
   private static long s = 0L;

   @SubscribeEvent
   public void onChatMessageReceived(ClientChatReceivedEvent event) {
      if (e && Utils.Player.isPlayerInGame()) {
         if (Utils.Java.str(event.message.getUnformattedText()).startsWith("Unknown")) {
            event.setCanceled(true);
            e = false;
            this.getPing();
         }
      }
   }

   public static void checkPing() {
      CommandPrompt.print("Checking...");
      if (e) {
         CommandPrompt.print("Please wait.");
      } else {
         Utils.mc.thePlayer.sendChatMessage("/...");
         e = true;
         s = System.currentTimeMillis();
      }
   }

   private void getPing() {
      int ping = (int)(System.currentTimeMillis() - s) - 20;
      if (ping < 0) {
         ping = 0;
      }

      CommandPrompt.print("Your ping: " + ping + "ms");
      reset();
   }

   public static void reset() {
      e = false;
      s = 0L;
   }

}