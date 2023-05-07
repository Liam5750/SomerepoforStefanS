package ravenNPlus.client.module.modules.render;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReachCircle extends Module {

    public static SliderSetting red, green, blue, expand;
    public static TickSetting damage, invis;
    int E;

    public ReachCircle() {
        super("ReachCircle", ModuleCategory.render, "Renders a circle around Players to see the reach");
        this.addSetting(red = new SliderSetting("Red", 150, 0, 255, 1));
        this.addSetting(green = new SliderSetting("Green", 10, 0, 255, 1));
        this.addSetting(blue = new SliderSetting("Blue", 250, 0, 255, 1));
        this.addSetting(invis = new TickSetting("Show invis", x));
        this.addSetting(damage = new TickSetting("Red on damage", x));
        this.addSetting(expand = new SliderSetting("Expand", -0.3, -0.5, 5, 0.1));
    }

    public void guiUpdate() {
        E = new java.awt.Color((int) red.getValue(), (int) blue.getValue(), (int) green.getValue()).getRGB();
    }

    @Override
    public void onDisable() {
        Utils.HUD.ring_c = false;
    }

    @SubscribeEvent
    public void r1(RenderWorldLastEvent e) {
        if (!this.inGame()) return;

        java.util.Iterator var3;
        var3 = mc.theWorld.playerEntities.iterator();
        while (true) {
            net.minecraft.entity.player.EntityPlayer en;
            do {
                do {
                    do {
                        if (!var3.hasNext()) {
                            return;
                        }
                        en = (net.minecraft.entity.player.EntityPlayer) var3.next();
                    } while (en == this.player());
                } while (en.deathTime != 0);
            } while (!invis.isToggled() && en.isInvisible());

            if (!NewAntiBot.isBot(en)) {
                Utils.HUD.drawBoxAroundEntity(en, 1, 1 + expand.getValue(), 1.5D, E, damage.isToggled());
            }
        }
    }

}