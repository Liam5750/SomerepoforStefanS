package ravenNPlus.client.module.modules.player;

import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.modules.combat.LeftClicker;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemShears;
import net.minecraft.block.BlockLiquid;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.concurrent.ThreadLocalRandom;
import org.lwjgl.input.Mouse;

public class AutoTool extends Module {

    private final TickSetting hotkeyBack;
    private Block previousBlock;
    private boolean isWaiting;
    public static DoubleSliderSetting mineDelay;
    public static int previousSlot;
    public static boolean justFinishedMining, mining;
    public static CoolDown delay;

    public AutoTool() {
        super("Auto Tool", ModuleCategory.player, "Automatically selects the best tool for breaking blocks");
        this.addSetting(hotkeyBack = new TickSetting("Hotkey back", true));
        this.addSetting(mineDelay = new DoubleSliderSetting("Max delay", 10, 50, 0, 2000, 1));
        delay = new CoolDown(0);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (!this.inGame() || mc.currentScreen != null)
            return;

        if (!Mouse.isButtonDown(0)) {
            if (mining)
                finishMining();
            if (isWaiting)
                isWaiting = false;
            return;
        }

        LeftClicker autoClicker = (LeftClicker) Client.moduleManager.getModuleByClazz(LeftClicker.class);
        if (autoClicker.isEnabled()) {
            if (!LeftClicker.breakBlocks.isToggled()) {
                return;
            }
        }

        BlockPos lookingAtBlock = mc.objectMouseOver.getBlockPos();
        if (lookingAtBlock != null) {

            Block stateBlock = mc.theWorld.getBlockState(lookingAtBlock).getBlock();
            if (stateBlock != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block) {

                if (mineDelay.getInputMax() > 0) {
                    if (previousBlock != null) {
                        if (previousBlock != stateBlock) {
                            previousBlock = stateBlock;
                            isWaiting = true;
                            delay.setCooldown((long) ThreadLocalRandom.current().nextDouble(mineDelay.getInputMin(), mineDelay.getInputMax() + 0.01));
                            delay.start();
                        } else {
                            if (isWaiting && delay.hasFinished()) {
                                isWaiting = false;
                                previousSlot = InvUtils.getCurrentPlayerSlot();
                                mining = true;
                                hotkeyToFastest();
                            }
                        }
                    } else {
                        previousBlock = stateBlock;
                        isWaiting = false;
                    }
                    return;
                }

                if (!mining) {
                    previousSlot = InvUtils.getCurrentPlayerSlot();
                    mining = true;
                }

                hotkeyToFastest();
            }
        }
    }

    public void finishMining() {
        if (hotkeyBack.isToggled()) {
            InvUtils.hotkeyToSlot(previousSlot);
        }
        justFinishedMining = false;
        mining = false;
    }

    private void hotkeyToFastest() {
        int index = -1;
        double speed = 1;

        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = this.player().inventory.getStackInSlot(slot);
            if (itemInSlot != null) {
                if (itemInSlot.getItem() instanceof ItemTool || itemInSlot.getItem() instanceof ItemShears) {
                    BlockPos p = mc.objectMouseOver.getBlockPos();
                    Block bl = mc.theWorld.getBlockState(p).getBlock();

                    if (itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                        speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                        index = slot;
                    }
                }
            }
        }

        if (index == -1 || speed <= 1.1 || speed == 0) {
            // ...
        } else {
            InvUtils.hotkeyToSlot(index);
        }
    }

}