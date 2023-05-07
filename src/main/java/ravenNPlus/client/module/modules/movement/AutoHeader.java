package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import io.netty.util.internal.ThreadLocalRandom;
import org.lwjgl.input.Keyboard;

public class AutoHeader extends Module {

    public static TickSetting cancelDuringShift, onlyWhenHoldingSpacebar;
    public static SliderSetting pbs;
    private double startWait;

    public AutoHeader() {
        super("AutoHeadHitter", ModuleCategory.movement, "Spams spacebar when under blocks");
        this.addSetting(cancelDuringShift = new TickSetting("Cancel if sneaking", true));
        this.addSetting(onlyWhenHoldingSpacebar = new TickSetting("Only when holding jump", true));
        this.addSetting(pbs = new SliderSetting("Jump Presses per second", 12, 1, 20, 1));
    }

    @Override
    public void onEnable() {
        startWait = System.currentTimeMillis();
        super.onEnable();
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (!this.inGame() || mc.currentScreen != null)
            return;
        if (cancelDuringShift.isToggled() && this.player().isSneaking())
            return;

        if (onlyWhenHoldingSpacebar.isToggled()) {
            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                return;
            }
        }


        if (Utils.Player.playerUnderBlock() && this.player().onGround) {
            if (startWait + (1000 / ThreadLocalRandom.current().nextDouble(pbs.getValue() - 0.543543, pbs.getValue() + 1.32748923)) < System.currentTimeMillis()) {
                this.player().jump();
                startWait = System.currentTimeMillis();
            }
        }

    }

}