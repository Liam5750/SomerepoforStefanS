package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.*;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class DoubleClicker extends Module {

    public static SliderSetting chance, delay;
    public static TickSetting rightClick, leftClick, onlySword, onlySprint;
    public static boolean hasClicked_R = false, hasClicked_L = false;

    public DoubleClicker() {
        super("DoubleClicker", ModuleCategory.combat, "Double Clicks like the Mouse Click Method");
        this.addSetting(rightClick = new TickSetting("Right Click", x));
        this.addSetting(leftClick = new TickSetting("Left Click", o));
        this.addSetting(chance = new SliderSetting("Chance %", 100, 0, 100, 1));
        this.addSetting(delay = new SliderSetting("Delay", 5, 1, 50, 1));
        this.addSetting(onlySword = new TickSetting("Only Sword", x));
        this.addSetting(onlySprint = new TickSetting("Only Sprinting", o));
    }

    @Override
    public void onDisable() {
        reset();
    }

    @SubscribeEvent
    public void r(TickEvent.PlayerTickEvent e) {
        if (!this.inGame() || !this.isEnabled())
            return;

        if (onlySword.isToggled())
            if (!InvUtils.isPlayerHoldingWeapon())
                return;

        if (onlySprint.isToggled())
            if (!mc.thePlayer.isSprinting())
                return;

        if (rightClick.isToggled()) {
            rightClick(delay.getValueToLong(), 0);

            if (Timer.hasTimeElapsed(delay.getValueToLong() / 2))
                unpressKey(0);
        }

        if (rightClick.isToggled()) {
            rightClick(delay.getValueToLong(), 1);

            if (Timer.hasTimeElapsed(delay.getValueToLong() / 2))
                unpressKey(1);
        }

        if (Timer.hasTimeElapsed(100L, x))
            reset();

        // press mouse right or left with cps settings
        // maybe add delay between multiple clicks

    }

    //------------------------------------------------------------------------------------------------------------

    public void rightClick(long delay, int key) {
        if (rightClick.isToggled() && !hasClicked_R) {
            if (Mouse.isButtonDown(0)) {

                if (Timer.hasTimeElapsed(delay, x)) {

                    if (!(chance.getValue() == 100 || Math.random() <= chance.getValue() / 100))
                        return;

                    Utils.Client.setMouseButtonState(key, x);
                }

                hasClicked_R = x;
            }
        }
    }

    public void leftClick(long delay, int key) {
        if (leftClick.isToggled() && !hasClicked_L) {
            if (Mouse.isButtonDown(1)) {

                if (Timer.hasTimeElapsed(delay, x)) {

                    if (!(chance.getValue() == 100 || Math.random() <= chance.getValue() / 100))
                        return;

                    Utils.Client.setMouseButtonState(key, x);
                }

                hasClicked_L = x;
            }
        }
    }

    public void unpressKey(int key) {
        Utils.Client.setMouseButtonState(key, o);
    }

    public void reset() {
        if (hasClicked_L)
            hasClicked_L = o;
        if (hasClicked_R)
            hasClicked_R = o;
    }

    //------------------------------------------------------------------------------------------------------------

}