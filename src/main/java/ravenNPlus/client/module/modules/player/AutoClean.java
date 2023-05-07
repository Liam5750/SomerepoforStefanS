package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.utils.DelayTimer;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ravenNPlus.client.utils.Timer;

public class AutoClean extends ravenNPlus.client.module.Module {

    public static TickSetting onlyInv, keepCorrupted;
    public static SliderSetting delay, secoundDelay;
    private final DelayTimer timer = new DelayTimer();

    public AutoClean() {
        super("AutoClean", ModuleCategory.player, "Cleans your Inventory Automatically");
        this.addSetting(keepCorrupted = new TickSetting("Keep Corrupted Pearl", o));
        this.addSetting(secoundDelay = new SliderSetting("Second Delay", 20, 0, 35, 1));
        this.addSetting(delay = new SliderSetting("Delay", 13, 0, 35, 1));
        this.addSetting(onlyInv = new TickSetting("Only Inv", x));
    }

    @SubscribeEvent
    public void r(TickEvent.PlayerTickEvent e) {
        if (!this.inGame()) return;

        if (!this.timer.hasPassed(200L))
            return;

        ravenNPlus.client.utils.InvUtils.cleanInventory(delay.getValueToLong() * 6, secoundDelay.getValueToLong() * 6, onlyInv.isToggled(), keepCorrupted.isToggled());

        if (Timer.hasTimeElapsed(delay.getValueToLong()*7))
            ravenNPlus.client.utils.InvUtils.cleanInventoryTimerReset();

        this.timer.reset();
    }

}