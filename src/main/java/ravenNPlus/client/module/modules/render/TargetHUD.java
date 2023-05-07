package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.RenderUtils;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TargetHUD extends Module {

    public static TickSetting background, head, sortBots;
    public static SliderSetting range, x, y;
    public static boolean isHudShown = false;

    public TargetHUD() {
        super("TargetHUD", ModuleCategory.render, "A Hud to see your enemy's health and name");
        this.addSetting(range = new SliderSetting("Distance", 5, 1, 50, 1));
        this.addSetting(head = new TickSetting("Head", false));
        this.addSetting(background = new TickSetting("Background", false));
        this.addSetting(x = new SliderSetting("X", 515, 5, mc.displayWidth - 5, 1));
        this.addSetting(y = new SliderSetting("Y", 285, 5, mc.displayHeight + 15, 1));
        this.addSetting(sortBots = new TickSetting("Sort Bots", true));
    }

    @Override
    public void onDisable() {
        RenderUtils.drawStringHUD_Disable(range.getValueToInt(), sortBots.isToggled(), x.getValueToInt()+2);
        isHudShown = false;
    }

    @SubscribeEvent
    public void r(TickEvent.RenderTickEvent ev) {
        if (!this.inGame()) return;
        drawHud();
    }

    public static void drawHud() {
        try {
            RenderUtils.drawStringHUD(x.getValueToInt(), y.getValueToInt(), range.getValueToInt(), background.isToggled(), true, head.isToggled(), sortBots.isToggled());
        } catch (Exception e) {
            e.printStackTrace();
            head.disable();
        }
        isHudShown = true;
    }

}