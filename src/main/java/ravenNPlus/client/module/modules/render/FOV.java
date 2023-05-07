package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.client.settings.GameSettings;

public class FOV extends Module {

    public static SliderSetting fov;
    private float oldFov = 0F;

    public FOV() {
        super("Fov", ModuleCategory.render, "Modifies your Fov");
        this.addSetting(fov = new SliderSetting("FOV", 140D, 0D, 500D, 10D));
    }

    @Override
    public void onEnable() {
        if (!this.inGame()) return;

        oldFov = mc.gameSettings.getOptionFloatValue(GameSettings.Options.FOV);
        mc.gameSettings.fovSetting = fov.getValueToFloat();
    }

    @Override
    public void onDisable() {
        if (!this.inGame()) return;

        mc.gameSettings.fovSetting = oldFov;
    }

}