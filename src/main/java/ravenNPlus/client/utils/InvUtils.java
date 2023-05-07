package ravenNPlus.client.utils;

import net.minecraft.item.*;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class InvUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int findEmptySlot() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] == null)
                return i;
        }

        return mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.getCurrentItem() == null ? 0 : ((mc.thePlayer.inventory.currentItem < 8) ? 4 : -1));
    }

    // TODO: AutoPot refill always put potions on slot 1, bugs here?
    public static int findEmptySlot(int priority) {
        if (mc.thePlayer.inventory.mainInventory[priority] == null)
            return priority;

        return findEmptySlot();
    }

    public static void swapShift(int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
    }

    public static void swap(int from, int to) {
        if (from <= 8) {
            from = 36 + from;
        }

        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, from, to, 2, mc.thePlayer);
    }

    public static void clean(int slot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 0, mc.thePlayer);
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
    }

    public static void swapItem(int fromSlot, int toSlot, long delay) {
        if (Timer.hasTimeElapsed(delay, true)) {
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, fromSlot, toSlot, 2, mc.thePlayer);
        }
    }

    public static int armorSlotToNormalSlot(int armorSlot) {
        return 8 - armorSlot;
    }

    public static void block() {
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
    }

    public static ItemStack getCurrentItem() {
        return mc.thePlayer.getCurrentEquippedItem() == null ? new ItemStack(Blocks.air) : mc.thePlayer.getCurrentEquippedItem();
    }

    public static ItemStack getItemBySlot(int slot) {
        return mc.thePlayer.inventory.mainInventory[slot] == null ? new ItemStack(Blocks.air) : mc.thePlayer.inventory.mainInventory[slot];
    }

    public static List<ItemStack> getHotbarContent() {
        List<ItemStack> result = new ArrayList<>();
        result.addAll(Arrays.asList(mc.thePlayer.inventory.mainInventory).subList(0, 9));
        return result;
    }

    public static List<ItemStack> getAllInventoryContent() {
        List<ItemStack> result = new ArrayList<>();
        result.addAll(Arrays.asList(mc.thePlayer.inventory.mainInventory).subList(0, 35));
        for (int i = 0; i < 4; i++) {
            result.add(mc.thePlayer.inventory.armorItemInSlot(i));
        }

        return result;
    }

    public static List<ItemStack> getInventoryContent() {
        List<ItemStack> result = new ArrayList<>();
        result.addAll(Arrays.asList(mc.thePlayer.inventory.mainInventory).subList(9, 35));
        return result;
    }

    public static int getEmptySlotInHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] == null)
                return i;
        }

        return -1;
    }

    public static float getArmorScore(ItemStack itemStack) {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor))
            return -1;

        ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
        float score = 0;
        score += itemArmor.damageReduceAmount;
        if (EnchantmentHelper.getEnchantments(itemStack).size() <= 0) score -= 0.1;
        int protection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
        score += protection * 0.2;

        return score;
    }

    public static boolean hasWeapon() {
        if (mc.thePlayer.inventory.getCurrentItem() != null)
            return false;

        return (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemAxe) || (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword);
    }

    public static int pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;

    public static void getBestPickaxe() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {

                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (isBestPickaxe(is) && pickaxeSlot != i) {
                    if (!isBestWeapon(is))
                        if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                            swap(i, pickaxeSlot - 36);
                        } else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
                            swap(i, pickaxeSlot - 36);
                        }
                }
            }
        }
    }

    public static void getBestShovel() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestShovel(is) && shovelSlot != i) {
                    if (!isBestWeapon(is))
                        if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                            swap(i, shovelSlot - 36);
                        } else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
                            swap(i, shovelSlot - 36);
                        }
                }
            }
        }
    }

    public static void getBestAxe() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestAxe(is) && axeSlot != i) {
                    if (!isBestWeapon(is)) {
                        if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                            swap(i, axeSlot - 36);
                        } else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
                            swap(i, axeSlot - 36);
                        }
                    }
                }
            }
        }
    }

    public static boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe))
            return false;

        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }

            }
        }

        return true;
    }

    public static boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }

            }
        }

        return true;
    }

    public static boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool))
            return 0;
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool) item;
        float value = 1;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else
            return 1f;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;

        return value;
    }

    public static boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword))
                    return false;
            }
        }

        return stack.getItem() instanceof ItemSword;
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

    public static float getDamage(ItemStack stack) {
        float damage = 0;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;
            damage += tool.getMaxDamage();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            damage += sword.getDamageVsEntity();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;

        return damage;
    }

    public static void hotkeyToSlot(int slot) {
        if (!Utils.Player.isPlayerInGame())
            return;

        mc.thePlayer.inventory.currentItem = slot;
    }

    public static int getCurrentPlayerSlot() {
        return mc.thePlayer.inventory.currentItem;
    }

    public static void setCurrentPlayerSlot(int slot) {
        mc.thePlayer.inventory.currentItem = slot;
    }

    public static boolean isPlayerHoldingWeapon() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
        }
    }

    public static boolean isPlayerHoldingSword() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword;
        }
    }

    public static boolean isPlayerHoldingAxe() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemAxe;
        }
    }

    public static boolean isPlayerHoldingBlock() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemBlock;
        }
    }

    public static boolean isPlayerHoldingFood() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemFood;
        }
    }

    public static boolean isPlayerHoldingBow() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemBow;
        }
    }

    public static boolean isPlayerHolding(net.minecraft.item.Item item) {
        if (mc.thePlayer.getCurrentEquippedItem().getItem() == null)
            return false;

        return mc.thePlayer.getCurrentEquippedItem().getItem() == item;
    }

    public static int getMaxDamageSlot() {
        int index = -1;
        double damage = -1;

        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
            if (itemInSlot == null)
                continue;
            for (AttributeModifier att : itemInSlot.getAttributeModifiers().values()) {
                if (att.getAmount() > damage) {
                    damage = att.getAmount();
                    index = slot;
                }
            }
        }

        return index;
    }

    public static double getSlotDamage(int slot) {
        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
        if (itemInSlot == null)
            return -1;
        for (AttributeModifier ret : itemInSlot.getAttributeModifiers().values()) {
            return ret.getAmount();
        }

        return -1;
    }

    public static ArrayList<Integer> playerWearingArmor() {
        ArrayList<Integer> wearingArmor = new ArrayList<>();
        for (int armorPiece = 0; armorPiece < 4; armorPiece++) {
            if (mc.thePlayer.getCurrentArmor(armorPiece) != null) {
                if (armorPiece == 0) {
                    wearingArmor.add(3);
                } else if (armorPiece == 1) {
                    wearingArmor.add(2);
                } else if (armorPiece == 2) {
                    wearingArmor.add(1);
                } else {
                    wearingArmor.add(0);
                }
            }
        }

        return wearingArmor;
    }

    public static int getBlockAmountInCurrentStack(int slot) {
        if (mc.thePlayer.inventory.getStackInSlot(slot) == null) {
            return 0;
        } else {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
            if (itemStack.getItem() instanceof ItemBlock) {
                return itemStack.stackSize;
            } else {
                return 0;
            }
        }
    }

    /*
    public static boolean isTrashString(Item item) {
        return item.getUnlocalizedName().contains("Arrow") || item.getUnlocalizedName().contains("Gravel") || item.getUnlocalizedName().contains("Sand") || item.getUnlocalizedName().contains("Corrupted")
            || item.getUnlocalizedName().contains("Flower") || item.getUnlocalizedName().contains("Sugar") || item.getUnlocalizedName().contains("Flower Pot") || item.getUnlocalizedName().contains("Trapped")
            || item.getUnlocalizedName().contains("Charge") || item.getUnlocalizedName().contains("Bucket") || item.getUnlocalizedName().contains("Milk") || item.getUnlocalizedName().contains("Snow")
            || item.getUnlocalizedName().startsWith("Piston") || item.getUnlocalizedName().contains("Sapling") || item.getUnlocalizedName().contains("Seed") || item.getUnlocalizedName().contains("String")
            || item.getUnlocalizedName().contains("TNT") || item.getUnlocalizedName().contains("Flint") || item.getUnlocalizedName().contains("Torch") || item.getUnlocalizedName().contains("Web")
            || item.getUnlocalizedName().contains("Boat") || item.getUnlocalizedName().contains("Wheat") || item.getUnlocalizedName().contains("Dirt") || item.getUnlocalizedName().startsWith("Grass");
    }
     --------------------------------------------------------------------------  */

    static final DelayTimer celanInventoryTimer = new DelayTimer();

    public static void cleanInventoryTimerReset() {
        celanInventoryTimer.reset();
    }

    public static void cleanInventory(long delay1, long delay2, boolean onlyInv, boolean keepCorrupted) {
        if (onlyInv && !Utils.Player.isPlayerInInv()) return;

        if (celanInventoryTimer.hasPassed(delay2)) {
            for (int i = 9; i < mc.thePlayer.inventoryContainer.getInventory().size(); i++) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;

                ItemStack iS = mc.thePlayer.inventoryContainer.getInventory().get(i);

                if ((InvUtils.isTrashInstanceOf(iS.getItem())) || (InvUtils.isTrashName(iS.getItem(), keepCorrupted)))
                    if (Timer.hasTimeElapsed(delay1, true))
                        InvUtils.clean(i);
            }

            //cleanInventoryTimerReset();
        }
    }

    public static boolean isTrashInstanceOf(Item item) {
        return item instanceof ItemEgg || item instanceof ItemSnowball || item instanceof ItemFirework || item instanceof ItemFireworkCharge || item instanceof ItemFishingRod || item instanceof ItemPickaxe
                || item instanceof ItemSeeds || item instanceof ItemBucket || item instanceof ItemBucketMilk || item instanceof ItemAnvilBlock || item instanceof ItemBook || item instanceof ItemPiston
                || item instanceof ItemGlassBottle || item instanceof ItemArmorStand || item instanceof ItemBoat || item instanceof ItemHoe || item instanceof ItemLilyPad || item instanceof ItemLead
                || item instanceof ItemExpBottle || item instanceof ItemNameTag || item instanceof ItemWritableBook || item instanceof ItemSaddle || (item instanceof ItemFood && !(item instanceof ItemAppleGold));
    }

    public static boolean isTrashName(Item item, boolean keepCorruptedPearl) {
        String name = item.getRegistryName().toLowerCase();

        if (!keepCorruptedPearl)
            if (name.startsWith("corrupt"))
                return true;

        return isFlowerName(name, true) || name.endsWith("sand") || name.endsWith("gravel") || name.endsWith("wheat") || name.endsWith("seeds") || name.endsWith("sugar");
    }

    public static boolean isFlowerName(String name, boolean flowerPod) {
        if (flowerPod)
            return name.endsWith("pod");

        return name.endsWith("dandelion") || name.endsWith("poppy") || name.endsWith("orchid") || /*name.endsWith("allium") ||*/ name.endsWith("bluet")
                || name.endsWith("tulip") || name.endsWith("daisy") || name.endsWith("sunflower");
    }

    public static int getSwordSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemSword) {
                        ItemSword is = (ItemSword) item.getItem();
                        float damage = is.getDamageVsEntity();
                        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, item) * 1.26F +
                                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, item) * 0.01f;
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }

        return bestSword;
    }

    public static int getPickaxeSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemPickaxe) {
                        ItemPickaxe is = (ItemPickaxe) item.getItem();
                        float damage = is.getStrVsBlock(item, Block.getBlockById(4));
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }

        return bestSword;
    }

    public static int getAxeSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemAxe) {
                        ItemAxe is = (ItemAxe) item.getItem();
                        float damage = is.getStrVsBlock(item, Block.getBlockById(17));
                        if (damage > bestDamage) {
                            bestDamage = damage;
                            bestSword = i;
                        }
                    }
                }
            }
        }

        return bestSword;
    }

    public static int getShovelSlot() {
        if (mc.thePlayer == null) {
            return -1;
        }

        int bestSword = -1;
        float bestDamage = 1F;

        for (int i = 9; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack item = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item != null) {
                    if (item.getItem() instanceof ItemTool) {
                        ItemTool is = (ItemTool) item.getItem();
                        if (isShovel(is)) {
                            float damage = is.getStrVsBlock(item, Block.getBlockById(3));
                            if (damage > bestDamage) {
                                bestDamage = damage;
                                bestSword = i;
                            }
                        }
                    }
                }
            }
        }

        return bestSword;
    }

    public static boolean isShovel(Item is) {
        return Item.getItemById(256) == is || Item.getItemById(269) == is || Item.getItemById(273) == is || Item.getItemById(277) == is || Item.getItemById(284) == is;
    }

    public static float getItemDamage(ItemStack stack) {
        if (stack.getItem() instanceof ItemSword) {
            ItemSword sword = (ItemSword) stack.getItem();
            float sharpness = (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F;
            float fireAspect = (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.5F;
            return sword.getDamageVsEntity() + sharpness + fireAspect;
        } else {
            return 0.0F;
        }
    }

    public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {

                for (PotionEffect o : potion.getEffects(stack)) {
                    if (o.getPotionID() == Potion.poison.getId() || o.getPotionID() == Potion.harm.getId() ||
                            o.getPotionID() == Potion.moveSlowdown.getId() || o.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isBadPotion(Item stack) {
        if (stack instanceof ItemPotion) {
            if (ItemPotion.isSplash(stack.getMaxDamage())) {

                for (PotionEffect o : ((ItemPotion) stack).getEffects(1)) {
                    if (o.getPotionID() == Potion.poison.getId() || o.getPotionID() == Potion.harm.getId() ||
                            o.getPotionID() == Potion.moveSlowdown.getId() || o.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}