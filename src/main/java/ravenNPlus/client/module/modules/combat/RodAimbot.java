package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;

public class RodAimbot extends Module {

    public static DescriptionSetting desc;
    public static SliderSetting fov, distance;
    public static TickSetting invis, silent;

    public RodAimbot() {
        super("RodAimbot", ModuleCategory.combat, "Credits: Kopamed");
        this.addSetting(desc = new DescriptionSetting("Credits: Kopamed"));
        this.addSetting(fov = new SliderSetting("FOV", 90.0D, 15.0D, 360.0D, 1.0D));
        this.addSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
        this.addSetting(invis = new TickSetting("Aim invis", o));
        this.addSetting(silent = new TickSetting("Silent", o));
    }

    @SubscribeEvent
    public void x(MouseEvent ev) {
        if (ev.button == 1 && ev.buttonstate && Utils.Player.isPlayerInGame() && mc.currentScreen == null) {
            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod && mc.thePlayer.fishEntity == null) {
                Entity en = this.gE();
                if (en != null) {
                    ev.setCanceled(true);
                    Utils.Player.aim(en, -7.0F, true, silent.isToggled());
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                }
            }
        }
    }

    public Entity gE() {
        int f = fov.getValueToInt();
        Iterator var2 = mc.theWorld.playerEntities.iterator();

        EntityPlayer en;
        do {
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return null;
                        }

                        en = (EntityPlayer) var2.next();
                    } while (en == mc.thePlayer);
                } while (en.deathTime != 0);
            } while (!invis.isToggled() && en.isInvisible());
        } while ((double) mc.thePlayer.getDistanceToEntity(en) > distance.getValue() || NewAntiBot.isBot(en) || !Utils.Player.fov(en, (float) f));

        return en;
    }

}