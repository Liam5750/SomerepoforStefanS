package ravenNPlus.client.module.modules.combat;

import ravenNPlus.client.utils.InvUtils;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.DelayTimer;
import ravenNPlus.client.module.setting.impl.TickSetting;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSort extends Module {

    public static TickSetting AutoSetSword, AutoSetBlock, AutoSetFood;
    private final DelayTimer timer = new DelayTimer();
    static int swordSlot = 0, blockSlot = 1, foodSlot = 2;

    public AutoSort() {
        super("AutoSort", ModuleCategory.player, "Automatically Sort the best Items");
        this.addSetting(AutoSetSword = new TickSetting("AutoSet Sword", x));
        this.addSetting(AutoSetBlock = new TickSetting("AutoSet Blocks", x));
        this.addSetting(AutoSetFood = new TickSetting("AutoSet Food", o));
    }

    @SubscribeEvent
    public void r(PlayerInteractEvent event) {
        if (ravenNPlus.client.utils.Utils.Player.isPlayerInContainer()) return;
        if (!this.inGame()) return;

        if (!this.timer.hasPassed(200))
            return;

        if (AutoSetSword.isToggled()) {
            int slotForSword = AutoSort.getBestSword(AutoSort.getScoreForSword(InvUtils.getCurrentItem()));

            if (slotForSword == -1)
                return;

            InvUtils.swap(slotForSword, AutoSort.swordSlot);
        }

        if (AutoSetBlock.isToggled()) {
            int slotForBlock = AutoSort.getBestBlock(AutoSort.getScoreForBlock(InvUtils.getItemBySlot(blockSlot)));

            if (slotForBlock == -1)
                return;

            InvUtils.swap(slotForBlock, AutoSort.blockSlot);
        }

        if (AutoSetFood.isToggled()) {
            int slotForFood = AutoSort.getBestFood(AutoSort.getScoreForFood(InvUtils.getItemBySlot(foodSlot)));

            if (slotForFood == -1)
                return;

            InvUtils.swap(slotForFood, AutoSort.foodSlot);
        }

        this.timer.reset();
    }

    // --------------------------------------------------------

    public static int getBestBlock(double minimum) {
        for (int i = 0; i < 36; i++) {
            if (mc.thePlayer.inventory.currentItem == i)
                continue;

            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

            if (itemStack == null)
                continue;

            if (!(itemStack.getItem() instanceof ItemBlock))
                continue;

            if (minimum >= AutoSort.getScoreForBlock(itemStack))
                continue;

            return i;
        }

        return -1;
    }

    public static int getScoreForBlock(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemBlock))
            return 0;

        ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

        int result = 1;

        result += itemBlock.hashCode();

        return result;
    }

    // --------------------------------------------------------

    public static int getBestSword(double minimum) {
        for (int i = 0; i < 36; ++i) {
            if (mc.thePlayer.inventory.currentItem == i)
                continue;

            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

            if (itemStack == null)
                continue;

            if (!(itemStack.getItem() instanceof ItemSword))
                continue;

            if (minimum >= AutoSort.getScoreForSword(itemStack))
                continue;

            return i;
        }

        return -1;
    }

    public static double getScoreForSword(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemSword))
            return 0;

        ItemSword itemSword = (ItemSword) itemStack.getItem();

        double result = 1.0;

        result += itemSword.getDamageVsEntity();

        result += 1.25 * EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
        result += 0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);

        return result;
    }

    // --------------------------------------------------------

    public static int getBestFood(double minimum) {
        for (int i = 0; i < 36; ++i) {
            if (mc.thePlayer.inventory.currentItem == i)
                continue;

            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

            if (itemStack == null)
                continue;

            if (!(itemStack.getItem() instanceof ItemFood))
                continue;

            if (minimum >= AutoSort.getScoreForFood(itemStack))
                continue;

            return i;
        }

        return -1;
    }

    public static int getScoreForFood(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemFood))
            return 0;

        ItemFood itemFood = (ItemFood) itemStack.getItem();

        int result = 1;

        result += itemFood.hashCode();

        return result;
    }

    // --------------------------------------------------------

}