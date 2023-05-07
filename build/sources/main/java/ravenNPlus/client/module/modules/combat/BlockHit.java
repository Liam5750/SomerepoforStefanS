package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.module.*;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.concurrent.ThreadLocalRandom;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class BlockHit extends Module {

    public static SliderSetting range, eventType, chance;
    public static DescriptionSetting eventTypeDesc;
    public static TickSetting onlyPlayers, onRightMBHold;
    public static DoubleSliderSetting waitMs, hitPer, postDelay;
    public static boolean executingAction, hitCoolDown, alreadyHit, safeGuard;
    public static int hitTimeout, hitsWaited;
    private CoolDown actionTimer = new CoolDown(0), postDelayTimer = new CoolDown(0);
    private boolean waitingForPostDelay;

    public BlockHit() {
        super("BlockHit", ModuleCategory.combat, "");
        this.addSetting(onlyPlayers = new TickSetting("Only combo players", true));
        this.addSetting(onRightMBHold = new TickSetting("When holding down rmb", true));
        this.addSetting(waitMs = new DoubleSliderSetting("Action Time (MS)", 110, 150, 1, 500, 1));
        this.addSetting(hitPer = new DoubleSliderSetting("Once every ... hits", 1, 1, 1, 10, 1));
        this.addSetting(postDelay = new DoubleSliderSetting("Post Delay (MS)", 10, 40, 0, 500, 1));
        this.addSetting(chance = new SliderSetting("Chance %", 100, 0, 100, 1));
        this.addSetting(range = new SliderSetting("Range: ", 3, 1, 6, 0.05));
        this.addSetting(eventType = new SliderSetting("Value: ", 2, 1, 2, 1));
        this.addSetting(eventTypeDesc = new DescriptionSetting("Mode: POST"));
    }

    public void guiUpdate() {
        eventTypeDesc.setDesc(Utils.md + Utils.Modes.SprintResetTimings.values()[(int) eventType.getValue() - 1]);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (!this.inGame())
            return;

        if (onRightMBHold.isToggled() && !Utils.Player.tryingToCombo()) {
            if (!safeGuard || InvUtils.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                safeGuard = true;
                finishCombo();
            }
            return;
        }
        if (waitingForPostDelay) {
            if (postDelayTimer.hasFinished()) {
                executingAction = true;
                startCombo();
                waitingForPostDelay = false;
                if (safeGuard) safeGuard = false;
                actionTimer.start();
            }
            return;
        }

        if (executingAction) {
            if (actionTimer.hasFinished()) {
                executingAction = false;
                finishCombo();
                return;
            } else {
                return;
            }
        }

        if (onRightMBHold.isToggled() && Utils.Player.tryingToCombo()) {
            if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null) {
                if (!safeGuard || InvUtils.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                    safeGuard = true;
                    finishCombo();
                }
                return;
            } else {
                Entity target = mc.objectMouseOver.entityHit;
                if (target.isDead) {
                    if (!safeGuard || InvUtils.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                        safeGuard = true;
                        finishCombo();
                    }
                    return;
                }
            }
        }

        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown(0)) {
            Entity target = mc.objectMouseOver.entityHit;
            if (target.isDead) {
                if (onRightMBHold.isToggled() && Mouse.isButtonDown(1) && Mouse.isButtonDown(0)) {
                    if (!safeGuard || InvUtils.isPlayerHoldingWeapon() && Mouse.isButtonDown(0)) {
                        safeGuard = true;
                        finishCombo();
                    }
                }
                return;
            }

            if (this.player().getDistanceToEntity(target) <= range.getValue()) {
                if ((target.hurtResistantTime >= 10 && Utils.Modes.SprintResetTimings.values()[(int) eventType.getValue() - 1] == Utils.Modes.SprintResetTimings.POST) || (target.hurtResistantTime <= 10 && Utils.Modes.SprintResetTimings.values()[(int) eventType.getValue() - 1] == Utils.Modes.SprintResetTimings.PRE)) {

                    if (onlyPlayers.isToggled()) {
                        if (!(target instanceof EntityPlayer)) {
                            return;
                        }
                    }

                    if (NewAntiBot.isBot(target))
                        return;

                    if (hitCoolDown && !alreadyHit) {
                        hitsWaited++;
                        if (hitsWaited >= hitTimeout) {
                            hitCoolDown = false;
                            hitsWaited = 0;
                        } else {
                            alreadyHit = true;
                            return;
                        }
                    }

                    if (!(chance.getValue() == 100 || Math.random() <= chance.getValue() / 100))
                        return;

                    if (!alreadyHit) {
                        guiUpdate();
                        if (hitPer.getInputMin() == hitPer.getInputMax()) {
                            hitTimeout = (int) hitPer.getInputMin();
                        } else {

                            hitTimeout = ThreadLocalRandom.current().nextInt((int) hitPer.getInputMin(), (int) hitPer.getInputMax());
                        }
                        hitCoolDown = true;
                        hitsWaited = 0;

                        actionTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(waitMs.getInputMin(), waitMs.getInputMax() + 0.01));
                        if (postDelay.getInputMax() != 0) {
                            postDelayTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(postDelay.getInputMin(), postDelay.getInputMax() + 0.01));
                            postDelayTimer.start();
                            waitingForPostDelay = true;
                        } else {
                            executingAction = true;
                            startCombo();
                            actionTimer.start();
                            alreadyHit = true;
                            if (safeGuard) safeGuard = false;
                        }
                        alreadyHit = true;
                    }
                } else {
                    if (alreadyHit) {
                        alreadyHit = false;
                    }

                    if (safeGuard) safeGuard = false;
                }
            }
        }
    }

    private static void finishCombo() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, false);
        Utils.Client.setMouseButtonState(1, false);
    }

    private static void startCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            int key = mc.gameSettings.keyBindUseItem.getKeyCode();
            KeyBinding.setKeyBindState(key, true);
            KeyBinding.onTick(key);
            Utils.Client.setMouseButtonState(1, true);
        }
    }

}