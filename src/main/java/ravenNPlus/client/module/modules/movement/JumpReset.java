package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JumpReset extends Module {

    public static TickSetting fakeJump;
    public static ModeSetting mode;
    static boolean canFly = true;

    public JumpReset() {
        super("JumpReset", ModuleCategory.movement, "Velocity Lite");
        this.addSetting(mode = new ModeSetting("Reset Mode", JumpReset.modes.Jump));
        this.addSetting(fakeJump = new TickSetting("Fake Jump", o));
    }

    @Override
    public void onDisable() {
        canFly = true;
    }

    @Override
    public void onEnable() {
        if (JumpReset.mode.getMode() != JumpReset.modes.Fly)
            JumpReset.canFly = true;
    }

    @SubscribeEvent
    public void r(TickEvent.PlayerTickEvent e) {
        if (!this.inGame()) return;

        if (this.isAlive() && (!(this.onLadder() || Utils.Player.isInLiquid() || this.player().hurtTime == 0 || this.player().velocityChanged))) {

            if (JumpReset.mode.getMode() == JumpReset.modes.OnGround) {
                if (!this.onGround())
                    this.player().onGround = true;
            }

            if (JumpReset.mode.getMode() == JumpReset.modes.Jump) {
                if (this.onGround()) {
                    if (JumpReset.fakeJump.isToggled())
                        Utils.Player.fakeJump(true);

                    if (!JumpReset.fakeJump.isToggled())
                        mc.thePlayer.jump();
                }
            }

            if (JumpReset.mode.getMode() == JumpReset.modes.Fly) {
                if (this.onGround() && JumpReset.canFly) {
                    this.player().capabilities.isFlying = true;
                    JumpReset.canFly = false;
                } else if (!this.player().onGround && this.player().capabilities.isFlying && !JumpReset.canFly) {
                    this.player().capabilities.isFlying = false;
                    JumpReset.canFly = true;
                }
            }

            if (JumpReset.mode.getMode() == JumpReset.modes.MoveJump)
                this.player().movementInput.jump = false;

            if (JumpReset.mode.getMode() == JumpReset.modes.MoveStat)
                this.player().addMovementStat(this.player().posX - 0.6, this.player().posY - 0.2, this.player().posZ - 0.6);

            if (JumpReset.mode.getMode() == JumpReset.modes.Motion)
                this.player().addVelocity(this.player().motionX / 8, this.player().motionY / 8, this.player().motionZ / 8);

        }

    }

    public static enum modes {
        Jump, OnGround, Fly,
        MoveJump, MoveStat, Motion
    }

}