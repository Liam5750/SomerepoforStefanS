package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastLadder extends Module {

    public static SliderSetting speed, delay;
    public static TickSetting delayToggle, auto;

    public FastLadder() {
        super("FastLadder", ModuleCategory.movement, "Makes you faster on Ladders");
        this.addSetting(speed = new SliderSetting("Speed", 0, 0, 1, 0.1));
        this.addSetting(auto = new TickSetting("Auto Ladder", true));
        this.addSetting(delayToggle = new TickSetting("Delay", false));
        this.addSetting(delay = new SliderSetting("Delay Time", 0, 0, 50, 1));
    }

    @SubscribeEvent
    public void r(TickEvent.PlayerTickEvent e) {
        if (!delayToggle.isToggled()) {
            if (auto.isToggled()) {
                void_pl23F9(e);
            } else {
                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    void_pl23F9(e);
                }
            }
        } else if (delayToggle.isToggled() && Timer.hasTimeElapsed(delay.getValueToLong() * 100L, true)) {
            if (auto.isToggled()) {
                void_pl23F9(e);
            } else {
                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    void_pl23F9(e);
                }
            }
        }
    }

    private void void_pl23F9(TickEvent.PlayerTickEvent e) {
        if (!this.inGame()) return;
        if (!this.player().isOnLadder()) return;

        if (e.phase != TickEvent.Phase.END) {
            if (!this.player().isRiding() && !this.player().isDead && this.player().isOnLadder()) {
                this.player().motionY = 0.169 + speed.getValue();
            }
        }

    }

}