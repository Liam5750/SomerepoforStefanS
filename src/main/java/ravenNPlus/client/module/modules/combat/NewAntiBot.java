package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.Utils.Player;
import ravenNPlus.client.utils.CombatUtils;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class NewAntiBot extends Module {
    public static TickSetting nameCheck, playerCheck, invisCheck, zeroHealthChecks;
    public static TickSetting noPushChecks, flyChecks, tabChecks, twiceChecks, pingCheck;
    public static SliderSetting range;
    public static ModeSetting sortMode;

    public NewAntiBot() {
        super("AntiBots", ModuleCategory.combat, "Sorts Bots (V2)");
        this.addSetting(sortMode = new ModeSetting("Sort Mode", NewAntiBot.sortModes.Hypixel));
        this.addSetting(range = new SliderSetting("Range", 5.0, 1.0, 25.0, 1.0));
        this.addSetting(nameCheck = new TickSetting("Name Checks", this.x));
        this.addSetting(playerCheck = new TickSetting("Player Checks", this.x));
        this.addSetting(invisCheck = new TickSetting("Invisible Checks", this.x));
        this.addSetting(pingCheck = new TickSetting("0 Ping Checks", this.x));
        this.addSetting(tabChecks = new TickSetting("Tab Checks", this.o));
        this.addSetting(flyChecks = new TickSetting("Fly Checks", this.o));
        this.addSetting(noPushChecks = new TickSetting("NoPush Checks", this.o));
        this.addSetting(twiceChecks = new TickSetting("Twice UUID Checks", this.o));
        this.addSetting(zeroHealthChecks = new TickSetting("ZeroHealth Checks", this.x));
    }

    public static boolean isBot(Entity en) {
        Module antiBot = Client.moduleManager.getModuleByClazz(NewAntiBot.class);

        if (antiBot.isEnabled()) {
            if (!Player.isPlayerInGame()) {

                return ravenNPlus.client.module.modules.player.Freecam.en != null && ravenNPlus.client.module.modules.player.Freecam.en == en;
            } else {
                if (mc.thePlayer.getDistanceToEntity(en) > (float) range.getValueToLong()) {
                    return true;
                }

                if (sortMode.getMode() == NewAntiBot.sortModes.Hypixel)
                    return isHypixelBot(en);

                if (sortMode.getMode() == NewAntiBot.sortModes.Costum)
                    return isCostumBot(en);
            }
        } else {
            return false;
        }

        return false;
    }

    public static boolean isHypixelBot(Entity en) {
        if (playerCheck.isToggled())
            return !(en instanceof EntityPlayer);

        if (invisCheck.isToggled())
            return en.isInvisibleToPlayer(mc.thePlayer);

        if (nameCheck.isToggled())
            return isBotName(en, 1);

        if (noPushChecks.isToggled())
            if (!en.canBePushed())
                return true;

        if (tabChecks.isToggled())
            if (!CombatUtils.inTab((EntityLivingBase) en))
                return true;

        return false;
    }

    public static boolean isCostumBot(Entity en) {
        if (playerCheck.isToggled())
            if (!(en instanceof EntityPlayer))
                return true;

        if (twiceChecks.isToggled())
            if (!CombatUtils.isPlayerTwiceInGame())
                return true;

        if (invisCheck.isToggled())
            if (en.isInvisibleToPlayer(mc.thePlayer))
                return true;

        if (nameCheck.isToggled())
            if (isBotName(en, 2))
                return true;

        if (noPushChecks.isToggled())
            if (!en.canBePushed())
                return true;

        if (zeroHealthChecks.isToggled())
            if (((EntityLivingBase) en).getHealth() < 0.0F || en.isDead)
                return true;

        if (flyChecks.isToggled())
            if (!en.onGround && ((net.minecraft.client.entity.EntityPlayerSP) en).capabilities.isFlying)
                return true;

        if (tabChecks.isToggled())
            if (!CombatUtils.inTab((EntityLivingBase) en))
                return true;

        if (pingCheck.isToggled())
            if (mc.getNetHandler().getPlayerInfo(en.getName()).getResponseTime() < 3)
                return true;

        return false;
    }

    private static boolean isBotName(Entity en, int mode) {
        String rawName = en.getDisplayName().getUnformattedText().toLowerCase();
        String forName = en.getDisplayName().getFormattedText().toLowerCase();

        if (mode == 1) {
            if (forName.startsWith("§r§8[npc]")) {
                return true;
            }

            for (EntityPlayer list : mc.theWorld.playerEntities) {
                if (list != mc.thePlayer && !list.isDead && list.isInvisible() && CombatUtils.getPlayerList().contains(list) && list.getCustomNameTag().length() >= 2) {
                    mc.theWorld.removeEntity(list);
                    return true;
                }
            }
        }

        if (mode == 2) {
            if (forName.contains("]")) return true;
            if (forName.contains("[")) return true;
            if (rawName.contains("-")) return true;
            if (rawName.contains(":")) return true;
            if (rawName.contains("+")) return true;
            if (rawName.startsWith("cit")) return true;
            if (rawName.startsWith("npc")) return true;
        }

        return rawName.isEmpty() || rawName.contains(" ") || forName.isEmpty();
    }

    public enum sortModes {
        Hypixel, Costum
    }

}