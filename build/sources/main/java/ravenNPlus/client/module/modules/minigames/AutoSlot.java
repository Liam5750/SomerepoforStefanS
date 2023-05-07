package ravenNPlus.client.module.modules.minigames;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.Timer;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AutoSlot extends Module {

    public static SliderSetting autoDisDistance, autoDisableHealth, delay, playerRange;
    public static TickSetting autoThrow, autoDis, onlyOnline;
    public static ModeSetting mode;
    boolean d = false;

    public AutoSlot() {
        super("AutoSlot", ModuleCategory.minigame, "Auto slot before Game starts");
        this.addSetting(onlyOnline = new TickSetting("Only on Servers", true));
        this.addSetting(mode = new ModeSetting("Mode", AutoSlot.modes.ExperienceLevel));
        this.addSetting(playerRange = new SliderSetting("Player Range", 4, 1, 50, 1));
        this.addSetting(autoDis = new TickSetting("Auto Disable", false));
        this.addSetting(autoDisDistance = new SliderSetting("Auto Disable Distance", 3D, 1D, 10D, 1D));
        this.addSetting(autoDisableHealth = new SliderSetting("Auto Disable Health", 10D, 1D, 10D, 1D));
        this.addSetting(autoThrow = new TickSetting("Auto Throw Kit", false));
        this.addSetting(delay = new SliderSetting("Auto Trow Delay", 1200D, 200D, 5000D, 100D));
    }

    @SubscribeEvent
    public void p(TickEvent.PlayerTickEvent e) {
        if (!this.inGame()) return;
        if (this.player().isEntityAlive() && this.player().getHealth() > 0 && this.player().onGround && !this.player().isRiding()
                && !this.player().isInWater() && !this.player().isInLava() && this.player().experienceLevel < 6) {

            if (onlyOnline.isToggled())
                if (this.isSingleplayer()) return;

            if (mode.getMode() == AutoSlot.modes.ExperienceLevel) {
                if (this.player().experienceLevel == 5) {
                    InvUtils.setCurrentPlayerSlot(4);
                } else if (this.player().experienceLevel == 4) {
                    InvUtils.setCurrentPlayerSlot(3);
                } else if (this.player().experienceLevel == 3) {
                    InvUtils.setCurrentPlayerSlot(2);
                } else if (this.player().experienceLevel == 2) {
                    InvUtils.setCurrentPlayerSlot(1);
                } else if (this.player().experienceLevel == 1) {
                    InvUtils.setCurrentPlayerSlot(0);
                    d = true;
                }

                if (autoDis.isToggled() && this.player().getHealth() < autoDisableHealth.getValue() * 2
                        && this.player().fallDistance < autoDisDistance.getValue() && this.player().experienceLevel == 0) {
                    this.disable();
                }
            }

            if (mode.getMode() == AutoSlot.modes.DisplayName) {

                if (this.player().getDisplayNameString().equalsIgnoreCase("5")) {
                    InvUtils.setCurrentPlayerSlot(4);
                } else if (this.player().getDisplayNameString().equalsIgnoreCase("4")) {
                    InvUtils.setCurrentPlayerSlot(3);
                } else if (this.player().getDisplayNameString().equalsIgnoreCase("3")) {
                    InvUtils.setCurrentPlayerSlot(2);
                } else if (this.player().getDisplayNameString().equalsIgnoreCase("3")) {
                    InvUtils.setCurrentPlayerSlot(1);
                } else if (this.player().getDisplayNameString().equalsIgnoreCase("1")) {
                    InvUtils.setCurrentPlayerSlot(0);
                }

                if (autoDis.isToggled() && this.player().getHealth() < autoDisableHealth.getValue() * 2
                        && this.player().fallDistance < autoDisDistance.getValue() && this.player().getDisplayNameString().equalsIgnoreCase("0")) {
                    this.disable();
                }
            }

            if (mode.getMode() == AutoSlot.modes.PlayerInRange) {

                List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
                targets = targets.stream().filter(entity -> entity.getDistanceToEntity(this.player()) < (int) playerRange.getValue() && entity
                        != this.player() && !entity.isDead && ((EntityLivingBase) entity).getHealth() > 0).collect(Collectors.toList());
                targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase) entity).getDistanceToEntity(this.player())));
                if (targets.isEmpty()) return;
                EntityLivingBase target = (EntityLivingBase) targets.get(0);

                if (target.getDistanceToEntity(this.player()) > playerRange.getValue()) {
                    this.player().inventory.currentItem = 0;
                }
            }

            if (autoThrow.isToggled() && d && Timer.hasTimeElapsed((long) delay.getValue(), true)) {
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, 1, 0, 1, this.player());
                Timer.hasTimeElapsed(1000L, true);
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, 2, 0, 1, this.player());
                Timer.hasTimeElapsed(1000L, true);
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, 3, 0, 1, this.player());
                Timer.hasTimeElapsed(1000L, true);
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, 4, 0, 1, this.player());
                autoThrow.disable();
            }
        }
    }

    @SubscribeEvent
    public void onChatMessageRecieved(ClientChatReceivedEvent event) {
        if (this.player().isEntityAlive() && this.player().getHealth() > 0 && this.player().onGround && !this.player().isRiding()
                && !this.player().isInWater() && !this.player().isInLava() && this.player().experienceLevel < 6) {
            if (!this.inGame()) return;

            if (onlyOnline.isToggled())
                if (this.isSingleplayer()) return;

            if (mode.getMode() == AutoSlot.modes.ChatMessage) {
                if (Utils.Java.str(event.message.getUnformattedText()).contains("5") || Utils.Java.str(event.message.getUnformattedText()).contains("Five")) {
                    this.player().inventory.currentItem = 4;
                } else if (Utils.Java.str(event.message.getUnformattedText()).contains("4") || Utils.Java.str(event.message.getUnformattedText()).contains("Four")) {
                    this.player().inventory.currentItem = 3;
                } else if (Utils.Java.str(event.message.getUnformattedText()).contains("3") || Utils.Java.str(event.message.getUnformattedText()).contains("Three")) {
                    this.player().inventory.currentItem = 2;
                } else if (Utils.Java.str(event.message.getUnformattedText()).contains("2") || Utils.Java.str(event.message.getUnformattedText()).contains("Two")) {
                    this.player().inventory.currentItem = 1;
                } else if (Utils.Java.str(event.message.getUnformattedText()).contains("1") || Utils.Java.str(event.message.getUnformattedText()).contains("One")) {
                    this.player().inventory.currentItem = 0;
                }
            }

            if (autoDis.isToggled() && this.player().getHealth() < autoDisableHealth.getValue() * 2
                    && this.player().fallDistance < autoDisDistance.getValue() && (Utils.Java.str(event.message.getUnformattedText()).contains("Start")
                    || Utils.Java.str(event.message.getUnformattedText()).contains("Go") || Utils.Java.str(event.message.getUnformattedText()).contains("0"))) {
                this.disable();
            }
        }
    }

    public enum modes {
        ExperienceLevel, ChatMessage,
        DisplayName, PlayerInRange
    }

}