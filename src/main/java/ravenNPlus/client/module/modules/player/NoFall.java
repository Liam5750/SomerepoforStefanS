package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import ravenNPlus.client.module.setting.impl.ModeSetting;

public class NoFall extends Module {

   public static ModeSetting mode;

   public NoFall() {
      super("NoFall", ModuleCategory.player, "Take no fall damage");
      this.addSetting(mode = new ModeSetting("Fall Mode", NoFall.modes.Packet));
   }

   public void update() {
      if ((double) mc.thePlayer.fallDistance > 2.5D) {

         if (mode.getMode() == NoFall.modes.Packet)
            this.sendPacketPlayer(new C03PacketPlayer(true));

         //Soon more...

      }
   }

   public enum modes {
      Packet //Soon more...
   }

}