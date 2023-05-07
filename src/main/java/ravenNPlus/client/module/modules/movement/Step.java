package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Step extends Module {

    public static SliderSetting height;
    public static ModeSetting mode;

    public Step() {
        super("Step", ModuleCategory.movement, "Modifies your step height");
        this.addSetting(mode = new ModeSetting("Step Modes", Step.modes.StepHeight));
        this.addSetting(height = new SliderSetting("Height", 3, 1, 50, 1));
    }

    @SubscribeEvent
    public void s(TickEvent.PlayerTickEvent e) {
        if (this.inGame() && mc.gameSettings.keyBindForward.isKeyDown() && this.player().isCollidedHorizontally && this.isAlive()) {

            if (Step.mode.getMode() == Step.modes.StepHeight) {
                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) && this.player().fallDistance < 0.1f && this.onGround()) {
                    this.player().stepHeight = height.getValueToFloat();
                } else {
                    this.player().stepHeight = 0.5F;
                }
            }

            if (Step.mode.getMode() == Step.modes.OldSpider) {
                this.player().motionY = 0.3f;
            }

            if (Step.mode.getMode() == Step.modes.Root) {
                if (this.player().ticksExisted % 4 == 0)
                    this.player().motionY = 0.25D;
            }

        }

    }

    @Override
    public void onDisable() {
        this.player().stepHeight = 0.5F;
    }

    public enum modes {
        StepHeight, OldSpider, Root
    }

}