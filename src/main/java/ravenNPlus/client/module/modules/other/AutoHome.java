package ravenNPlus.client.module.modules.other;

import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AutoHome extends Module {

    public static ModeSetting mode;
    public static SliderSetting range, delay;

    public AutoHome() {
        super("AutoHome", ModuleCategory.other, "Auto TPs home when a entity is in a range");
        this.addSetting(range = new SliderSetting("Range", 5, 1, 50, 1));
        this.addSetting(mode = new ModeSetting("Command Mode", AutoHome.modes.home));
        this.addSetting(delay = new SliderSetting("Delay", 0, 0, 50, 1));
    }

    @SubscribeEvent
    public void p(TickEvent.PlayerTickEvent e) {
        if (!this.inGame()) return;

        List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
        targets = targets.stream().filter(entity -> entity.getDistanceToEntity(this.player()) < range.getValueToInt() && entity != this.player() && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
        targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(this.player())));
        if (targets.isEmpty()) return;
        EntityLivingBase target = (EntityLivingBase) targets.get(0);

        if (NewAntiBot.isBot(target)) return;

        if (mode.getMode() == AutoHome.modes.home) {
            if (target.getDistanceToEntity(this.player()) < range.getValueToFloat()) {
                if (Timer.hasTimeElapsed(delay.getValueToLong() * 5, true))
                    this.player().sendChatMessage("/home");
            }
        }

        if (mode.getMode() ==  AutoHome.modes.back) {
            if (target.getDistanceToEntity(this.player()) < range.getValueToFloat()) {
                if (Timer.hasTimeElapsed(delay.getValueToLong() * 5, true))
                    this.player().sendChatMessage("/back");
            }
        }

        if (mode.getMode() ==  AutoHome.modes.is) {
            if (target.getDistanceToEntity(this.player()) < range.getValueToFloat()) {
                if (Timer.hasTimeElapsed(delay.getValueToLong() * 5, true))
                    this.player().sendChatMessage("/is");
            }
        }

        if (mode.getMode() ==  AutoHome.modes.spawn) {
            if (target.getDistanceToEntity(this.player()) < range.getValueToFloat()) {
                if (Timer.hasTimeElapsed(delay.getValueToLong() * 5, true))
                    this.player().sendChatMessage("/spawn");
            }
        }

        if (mode.getMode() ==  AutoHome.modes.hub) {
            if (target.getDistanceToEntity(this.player()) < range.getValueToFloat()) {
                if (Timer.hasTimeElapsed(delay.getValueToLong() * 5, true))
                    this.player().sendChatMessage("/hub");
            }
        }
    }

    public enum modes {
        home, back, is, spawn, hub
    }

}