package ravenNPlus.client.utils.notifications;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.modules.client.GuiClick;
import ravenNPlus.client.utils.RenderUtils;
import ravenNPlus.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Render {

    public static final Render notificationRenderer = new Render();

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        if(GuiClick.notifications.isToggled()) Manager.render();
    }

    public static void change(Module m) {
        if(!GuiClick.notifications.isToggled() || Minecraft.getMinecraft().currentScreen != null || !Utils.Player.isPlayerInGame()) return;

        String s = m.isEnabled() ? "enabled" : "disabled";
        RenderUtils.notification(Type.OTHER, m.getName(), s, 2);
    }

}