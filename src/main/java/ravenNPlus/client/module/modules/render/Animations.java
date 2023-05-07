package ravenNPlus.client.module.modules.render;

import org.lwjgl.opengl.GL11;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animations extends Module {

    public static SliderSetting x, y, z, s, block_height, height, digSlow, digSpeed;
    public static TickSetting onlyGround, onlyServer, onlySprint;
    public static ModeSetting mode;

    public Animations() {
        super("Animations", ModuleCategory.render, "Modifies your Hand Animation");
        this.addSetting(x = new SliderSetting("X", -1.8D, -2D, 2D, -0.1D));
        this.addSetting(y = new SliderSetting("Y", 0.8D, -2D, 2D, -0.1D));
        this.addSetting(z = new SliderSetting("Z", 2.0D, -2D, 2D, -0.1D));
        this.addSetting(s = new SliderSetting("Speed", 323.0D, 5D, 500D, 1D));
        this.addSetting(mode = new ModeSetting("Mode", Animations.modes.Mode6));
        this.addSetting(block_height = new SliderSetting("Block Height", 1D, 1D, 10D, 1D));
        this.addSetting(height = new SliderSetting("Height", 1D, 1D, 10D, 1D));
        this.addSetting(digSlow = new SliderSetting("DigSlowdown", 10D, 0D, 100D, 1D));
        this.addSetting(digSpeed = new SliderSetting("DigSpeed", 0D, 1D, 100D, 1D));
        this.addSetting(onlyGround = new TickSetting("Only onGround", false));
        this.addSetting(onlyServer = new TickSetting("Only Server", false));
        this.addSetting(onlySprint = new TickSetting("Only Sprint", false));
    }

    @SubscribeEvent
    public void onRenderArms(RenderHandEvent e) {
        if (onlyGround.isToggled())
            if (!this.player().onGround) return;

        if (onlyServer.isToggled())
            if (mc.isSingleplayer()) return;

        if (onlySprint.isToggled())
            if (!this.player().isSprinting()) return;

        if (mode.getMode() == Animations.modes.Mode1 && e.partialTicks > 0) {
            final float angle = 1f - e.partialTicks * s.getValueToFloat();
            GL11.glRotated(angle, x.getValue(), y.getValue(), z.getValue());
        }

        if (mode.getMode() == Animations.modes.Mode2 && e.partialTicks > 0) {
            double a = height.getValue();
            float heightSet = (float) (20.0F - (this.player().isSwingInProgress && this.isBlocking() ? block_height.getValue() : this.player().getHeldItem() == null ? 0.0F : height.getValue()));

            if (1 < heightSet) {
                a += 1.0F;
            } else if (a > heightSet) {
                a -= 1.0F;
            }
        }

        if (mode.getMode() == Animations.modes.Mode3) {
            Utils.Player.SlowSwing(digSlow.getValue());
        }

        if (mode.getMode() == Animations.modes.Mode4) {
            Utils.Player.FastSwing(digSpeed.getValue());
        }

        if (mode.getMode() == Animations.modes.Mode5) {
            if (e.renderPass > 0) {
                final float angle = (1f - Utils.Player.getArmSwingAnimationEnd(this.player())) * 360F;

                GL11.glRotatef(angle, 0, 0, 1);
            }
        }

        if (mode.getMode() == Animations.modes.Mode6) {
            if (e.partialTicks > 0) {
                final float angle = (1f - this.player().swingProgress) * 360;

                GL11.glRotatef(angle, 0, 1.8f, 7f);
            }
        }
    }

    public enum modes {
        Mode1, Mode2, Mode3,
        Mode4, Mode5, Mode6
    }

}