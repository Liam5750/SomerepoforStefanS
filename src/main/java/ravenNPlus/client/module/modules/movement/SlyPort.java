package ravenNPlus.client.module.modules.movement;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.SoundUtil;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;

public class SlyPort extends Module {

    public static TickSetting sound, playersOnly, aim, ignoreFriends;
    public static SliderSetting range;

    public SlyPort() {
        super("SlyPort", ModuleCategory.movement, "Teleport behind enemies");
        this.addSetting(range = new SliderSetting("Range", 6.0D, 2.0D, 15.0D, 1.0D));
        this.addSetting(aim = new TickSetting("Aim", true));
        this.addSetting(sound = new TickSetting("Play sound", true));
        this.addSetting(playersOnly = new TickSetting("Players only", true));
        this.addSetting(ignoreFriends = new TickSetting("Ignore Friends", true));
    }

    public void onEnable() {
        Entity en = this.ge();
        if (en != null) {
            this.tp(en);
        }

        this.disable();
    }

    private void tp(Entity en) {
        if (sound.isToggled()) {
            SoundUtil.play(SoundUtil.endermenPortal, 1F, 1F);
        }

        if (ignoreFriends.isToggled())
            if (Utils.FriendUtils.isAFriend(en)) return;

        Vec3 vec = en.getLookVec();
        double x = en.posX - vec.xCoord * 2.5D;
        double z = en.posZ - vec.zCoord * 2.5D;
        this.player().setPosition(x, this.player().posY, z);
        if (aim.isToggled()) {
            Utils.Player.aim(en, 0.0F, false, false);
        }
    }

    private Entity ge() {
        Entity en = null;
        double r = Math.pow(SlyPort.range.getValue(), 2.0D);
        double dist = r + 1.0D;
        Iterator<Entity> var6 = mc.theWorld.loadedEntityList.iterator();

        while (true) {
            Entity ent;
            do {
                do {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                return en;
                            }

                            ent = var6.next();
                        } while (ent == this.player());
                    } while (!(ent instanceof EntityLivingBase));
                } while (((EntityLivingBase) ent).deathTime != 0);
            } while (SlyPort.playersOnly.isToggled() && !(ent instanceof EntityPlayer));

            if (!NewAntiBot.isBot(ent)) {
                double d = this.player().getDistanceSqToEntity(ent);
                if (!(d > r) && !(dist < d)) {
                    dist = d;
                    en = ent;
                }
            }
        }
    }

}