package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.notifications.Type;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UserHud extends Module {

    public static TickSetting fps, clock, ping, ip, coordinates, dontShowCoordsOnline;
    public static SliderSetting hudX, hudY;

    public UserHud() {
        super("UserHUD", ModuleCategory.render, "Draws a hud you can setup");
        this.addSetting(fps = new TickSetting("FPS", true));
        this.addSetting(clock = new TickSetting("Clock", true));
        this.addSetting(ping = new TickSetting("Ping", true));
        this.addSetting(ip = new TickSetting("IP", true));
        this.addSetting(coordinates = new TickSetting("Coordinates", true));
        this.addSetting(dontShowCoordsOnline = new TickSetting("Dont show Coordinates Online", true));
        this.addSetting(hudX = new SliderSetting("X", 5, -50, mc.displayWidth + 150, 1));
        this.addSetting(hudY = new SliderSetting("Y", 50, -50, mc.displayHeight + 150, 1));
    }

    @SubscribeEvent
    public void p(TickEvent.RenderTickEvent ev) {
        if (!this.inGame()) return;

        FontRenderer fr = mc.fontRendererObj;
        int y = 5 + hudX.getValueToInt(), x = 20 + hudY.getValueToInt(), color = 0xFF0079;

        if (fps.isToggled()) {
            fr.drawStringWithShadow("FPS  : " + Minecraft.getDebugFPS(), x, y, color);
            y += fr.FONT_HEIGHT;
        }

        if (clock.isToggled()) {
            mc.fontRendererObj.drawStringWithShadow(java.util.Calendar.getInstance().getTime().getHours()
                    + ":" + java.util.Calendar.getInstance().getTime().getMinutes() + ":" + java.util.Calendar.getInstance().getTime().getSeconds(), x, y, 0xFF0079);
            y += fr.FONT_HEIGHT;
        }

        if (coordinates.isToggled()) {
            if (dontShowCoordsOnline.isToggled() && !mc.isSingleplayer()) return;
            if (Utils.Client.isServerIP("2b2t.org")) {
                this.notification(Type.WARNING, "Disabled cause ur in 2b2t", 5);
                return;
            }

            if (Utils.Client.isServerIP("0b0t.org")) {
                this.notification(Type.WARNING, "Disabled cause ur in 0b0t", 10);
                return;
            }

            if (Utils.Client.isServerIP("9b9t.org")) {
                this.notification(Type.WARNING, "Disabled cause ur in 9b9t", 5);
                return;
            }

            fr.drawStringWithShadow("X:" + (int) this.player().posX + " Y:" + (int) this.player().posY + " Z:" + (int) this.player().posZ, x, y, color);
            y += fr.FONT_HEIGHT;
        }

        if (ping.isToggled()) {
            if (mc.isSingleplayer()) return;
            fr.drawStringWithShadow("Ping  : " + mc.getCurrentServerData().pingToServer, x, y, color);
            y += fr.FONT_HEIGHT;
        }

        if (ip.isToggled()) {
            if (mc.isSingleplayer()) return;
            fr.drawStringWithShadow("IP  : " + mc.getCurrentServerData().serverIP, x, y, color);
            y += fr.FONT_HEIGHT;
        }
    }

}