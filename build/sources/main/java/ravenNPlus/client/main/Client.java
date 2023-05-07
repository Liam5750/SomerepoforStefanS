package ravenNPlus.client.main;

import ravenNPlus.client.utils.*;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.config.ConfigManager;
import ravenNPlus.client.utils.notifications.Type;
import ravenNPlus.client.module.ModuleManager;
import ravenNPlus.client.utils.notifications.Render;
import ravenNPlus.client.utils.version.VersionManager;
import ravenNPlus.client.clickgui.RavenNPlus.ClickGui;
import ravenNPlus.client.command.CommandManager;
import ravenNPlus.client.module.modules.other.NameHider;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Client {

   public static boolean debugger = false;
   public static final VersionManager versionManager = new VersionManager();
   public static CommandManager commandManager;
   public static final String sourceLocation = "https://discord.gg/KzBu6USXAc";
   public static final String downloadLocation = "https://discord.gg/KzBu6USXAc";
   public static final String discord = "https://discord.gg/KzBu6USXAc";
   public static String name = "Raven N+", author = "SleepyFish";
   public static String[] updateText = {"Your version of " + Client.name + " ( " + versionManager.getClientVersion().toString() + " ) is outdated!"};
   public static ConfigManager configManager;
   public static ClientConfig clientConfig;
   public static final ModuleManager moduleManager = new ModuleManager();
   public static ClickGui clickGui;
   private static final java.util.concurrent.ScheduledExecutorService ex = java.util.concurrent.Executors.newScheduledThreadPool(2);
   public static DiscordRichPresence pres = new DiscordRichPresence();
   public static Client instance;
   //static String status = "Menu";

   public static void init() {

      MinecraftForge.EVENT_BUS.register(new Client());
      MinecraftForge.EVENT_BUS.register(new DebugInfoRenderer());
      MinecraftForge.EVENT_BUS.register(new MouseManager());
      MinecraftForge.EVENT_BUS.register(new ChatHelper());
      MinecraftForge.EVENT_BUS.register(Render.notificationRenderer);
      Runtime.getRuntime().addShutdownHook(new Thread(ex::shutdown));

      commandManager = new CommandManager();
      clickGui = new ClickGui();
      configManager = new ConfigManager();
      clientConfig = new ClientConfig();
      clientConfig.applyConfig();

      RenderUtils.drawStringStringInteger = 0;
      RenderUtils.drawStringHUDInteger = 0;

      String applicationId = "1010880713551269988";
      String steamId = "" + Client.discord;
      DiscordEventHandlers handlers = new DiscordEventHandlers();
      updateRPC(1);
      handlers.ready = (user) -> System.out.println("Ready!");
      DiscordRPC.discordInitialize(applicationId, handlers, true, steamId);
      pres.state = "Playing Minecraft 1.8.9";
      pres.smallImageText = "" + Client.discord;
      updateRPC(0);
      pres.startTimestamp = System.currentTimeMillis() / 1000;
      new Thread(() -> {
         while (!Thread.currentThread().isInterrupted()) {
            DiscordRPC.discordRunCallbacks();
            try {
               Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
         }
      }, "RPC-Callback-Handler").start();


      RenderUtils.notification(Type.OTHER, name, "Initialized: " + ModuleManager.initialized, 5);
   }

   /*
   if(GuiClick.monkeee.isToggled()) {
      pres.largeImageKey = "monkeeee_-_kopie";
      updateRPC(0);
      } else {
         pres.largeImageKey = "npuls";  //if you want the old one, the name is: "icon"
         updateRPC(0);
      }

   pres.largeImageText = RavenNPlus.discord;
   */

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         if (Utils.Player.isPlayerInGame()) {
            for (int i = 0; i < moduleManager.numberOfModules(); i++) {
               Module module = moduleManager.getModules().get(i);
               if (Minecraft.getMinecraft().currentScreen == null) {
                  module.keybind();
               } else if (Minecraft.getMinecraft().currentScreen instanceof ClickGui) {
                  module.guiUpdate();
               }

               if (module.isEnabled()) module.update();
            }
         }
      }
   }

/*
if(mc.isSingleplayer()) {
status = "Singleplayer";
updateRPC(1);
} else if(!mc.isSingleplayer()) {
if(mc.getCurrentServerData() == null) {
status = "Menu";
updateRPC(1);
} else {
status = mc.getCurrentServerData().serverIP.toLowerCase();
updateRPC(1);
}
}
}

if(GuiClick.monkeee.isToggled()) {
pres.largeImageKey = "monkeeee_-_kopie";
DiscordRPC.discordUpdatePresence(pres);
} else {
pres.largeImageKey = "npuls";  //if you want the old one, the name is: "icon"
DiscordRPC.discordUpdatePresence(pres);
}
}
*/

   static void updateRPC(int type) {
      if (type == 1) {
         //  pres.state = "Status: " + status;
         DiscordRPC.discordUpdatePresence(pres);
      }
      if (type == 0) {
         DiscordRPC.discordUpdatePresence(pres);
      }
   }

   @SubscribeEvent
   public void onChatMessageReceived(ClientChatReceivedEvent event) {
      if (Utils.Player.isPlayerInGame()) {
         String msg = event.message.getUnformattedText();

         if (msg.startsWith("Your new API key is")) {
            Utils.URLS.hypixelApiKey = msg.replace("Your new API key is ", "");
            Utils.Player.sendMessageToSelf("&aSet api key to " + Utils.URLS.hypixelApiKey + "!");
            clientConfig.saveConfig();
         }

         /*
         if (NameHider.isEnabled) {
            if (msg.contains(Minecraft.getMinecraft().getSession().getUsername())) {
               String x = msg.replace(Minecraft.getMinecraft().getSession().getUsername(), NameHider.n);
               Utils.Player.sendMessageToSelf(x + msg);
            }
         }
          */
      }
   }

   public static java.util.concurrent.ScheduledExecutorService getExecutor() {
      return ex;
   }

}