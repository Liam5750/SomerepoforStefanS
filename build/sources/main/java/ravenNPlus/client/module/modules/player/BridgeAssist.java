package ravenNPlus.client.module.modules.player;


import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BridgeAssist extends Module {

    private final TickSetting onSneak, setLook, workWithSafeWalk;
    private final SliderSetting waitFor, assistRange;
    private final ModeSetting assistMode;
    private boolean waitingForAim;
    private boolean gliding;
    private long startWaitTime;
    private final float[] godbridgePos = {75.6f, -315, -225, -135, -45, 0, 45, 135, 225, 315};
    private final float[] moonwalkPos = {79.6f, -340, -290, -250, -200, -160, -110, -70, -20, 0, 20, 70, 110, 160, 200, 250, 290, 340};
    private final float[] breezilyPos = {79.9f, -360, -270, -180, -90, 0, 90, 180, 270, 360};
    private final float[] normalPos = {78f, -315, -225, -135, -45, 0, 45, 135, 225, 315};
    private double speedYaw, speedPitch;
    private float waitingForYaw, waitingForPitch;

    public BridgeAssist() {
        super("Bridge Assist", ModuleCategory.player, "Assists you with bridging (Best with fastplace, not autoplace)");
        this.addSetting(waitFor = new SliderSetting("Wait time (ms)", 500, 0, 5000, 25));
        this.addSetting(setLook = new TickSetting("Set look pos", true));
        this.addSetting(onSneak = new TickSetting("Work only when sneaking", true));
        this.addSetting(workWithSafeWalk = new TickSetting("Work with safewalk", false));
        this.addSetting(assistRange = new SliderSetting("Assist range", 10.0D, 1.0D, 40.0D, 1.0D));
        this.addSetting(assistMode = new ModeSetting("Bride Mode", BridgeAssist.modes.GodBridge));
    }

    @Override
    public void onEnable() {
        this.waitingForAim = false;
        this.gliding = false;
        super.onEnable();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (!this.inGame()) return;

        Module safeWalk = ravenNPlus.client.main.Client.moduleManager.getModuleByClazz(SafeWalk.class);
        if (safeWalk != null && safeWalk.isEnabled()) {
            if (!workWithSafeWalk.isToggled())
                return;
        }

        if (!(ravenNPlus.client.utils.Utils.Player.playerOverAir(1) && this.onGround()))
            return;

        if (onSneak.isToggled())
            if (!this.player().isSneaking())
                return;

        if (gliding) {
            float fuckedYaw = this.player().rotationYaw;
            float fuckedPitch = this.player().rotationPitch;
            float yaw = fuckedYaw - ((int) fuckedYaw / 360) * 360;
            float pitch = fuckedPitch - ((int) fuckedPitch / 360) * 360;

            double ilv1 = yaw - speedYaw, ilv2 = yaw + speedYaw, ilv3 = pitch - speedPitch, ilv4 = pitch + speedPitch;

            if (ilv1 < 0) ilv1 *= -1;
            if (ilv2 < 0) ilv2 *= -1;
            if (ilv3 < 0) ilv3 *= -1;
            if (ilv4 < 0) ilv4 *= -1;

            if (this.speedYaw > ilv1 || this.speedYaw > ilv2) this.player().rotationYaw = this.waitingForYaw;
            if (this.speedPitch > ilv3 || this.speedPitch > ilv4) this.player().rotationPitch = this.waitingForPitch;
            if (this.player().rotationYaw < this.waitingForYaw) this.player().rotationYaw += this.speedYaw;
            if (this.player().rotationYaw > this.waitingForYaw) this.player().rotationYaw -= this.speedYaw;
            if (this.player().rotationPitch > this.waitingForPitch) this.player().rotationPitch -= this.speedPitch;

            if (this.player().rotationYaw == this.waitingForYaw && this.player().rotationPitch == this.waitingForPitch) {
                gliding = false;
                this.waitingForAim = false;
            }
            return;
        }

        if (!waitingForAim) {
            waitingForAim = true;
            startWaitTime = System.currentTimeMillis();
            return;
        }

        if (System.currentTimeMillis() - startWaitTime < waitFor.getValue())
            return;

        float fuckedYaw = this.player().rotationYaw;
        float fuckedPitch = this.player().rotationPitch;
        float yaw = fuckedYaw - ((int) fuckedYaw / 360) * 360;
        float pitch = fuckedPitch - ((int) fuckedPitch / 360) * 360;
        float range = (float) assistRange.getValue();

        if (assistMode.getMode() == BridgeAssist.modes.GodBridge) {
            if (godbridgePos[0] >= (pitch - range) && godbridgePos[0] <= (pitch + range)) {
                for (int k = 1; k < godbridgePos.length; k++) {
                    if (godbridgePos[k] >= (yaw - range) && godbridgePos[k] <= (yaw + range)) {
                        aimAt(godbridgePos[0], godbridgePos[k], fuckedYaw, fuckedPitch);
                        this.waitingForAim = false;
                        return;
                    }
                }
            }
        } else if (assistMode.getMode() == BridgeAssist.modes.MoonWalk) {
            if (moonwalkPos[0] >= (pitch - range) && moonwalkPos[0] <= (pitch + range)) {
                for (int k = 1; k < moonwalkPos.length; k++) {
                    if (moonwalkPos[k] >= (yaw - range) && moonwalkPos[k] <= (yaw + range)) {
                        aimAt(moonwalkPos[0], moonwalkPos[k], fuckedYaw, fuckedPitch);
                        this.waitingForAim = false;
                        return;
                    }
                }
            }
        } else if (assistMode.getMode() == BridgeAssist.modes.Breezily) {
            if (breezilyPos[0] >= (pitch - range) && breezilyPos[0] <= (pitch + range)) {
                for (int k = 1; k < breezilyPos.length; k++) {
                    if (breezilyPos[k] >= (yaw - range) && breezilyPos[k] <= (yaw + range)) {
                        aimAt(breezilyPos[0], breezilyPos[k], fuckedYaw, fuckedPitch);
                        this.waitingForAim = false;
                        return;
                    }
                }
            }
        } else if (assistMode.getMode() == BridgeAssist.modes.Normal) {
            if (normalPos[0] >= (pitch - range) && normalPos[0] <= (pitch + range)) {
                for (int k = 1; k < normalPos.length; k++) {
                    if (normalPos[k] >= (yaw - range) && normalPos[k] <= (yaw + range)) {
                        aimAt(normalPos[0], normalPos[k], fuckedYaw, fuckedPitch);
                        this.waitingForAim = false;
                        return;
                    }
                }
            }
        }

        this.waitingForAim = false;
    }

    public enum modes {
        GodBridge, MoonWalk,
        Breezily, Normal
    }

    public void aimAt(float pitch, float yaw, float fuckedYaw, float fuckedPitch) {
        if (setLook.isToggled()) {
            this.player().rotationPitch = pitch + ((int) fuckedPitch / 360) * 360;
            this.player().rotationYaw = yaw;
        }
    }

}