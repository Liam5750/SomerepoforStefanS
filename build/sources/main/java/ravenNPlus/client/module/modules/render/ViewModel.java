package ravenNPlus.client.module.modules.render;

import org.lwjgl.opengl.GL11;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel extends Module {

    public static SliderSetting Trandx, Trandy, Trandz, Tranfx, Tranfy, Tranfz;
    public static TickSetting translateF, onlyGround, onlyServer, onlySprint;

    public ViewModel() {
        super("ViewModel", ModuleCategory.render, "Modifies your Hand Model");
        this.addSetting(Trandx = new SliderSetting("TranslateD X", 1.0D, -2D, 2D, -0.1D));
        this.addSetting(Trandy = new SliderSetting("TranslateD Y", -0.2D, -2D, 2D, -0.1D));
        this.addSetting(Trandz = new SliderSetting("TranslateD Z", -0.9D, -2D, 2D, -0.1D));
        this.addSetting(translateF = new TickSetting("Translate F", true));
        this.addSetting(Tranfx = new SliderSetting("TranslateF X", 1.0D, -2D, 2D, -0.1D));
        this.addSetting(Tranfy = new SliderSetting("TranslateF Y", -0.2D, -2D, 2D, -0.1D));
        this.addSetting(Tranfz = new SliderSetting("TranslateF Z", -0.9D, -2D, 2D, -0.1D));
        this.addSetting(onlyGround = new TickSetting("Only onGround", false));
        this.addSetting(onlyServer = new TickSetting("Only Server", false));
        this.addSetting(onlySprint = new TickSetting("Only Sprint", false));
    }

    @SubscribeEvent
    public void onRenderArms(RenderHandEvent e) {
        if (!this.inGame()) return;

        if (onlyGround.isToggled())
            if (!this.player().onGround) return;

        if (onlyServer.isToggled())
            if (mc.isSingleplayer()) return;

        if (onlySprint.isToggled())
            if (!this.player().isSprinting()) return;

        if (translateF.isToggled()) {
            GL11.glTranslated(Trandx.getValue(), Trandy.getValue(), Trandz.getValue());
        } else {
            GL11.glTranslatef(Tranfx.getValueToFloat(), Tranfy.getValueToFloat(), Tranfz.getValueToFloat());
        }
    }

}