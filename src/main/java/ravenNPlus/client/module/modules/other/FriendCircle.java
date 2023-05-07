package ravenNPlus.client.module.modules.other;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.RenderUtils;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;
import java.util.stream.Collectors;

public class FriendCircle extends Module {

    public static SliderSetting red, green, blue, opacity, range, mode;
    public static TickSetting damage;

    public FriendCircle() {
        super("Friend Circle", ModuleCategory.other, "Draws circles around your Friends");
        this.addSetting(damage = new TickSetting("Red on Damage", true));
        this.addSetting(mode = new SliderSetting("Mode", 1, 1, 6, 1));
        this.addSetting(range = new SliderSetting("Range", 3.7D, 2D, 8D, 0.1D));
        this.addSetting(red = new SliderSetting("Red", 250, 0, 255, 1));
        this.addSetting(green = new SliderSetting("Green", 0, 0, 255, 1));
        this.addSetting(blue = new SliderSetting("Blue", 255, 0, 255, 1));
        this.addSetting(opacity = new SliderSetting("Opacity", 0.4, 0, 1, 0.1));
    }

    @SubscribeEvent
    public void r(TickEvent.RenderTickEvent e) {
        if (!this.inGame()) return;

        java.util.List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        if (targets.isEmpty()) return;
        EntityLivingBase target = (EntityLivingBase) targets.get(0);

        RenderUtils.renderFriends(target, mode.getValueToInt(), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), (int) opacity.getValue()).getRGB(), damage.isToggled());
    }

}