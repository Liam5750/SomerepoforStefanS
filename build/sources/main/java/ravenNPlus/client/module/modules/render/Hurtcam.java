package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Hurtcam extends Module {

    public static SliderSetting hurtTime, yaw;
    public static ModeSetting mode;

    public Hurtcam() {
        super("HurtCam", ModuleCategory.render, "");
        this.addSetting(mode = new ModeSetting("Mode", Hurtcam.modes.NoHurtcam));
        this.addSetting(hurtTime = new SliderSetting("HurtTime", 1D, -10D, 10D, 1D));
        this.addSetting(yaw = new SliderSetting("HurtYaw", 1D, -180D, 180D, 1D));
    }

    @SubscribeEvent
    public void r(TickEvent.PlayerTickEvent e) {
        if (this.inGame()) {
            if (this.player().hurtTime > hurtTime.getValue()) {

                if (mode.getMode() == Hurtcam.modes.PerformHurt) {
                    this.player().performHurtAnimation();
                }

                /*
                if(mode.getMode() == null) {
                    this.player().hurtTime = this.player().maxHurtTime = 0;
                    this.player().attackedAtYaw = 0.0F;
                }
                 */

                if (mode.getMode() == Hurtcam.modes.MaxHurtTime) {
                    this.player().hurtTime = this.player().maxHurtTime = hurtTime.getValueToInt();
                    this.player().attackedAtYaw = yaw.getValueToFloat();
                }

                if (mode.getMode() == Hurtcam.modes.AttackedYaw) {
                    this.player().hurtTime = hurtTime.getValueToInt();
                    this.player().attackedAtYaw = yaw.getValueToFloat();
                }

                if (mode.getMode() == Hurtcam.modes.HurtTime2) {
                    this.player().hurtTime = hurtTime.getValueToInt();
                }

                if (mode.getMode() == Hurtcam.modes.AttackedYaw2) {
                    this.player().attackedAtYaw = yaw.getValueToInt();
                }

                if (mode.getMode() == Hurtcam.modes.NoHurtcam) {
                    LivingHurtEvent ex = new LivingHurtEvent(this.player(), null, 0);

                    ex.setCanceled(true);
                }
            }
        }
    }

    public enum modes {
        PerformHurt, MaxHurtTime, AttackedYaw,
        HurtTime2, AttackedYaw2, NoHurtcam
    }

}