package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.ColorUtil;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemESP extends Module {

    public static SliderSetting range, mode;
    public static TickSetting ran;

    public ItemESP() {
        super("ItemESP", ModuleCategory.render, "Draw a outline around Items");
        this.addSetting(mode = new SliderSetting("Mode", 1, 1, 6, 1));
        this.addSetting(ran = new TickSetting("Range", true));
        this.addSetting(range = new SliderSetting("Range", 25, 5, 200, 1));
    }

    @SubscribeEvent
    public void s(RenderWorldLastEvent ev) {
        if (!this.inGame() || Utils.Player.isPlayerInContainer()) return;

        for (Entity item : mc.theWorld.loadedEntityList)
            if (item instanceof EntityItem) {
                if (ran.isToggled() && this.player().getDistanceToEntity(item) < range.getValueToInt())
                    return;

                Utils.HUD.drawBoxAroundItem((EntityItem) item, mode.getValueToInt(), 0, ColorUtil.color_esp_green);
            }

    }

}