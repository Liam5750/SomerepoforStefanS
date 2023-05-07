package ravenNPlus.client.module.modules.client;

import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.ChatHelper;
import ravenNPlus.client.utils.MouseManager;
import ravenNPlus.client.utils.DebugInfoRenderer;
import ravenNPlus.client.utils.notifications.Render;
import ravenNPlus.keystrokemod.KeyStrokeRenderer;
import net.minecraftforge.common.MinecraftForge;

public class SelfDestruct extends Module {

   public SelfDestruct() {
      super("Self Destruct", ModuleCategory.client, "Disable all Modules");
   }

   public void onEnable() {
      this.disable();

      mc.displayGuiScreen(null);

      for (Module module : Client.moduleManager.getModules()) {
         if (module != this && module.isEnabled()) {
            module.disable();
         }
      }

      MinecraftForge.EVENT_BUS.unregister(new Client());
      MinecraftForge.EVENT_BUS.unregister(new Render());
      MinecraftForge.EVENT_BUS.unregister(new DebugInfoRenderer());
      MinecraftForge.EVENT_BUS.unregister(new MouseManager());
      MinecraftForge.EVENT_BUS.unregister(new KeyStrokeRenderer());
      MinecraftForge.EVENT_BUS.unregister(new ChatHelper());
   }

}