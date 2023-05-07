package ravenNPlus.client.module.modules.minigames.sumo;

import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.modules.combat.STap;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.modules.combat.AimAssist;
import ravenNPlus.client.module.modules.render.AntiShuffle;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.IOException;

public class SumoBot extends Module {

    private State state = State.HUB;
    private STap sTap;
    private SumoClicker leftClicker;
    private AimAssist aimAssist;
    private final CoolDown timer = new CoolDown(0);
    private final CoolDown rcTimer = new CoolDown(0);
    private final CoolDown slotTimer = new CoolDown(0);
    private final SliderSetting autoClickerTrigger;

    public SumoBot() {
        super("Sumo Bot", ModuleCategory.minigame, "A bot for Hypixel sumo");
        this.addSetting(autoClickerTrigger = new SliderSetting("Autoclicker Distance", 4.5, 1, 5, 0.1));
    }

    public void onEnable() {
        state = State.HUB;
        sTap = (STap) Client.moduleManager.getModuleByName("STap");
        leftClicker = (SumoClicker) Client.moduleManager.getModuleByName("Sumo Clicker");
        aimAssist = (AimAssist) Client.moduleManager.getModuleByName("AimAssist");
        mc.gameSettings.pauseOnLostFocus = false;
    }

    private void matchStart() {
        state = State.INGAME;
        sTap.setToggled(true);
        aimAssist.setToggled(true);
        this.player().inventory.currentItem = 4;
        slotTimer.setCooldown(2300);
        slotTimer.start();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
    }

    private void matchEnd() {
        timer.setCooldown(2300);
        timer.start();
        state = State.GAME_END;
        sTap.setToggled(false);
        leftClicker.setToggled(false);
        aimAssist.setToggled(false);
        this.player().inventory.currentItem = 4;
        KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
    }

    @SubscribeEvent
    public void event(TickEvent.RenderTickEvent e) {
        if (!this.inGame())
            return;

        if (slotTimer.firstFinish()) {
            this.player().inventory.currentItem = 4;
        }

        if (rcTimer.firstFinish() && state == State.GAME_END) {
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            rcTimer.setCooldown(1900);
            rcTimer.start();
        }

        if (state == State.QUEUE) {
            for (String s : Utils.Client.getPlayersFromScoreboard()) {
                if (s.contains("Opponent")) {
                    matchStart();
                }
            }
        } else if (state == State.GAME_END) {
            if (timer.hasFinished()) {
                state = State.QUEUE;
                rcTimer.setCooldown(1900);
                rcTimer.start();
                KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            }
        } else if (state == State.HUB) {
            if (timer.hasFinished()) {
                if (timer.firstFinish()) {
                    this.player().sendChatMessage("/play duels_sumo_duel");
                    state = State.QUEUE;
                }
                timer.setCooldown(1500);
                timer.start();
            }
        }
    }

    @SubscribeEvent
    public void event2(TickEvent.RenderTickEvent e) {
        if (state == State.INGAME) {
            if (leftClicker.isEnabled()) {
                if (Utils.Player.getClosestPlayer(autoClickerTrigger.getValue()) == null) {
                    leftClicker.disable();
                }
            } else {
                if (Utils.Player.getClosestPlayer(autoClickerTrigger.getValue()) != null) {
                    leftClicker.enable();
                }
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) throws IOException {
        if (AntiShuffle.getUnformattedTextForChat(e.message.getFormattedText()).contains("WINNER") || AntiShuffle.getUnformattedTextForChat(e.message.getFormattedText()).contains("DRAW")) {
            matchEnd();
        }
    }

    public void reQueue() {
        this.player().sendChatMessage("/hub");
        state = State.HUB;
    }

    public enum State {
        INGAME, HUB, QUEUE, GAME_END
    }

}