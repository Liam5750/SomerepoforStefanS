package ravenNPlus.client.module.modules.combat;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import ravenNPlus.client.utils.*;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillAura extends Module {

    public static ModeSetting mode;
    public static TickSetting background, onlySprint, onlyWeapon, swing, drawHUD, lookPacket, onlyAlive, disableOnDeath, disableOnFall;
    public static TickSetting silent, ignoreFriends, head, allowBlock, onlyInFocus, sortBots, fovv, fovIndicator;
    public static SliderSetting range, posX, posY, delay, chance, fov;
    public static DescriptionSetting desc;
    public static boolean isHudShown = false;

    public KillAura() {
        super("KillAura", ModuleCategory.combat, "Automatically Attacks Players");

        this.addSetting(desc = new DescriptionSetting("Attack Settings"));
        this.addSetting(mode = new ModeSetting("Attack Mode", KillAura.modes.Legit));
        this.addSetting(swing = new TickSetting("Swing", x));
        this.addSetting(silent = new TickSetting("Silent", x));
        this.addSetting(lookPacket = new TickSetting("Look Packet", o));
        this.addSetting(chance = new SliderSetting("Chance %", 100.0D, 0.0D, 100.0D, 1.0D));
        this.addSetting(range = new SliderSetting("Range", 3.3D, 2D, 6D, 0.1D));
        this.addSetting(delay = new SliderSetting("Delay", 46.0D, 25.0D, 150.0D, 0.5D));
        this.addSetting(desc = new DescriptionSetting("Disable Settings"));
        this.addSetting(disableOnDeath = new TickSetting("on Death (0 Hearts)", x));
        this.addSetting(disableOnFall = new TickSetting("on Fall (5 Blocks)", o));
        this.addSetting(desc = new DescriptionSetting("Allow Settings"));
        this.addSetting(ignoreFriends = new TickSetting("Ignore Friends", x));
        this.addSetting(sortBots = new TickSetting("Ignore Bots", x));
        this.addSetting(allowBlock = new TickSetting("Allow blocking", o));
        this.addSetting(onlyInFocus = new TickSetting("Only When focus", x));
        this.addSetting(onlyWeapon = new TickSetting("Only Weapon", x));
        this.addSetting(onlyAlive = new TickSetting("Only Alive", x));
        this.addSetting(onlySprint = new TickSetting("Only Sprint", o));
        this.addSetting(desc = new DescriptionSetting("Fov Settings"));
        this.addSetting(fovv = new TickSetting("Fov", x));
        this.addSetting(fovIndicator = new TickSetting("Fov Indicator", x));
        this.addSetting(fov = new SliderSetting("Fov", 40.0D, 15.0D, 360.0D, 1.0D));
        this.addSetting(desc = new DescriptionSetting("HUD Settings"));
        this.addSetting(drawHUD = new TickSetting("Draw HUD", x));
        this.addSetting(head = new TickSetting("Head", o));
        this.addSetting(background = new TickSetting("Background", x));
        this.addSetting(posX = new SliderSetting("HUD X", (float) mc.displayWidth / 2 + 5, 20, mc.displayWidth + 50, 1));
        this.addSetting(posY = new SliderSetting("HUD Y", (float) mc.displayHeight / 2 - 10, 20, mc.displayHeight + 50, 1));
    }

    @Override
    public void onDisable() {

        // animation reset
        circleRange = 0;
        RenderUtils.drawStringHUD_Disable(range.getValue(), sortBots.isToggled(), KillAura.posX.getValueToInt() + 2);

        if (swing.isToggled())
            this.swing(!silent.isToggled());

        isHudShown = false;
    }

    @SubscribeEvent
    public void r(TickEvent.PlayerTickEvent p) {
        if (!this.inGame()) return;

        if (canAttack(getTarget())) {

            if (CombatUtils.isEntityInPlayerRange(getTarget(), range.getValue(), true)) {

                if (Timer.hasTimeElapsed(delay.getValueToLong() * 5, true)) {

                    if (mode.getMode() == KillAura.modes.Packets) {
                        Utils.Player.aim(getTarget(), 0.0F, lookPacket.isToggled(), silent.isToggled());

                        this.sendPacketPlayer(new C02PacketUseEntity(getTarget(), C02PacketUseEntity.Action.ATTACK));

                        if (swing.isToggled())
                            performSwing();
                    }

                    if (mode.getMode() == KillAura.modes.NetPackets) {
                        Utils.Player.aim(getTarget(), 0.0F, lookPacket.isToggled(), silent.isToggled());
                        this.sendPacketNetHandler(new C02PacketUseEntity(getTarget(), C02PacketUseEntity.Action.ATTACK));

                        if (swing.isToggled())
                            performSwing();
                    }

                    if (mode.getMode() == KillAura.modes.AttackEntityVoid) {
                        Utils.Player.aim(getTarget(), 0.0F, lookPacket.isToggled(), silent.isToggled());
                        this.attackPlayer(getTarget());

                        if (swing.isToggled())
                            performSwing();
                    }

                    if (mode.getMode() == KillAura.modes.HitByEntity) {
                        Utils.Player.aim(getTarget(), 0.0F, lookPacket.isToggled(), silent.isToggled());

                        AttackEntityEvent e = new AttackEntityEvent(this.player(), getTarget());
                        e.target.hitByEntity(getTarget());

                        if (swing.isToggled())
                            performSwing();
                    }

                    if (mode.getMode() == KillAura.modes.Legit) {
                        if (mc.objectMouseOver.entityHit instanceof net.minecraft.entity.player.EntityPlayer) {
                            this.attackPlayer(getTarget());

                            if (swing.isToggled())
                                performSwing();
                        }
                    }

                    // swing when mc.thePlayer killed someone

                }
            }
        }

    }

    // animation integer
    static int circleRange = 0;

    @SubscribeEvent
    public void p(TickEvent.RenderTickEvent e) {
        if (!this.inGame()) return;

        if (fovv.isToggled() && fovIndicator.isToggled()) {
            int foV = fov.getValueToInt() + 10;

            // animation in
            if (circleRange < foV /*|| mc.thePlayer.getDistanceToEntity(getTarget()) > range.getValueToFloat()*/)
                circleRange++;

            // animation out
            if (circleRange > foV /*|| mc.thePlayer.getDistanceToEntity(getTarget()) < range.getValueToFloat()*/)
                circleRange--;

            if (canAttack(getTarget()) && foV < 280)
                RenderUtils.drawCircle(this.screenWidth(), this.screenHeight(), circleRange, 1, 380);
        }

        if (drawHUD.isToggled())
            drawHud();
    }

    public static void drawHud() {
        try {
            RenderUtils.drawStringHUD(posX.getValueToInt(), posY.getValueToInt(), range.getValueToInt(), background.isToggled(), true, head.isToggled(), sortBots.isToggled());
        } catch (Exception e) {
            e.printStackTrace();
            head.disable();
        }
        isHudShown = true;
    }

    //----------------------------------------------------------------------------------------------------

    boolean canAttack(Entity en) {

        if (en == mc.thePlayer || getTarget() == null)
            return false;

        if (sortBots.isToggled())
            if (NewAntiBot.isBot(en))
                return false;

        if (ignoreFriends.isToggled())
            if (Utils.FriendUtils.isAFriend(en))
                return false;

        if (!(chance.getValue() == 100 || Math.random() <= chance.getValue() / 100))
            return false;

        if (fovv.isToggled())
            if (!Utils.Player.fov(en, fov.getValueToFloat() + 10))
                return false;

        if (onlyAlive.isToggled())
            if (!this.isAlive())
                return false;

        if (onlyInFocus.isToggled())
            if (!this.inFocus())
                return false;

        if (allowBlock.isToggled())
            if (!this.player().isBlocking())
                return false;

        if (onlySprint.isToggled())
            if (!this.player().isSprinting())
                return false;

        if (onlyWeapon.isToggled())
            if (!InvUtils.isPlayerHoldingWeapon())
                return false;

        return true;
    }

    public Entity getTarget() {
        java.util.List<net.minecraft.entity.Entity> targets = mc.theWorld.loadedEntityList.stream().filter(net.minecraft.entity.EntityLivingBase.class::isInstance).collect(java.util.stream.Collectors.toList());
        targets = targets.stream().filter(entity -> entity != mc.thePlayer && !entity.isDead && ((net.minecraft.entity.EntityLivingBase) entity).getHealth() > 0).collect(java.util.stream.Collectors.toList());
        targets.sort(java.util.Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
        EntityLivingBase target = null;
        
        if (!targets.isEmpty()) {
            target = (EntityLivingBase) targets.get(0);
        }
        
        return target;
    }

    public void performSwing() {
        if (mc.objectMouseOver.entityHit instanceof net.minecraft.entity.player.EntityPlayer) {
            this.attackPlayer(getTarget());
        }

        if (swing.isToggled())
            this.swing(!silent.isToggled());
    }

    public enum modes {
        Packets, NetPackets, AttackEntityVoid,
        HitByEntity, Legit, //Maybe More ?
    }

}