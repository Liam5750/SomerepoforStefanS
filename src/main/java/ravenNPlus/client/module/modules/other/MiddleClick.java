package ravenNPlus.client.module.modules.other;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemEnderPearl;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.awt.*;
import java.util.Arrays;
import java.awt.event.InputEvent;

import org.lwjgl.input.Mouse;

public class MiddleClick extends Module {

    public static ModeSetting mode;
    public static TickSetting showHelp;
    int prevSlot;
    public static boolean a;
    private Robot bot;
    private boolean hasClicked;
    private int pearlEvent;

    public MiddleClick() {
        super("MiddleClick", ModuleCategory.other, "Hover over a player and MiddleClick");
        this.addSetting(showHelp = new TickSetting("Show friend help in chat", true));
        this.addSetting(mode = new ModeSetting("On click", modes.ThrowPearl));
    }

    @Override
    public void onEnable() {
        try {
            this.bot = new Robot();
        } catch (AWTException var2) {
            this.disable();
        }

        hasClicked = false;
        pearlEvent = 4;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        if (!this.inGame()) return;

        if (pearlEvent < 4) {
            if (pearlEvent == 3) mc.thePlayer.inventory.currentItem = prevSlot;
            pearlEvent++;
        }

        if (Mouse.isButtonDown(2) && !hasClicked) {
            if (modes.ThrowPearl.equals(mode.getMode())) {
                for (int slot = 0; slot <= 8; slot++) {
                    ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
                    if (itemInSlot != null && itemInSlot.getItem() instanceof ItemEnderPearl) {
                        prevSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.inventory.currentItem = slot;
                        this.bot.mousePress(InputEvent.BUTTON3_MASK);
                        this.bot.mouseRelease(InputEvent.BUTTON3_MASK);
                        pearlEvent = 0;
                        hasClicked = true;
                        return;
                    }
                }
            } else if (modes.AddFriend.equals(mode.getMode())) {
                addFriend();
                if (showHelp.isToggled()) showHelpMessage();
            } else if (modes.RemoveFriend.equals(mode.getMode())) {
                removeFriend();
                if (showHelp.isToggled()) showHelpMessage();
            } else if (modes.Insult.equals(mode.getMode())) {
                insult();
            }
            hasClicked = true;
        } else if (!Mouse.isButtonDown(2) && hasClicked) {
            hasClicked = false;
        }
    }

    private void showHelpMessage() {
        if (showHelp.isToggled()) {
            Utils.Player.sendMessageToSelf("Run 'help friends' in CommandLine to find out how to add, remove, insult and view friends.");
        }
    }

    private void removeFriend() {
        Entity player = mc.objectMouseOver.entityHit;
        if (player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when removing them.");
        } else {
            if (Utils.FriendUtils.removeFriend(player)) {
                Utils.Player.sendMessageToSelf("Successfully removed " + player.getName() + " from friends list!");
                Utils.Player.sendMessageToSelf("You have " + Utils.FriendUtils.getFriendCount() + " Friends");
            } else {
                Utils.Player.sendMessageToSelf(player.getName() + " was not found in the friends list!");
            }
        }
    }

    private void addFriend() {
        Entity player = mc.objectMouseOver.entityHit;
        if (player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when adding them.");
        } else {
            Utils.FriendUtils.addFriend(player);
            Utils.Player.sendMessageToSelf("Successfully added " + player.getName() + " to friends list.");
            Utils.Player.sendMessageToSelf("You have " + Utils.FriendUtils.getFriendCount() + " Friends");
        }
    }

    private void insult() {
        Entity player = mc.objectMouseOver.entityHit;
        if (player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when insult them.");
        } else {
            String[] x = new String[]{"Haha", "Noob", "L", "U are bad", player.getName() + " you are bad", player.getName() + " L",
                    player.ignoreFrustumCheck ? player.getName() + "is a Noob" : player.getName() + "is a God"};

            mc.thePlayer.sendChatMessage(Arrays.stream(x).findAny().get());
        }
    }

    public enum modes {
        ThrowPearl,
        AddFriend,
        RemoveFriend,
        Insult
    }

}