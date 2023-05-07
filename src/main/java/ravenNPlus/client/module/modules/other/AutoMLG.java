package ravenNPlus.client.module.modules.other;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.utils.DimensionHelper;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import net.minecraft.util.MovingObjectPosition.MovingObjectType;
public class AutoMLG extends Module {

   public static DescriptionSetting moduleDesc;
   private boolean handling;

   public AutoMLG() {
      super("AutoMLG", ModuleCategory.other, "Automatically Water MLG (Disabled in nether)");
      this.addSetting(moduleDesc = new DescriptionSetting("Credits: aycy"));
   }

   @Override
   public boolean canBeEnabled() {
      return !DimensionHelper.isPlayerInNether();
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent ev) {
      if (ev.phase != Phase.END && this.inGame() && !mc.isGamePaused()) {
         if (DimensionHelper.isPlayerInNether()) this.disable();

         if (this.inPosition() && this.holdWaterBucket())
            this.handling = true;

         if (this.handling) {
            this.mlg();
            if (this.player().onGround || this.player().motionY > 0.0D) {
               this.reset();
            }
         }
      }
   }

   private boolean inPosition() {
      if (this.player().motionY < -0.6D && !this.player().onGround && !this.player().capabilities.isFlying && !this.player().capabilities.isCreativeMode && !this.handling) {
         BlockPos playerPos = this.player().getPosition();

         for (int i = 1; i < 3; ++i) {
            BlockPos blockPos = playerPos.down(i);
            Block block = mc.theWorld.getBlockState(blockPos).getBlock();
            if (block.isBlockSolid(mc.theWorld, blockPos, EnumFacing.UP)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean holdWaterBucket() {
      if (this.containsItem(this.player().getHeldItem(), Items.water_bucket)) {
         return true;
      } else {
         for (int i = 0; i < InventoryPlayer.getHotbarSize(); ++i) {
            if (this.containsItem(this.player().inventory.mainInventory[i], Items.water_bucket)) {
               this.player().inventory.currentItem = i;
               return true;
            }
         }

         return false;
      }
   }

   private void mlg() {
      ItemStack heldItem = this.player().getHeldItem();
      if (this.containsItem(heldItem, Items.water_bucket) && this.player().rotationPitch >= 70.0F) {
         MovingObjectPosition object = mc.objectMouseOver;
         if (object.typeOfHit == MovingObjectType.BLOCK && object.sideHit == EnumFacing.UP) {
            mc.playerController.sendUseItem(this.player(), mc.theWorld, heldItem);
         }
      }
   }

   private void reset() {
      ItemStack heldItem = this.player().getHeldItem();
      if (this.containsItem(heldItem, Items.bucket))
         mc.playerController.sendUseItem(this.player(), mc.theWorld, heldItem);

      this.handling = false;
   }

   private boolean containsItem(ItemStack itemStack, Item item) {
      return itemStack != null && itemStack.getItem() == item;
   }

}