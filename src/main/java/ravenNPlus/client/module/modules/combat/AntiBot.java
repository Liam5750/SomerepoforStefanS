package ravenNPlus.client.module.modules.combat;
/*
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.notifications.Type;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.modules.player.Freecam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class AntiBot extends Module {

   private static final HashMap<EntityPlayer, Long> newEnt = new HashMap<>();
   private final long ms = 4000L;
   public static boolean isInHyp = false;
   public static TickSetting checkDelay, nameChecks, playerChecks, playerInTabChecks;
   public static TickSetting rideChecks, pushChecks, invChecks, invisChecks, hypCheck, armorStandCheck;

   public AntiBot() {
      super("AntiBot", ModuleCategory.combat, "Sorts Bots out");
      withEnabled(true);
      this.addSetting(checkDelay = new TickSetting("Wait 80 ticks before checking", false));
      this.addSetting(playerInTabChecks = new TickSetting("Tab checks", true));
      this.addSetting(playerChecks = new TickSetting("Player checks", true));
      this.addSetting(nameChecks = new TickSetting("Name checks", true));
      this.addSetting(rideChecks = new TickSetting("Ride checks", true));
      this.addSetting(pushChecks = new TickSetting("Push checks", true));
      this.addSetting(invisChecks = new TickSetting("Invis checks", true));
      this.addSetting(invChecks = new TickSetting("Inventory checks", true));
      this.addSetting(armorStandCheck = new TickSetting("Armor Stand checks", false));
      this.addSetting(hypCheck = new TickSetting("Hypixel Bot checks", false));
   }

   @Override
   public void onDisable() {
      newEnt.clear();

      if (isInHyp)
         isInHyp = false;
   }

   @SubscribeEvent
   public void onEntityJoinWorld(EntityJoinWorldEvent event) {
      if (!this.inGame()) return;
      if (checkDelay.isToggled() && event.entity instanceof EntityPlayer && event.entity != this.player()) {
         newEnt.put((EntityPlayer) event.entity, System.currentTimeMillis());
      }

      if (Utils.Client.isHyp() && !isInHyp) {
         hypCheck.enable();
         this.notification(Type.INFO, "Enabled Hypixel Bot Checks", 5);
         isInHyp = true;
      }
   }

   @Override
   public void update() {
      if (checkDelay.isToggled() && !newEnt.isEmpty()) {
         newEnt.values().removeIf((e) -> e < System.currentTimeMillis() - ms);
      }
   }

   public static boolean isBot(Entity en) {
      if (!Utils.Player.isPlayerInGame() || mc.currentScreen != null) return false;

      if (Freecam.en != null && Freecam.en == en) {
         return true;
      } else {
         Module antiBot = Client.moduleManager.getModuleByClazz(AntiBot.class);
         if (antiBot != null && !antiBot.isEnabled()) {
            return false;
         } else if (checkDelay.isToggled() && !newEnt.isEmpty() && newEnt.containsKey(en)) {
            return true;
         } else {

            if (invisChecks.isToggled())
               if (en.isInvisible() || en.isInvisibleToPlayer(mc.thePlayer))
                  return true;

            if (nameChecks.isToggled())
               if (AntiBot.isBotName(en))
                  return true;

            if (playerChecks.isToggled())
               if (!(en instanceof EntityPlayer))
                  return true;

            if (invChecks.isToggled())
               if (en.getInventory() == null)
                  return true;

            if (pushChecks.isToggled())
               if (!en.canBePushed())
                  return true;

            if (rideChecks.isToggled())
               if (en.isRiding())
                  return true;

            if (playerInTabChecks.isToggled())
               if (!ravenNPlus.client.utils.CombatUtils.inTab((EntityLivingBase) en))
                  return true;

            if (armorStandCheck.isToggled() && en instanceof EntityArmorStand)
               return true;

            if (hypCheck.isToggled()) {
               String formattedName = en.getDisplayName().getFormattedText();

               if (!formattedName.startsWith("\247") && formattedName.endsWith("\247r"))
                  return true;

               if (formattedName.contains("[NPC]"))
                  return true;

               return false;
            }

            String n = en.getDisplayName().getUnformattedText();
            if (n.length() == 10) {
               int num = 0;
               int let = 0;
               char[] var4 = n.toCharArray();
               for (char c : var4) {
                  if (Character.isLetter(c)) {
                     if (Character.isUpperCase(c)) {
                        return false;
                     }
                     ++let;
                  } else {
                     if (!Character.isDigit(c)) {
                        return false;
                     }
                     ++num;
                  }
               }
               return num >= 2 && let >= 2;
            }
         }
         return false;
      }
   }

   public static boolean isBotName(Entity en) {
      String n = en.getDisplayName().getUnformattedText();

      // name checks ---------------------
      if (n.startsWith("CIT-")) return true;
      if (n.startsWith("NPC-")) return true;
      if (n.startsWith("NCP-")) return true;
      if (n.startsWith("cit-")) return true;
      if (n.startsWith("npc-")) return true;
      if (n.startsWith("ncp-")) return true;
      //--------------------------------------
      if (n.contains("CIT")) return true;
      if (n.contains("Ncp")) return true;
      if (n.contains("NPC")) return true;
      if (n.contains("NCP")) return true;
      if (n.contains("cit")) return true;
      if (n.contains("npc")) return true;
      if (n.contains("ncp")) return true;
      if (n.contains("Empty")) return true;
      if (n.contains("empty")) return true;
      if (n.contains("Bot")) return true;
      if (n.contains("bot")) return true;
      //--------------------------------------
      if (n.contains("|")) return true;
      if (n.contains("<")) return true;
      if (n.contains(">")) return true;
      if (n.contains("#")) return true;
      if (n.contains("+")) return true;
      if (n.contains("&")) return true;
      if (n.contains("/")) return true;
      if (n.contains("(")) return true;
      if (n.contains(")")) return true;
      if (n.contains("}")) return true;
      if (n.contains("@")) return true;
      if (n.contains("%")) return true;
      if (n.contains(";")) return true;
      if (n.contains("^")) return true;
      if (n.contains("°")) return true;
      if (n.contains(" ")) return true;
      if (n.contains(":")) return true;
      if (n.contains("{")) return true;
      if (n.contains("ä")) return true;
      if (n.contains("ü")) return true;
      if (n.contains("ö")) return true;
      if (n.contains("é")) return true;
      if (n.contains("²")) return true;
      if (n.contains("³")) return true;
      if (n.contains("´")) return true;
      if (n.contains("*")) return true;
      if (n.contains(".")) return true;
      if (n.contains("~")) return true;
      if (n.contains("|")) return true;
      if (n.contains("$")) return true;
      if (n.contains("[")) return true;
      if (n.contains("]")) return true;
      if (n.contains("-")) return true;
      if (n.contains("!")) return true;
      if (n.contains("?")) return true;
      if (n.contains("=")) return true;
      if (n.contains("§")) return true;
      //--------------------------------------
      if (n.length() < 3) return true;

      else
         return false;
   }

}
 */