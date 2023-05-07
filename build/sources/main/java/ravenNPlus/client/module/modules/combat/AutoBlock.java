package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoBlock extends Module {

    public static DoubleSliderSetting duration, distance;
    public static SliderSetting chance;
    private boolean engaged;
    private final CoolDown engagedTime = new CoolDown(0);

    public AutoBlock() {
        super("AutoBlock", ModuleCategory.combat, "Automatically blocks your sword after a hit");
        this.addSetting(duration = new DoubleSliderSetting("Block duration (MS)", 20, 100, 1, 500, 1));
        this.addSetting(distance = new DoubleSliderSetting("Distance to player (blocks)", 0, 3, 0, 6, 0.01));
        this.addSetting(chance = new SliderSetting("Chance %", 100, 0, 100, 1));
    }

    @SubscribeEvent
    public void yes(TickEvent.RenderTickEvent e) {
        if (!this.inGame())
            return;

        if (engaged) {
            if ((engagedTime.hasFinished() || !Mouse.isButtonDown(0)) && duration.getInputMin() <= engagedTime.getElapsedTime()) {
                engaged = false;
                release();
            }
            return;
        }

        if (Mouse.isButtonDown(0) && mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && this.player().getDistanceToEntity(mc.objectMouseOver.entityHit) >= distance.getInputMin() && mc.objectMouseOver.entityHit != null && this.player().getDistanceToEntity(mc.objectMouseOver.entityHit) <= distance.getInputMax() && (chance.getValue() == 100 || Math.random() <= chance.getValue() / 100)) {
            engaged = true;
            engagedTime.setCooldown((long) duration.getInputMax());
            engagedTime.start();
            press();
        }
    }

    private static void release() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, false);
        Utils.Client.setMouseButtonState(1, false);
    }

    private static void press() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, true);
        KeyBinding.onTick(key);
        Utils.Client.setMouseButtonState(1, true);
    }

}