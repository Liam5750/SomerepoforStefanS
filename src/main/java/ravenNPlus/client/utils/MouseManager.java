package ravenNPlus.client.utils;

import ravenNPlus.client.main.Client;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MouseManager {

   private static final java.util.List<Long> leftClicks = new java.util.ArrayList<>();
   private static final java.util.List<Long> rightClicks = new java.util.ArrayList<>();
   public static long leftClickTimer = 0L;
   public static long rightClickTimer = 0L;

   @SubscribeEvent
   public void onMouseUpdate(MouseEvent mouse) {
      if (mouse.buttonstate) {
         if (mouse.button == 0) {
            addLeftClick();
            if (Client.debugger && net.minecraft.client.Minecraft.getMinecraft().objectMouseOver != null) {
               net.minecraft.entity.Entity en = net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.entityHit;
               if (en == null) return;

               Utils.Player.sendMessageToSelf("&7&m-------------------------");
               Utils.Player.sendMessageToSelf("n: " + en.getName());
               Utils.Player.sendMessageToSelf("rn: " + en.getName().replace("§", "%"));
               Utils.Player.sendMessageToSelf("d: " + en.getDisplayName().getUnformattedText());
               Utils.Player.sendMessageToSelf("rd: " + en.getDisplayName().getUnformattedText().replace("§", "%"));
               Utils.Player.sendMessageToSelf("b?: " + ravenNPlus.client.module.modules.combat.NewAntiBot.isBot(en));
               Utils.Player.sendMessageToSelf("&7&m-------------------------");
            }
         } else if (mouse.button == 1) {
            addRightClick();
         }
      }
   }

   public static void addLeftClick() {
      leftClicks.add(leftClickTimer = System.currentTimeMillis());
   }

   public static void addRightClick() {
      rightClicks.add(rightClickTimer = System.currentTimeMillis());
   }

   public static int getLeftClickCounter() {
      if (!Utils.Player.isPlayerInGame()) return leftClicks.size();
      for (Long lon : leftClicks) {
         if (lon < System.currentTimeMillis() - 1000L) {
            leftClicks.remove(lon);
            break;
         }
      }
      return leftClicks.size();
   }

   public static int getRightClickCounter() {
      if (!Utils.Player.isPlayerInGame()) return leftClicks.size();
      for (Long lon : rightClicks) {
         if (lon < System.currentTimeMillis() - 1000L) {
            rightClicks.remove(lon);
            break;
         }
      }
      return rightClicks.size();
   }

}