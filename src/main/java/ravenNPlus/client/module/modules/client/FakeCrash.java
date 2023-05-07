package ravenNPlus.client.module.modules.client;

import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.notifications.Type;
import ravenNPlus.client.module.setting.impl.SliderSetting;

public class FakeCrash extends Module {

    static SliderSetting status, delay;

    public FakeCrash() {
        super("FakeCrash", ModuleCategory.client, "Fake a crash");
        this.addSetting(status = new SliderSetting("Status", 1D, 0D, 10D, 1D));
        this.addSetting(delay = new SliderSetting("Delay", 5D, 0D, 50D, 1D));
    }

    @Override
    public void onEnable() {
        this.notification(Type.WARNING, "Minecraft shutdowns soon", 8);

        Client.configManager.save();

        if (Timer.hasTimeElapsed(delay.getValueToLong() * 1000L, true))
            System.exit(status.getValueToInt());
    }

}