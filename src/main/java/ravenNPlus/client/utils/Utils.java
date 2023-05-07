package ravenNPlus.client.utils;

import com.sun.javafx.geom.Vec3d;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.modules.combat.LeftClicker;
import ravenNPlus.client.module.modules.combat.NewAntiBot;
import ravenNPlus.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.*;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Utils {

   private static final Random rand = new Random();
   public static final Minecraft mc = Minecraft.getMinecraft();
   public static final String md = "Mode: ";
   public static final String by = "Bypass: ";
   public static final String th = "Theme: ";
   public static final String te = "Test: ";
   public static final String so = "Sort: ";
   public static final String co = "Color: ";
   public static float prevSwingProgress;
   public static float swingProgress;
   private static int swingProgressInt;
   private static boolean isSwingInProgress;

    public static class Player {
      static boolean isPlayerInRANGE = false;

      public static void dropAllItems() {
         Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
      }

      public static void startDestroyBlock(int x, int y, int z, EnumFacing facing) {
         Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(x, y, z), facing));
      }

      public static Vec3d interpolateEntity(Entity entity, float time) {
         return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY +
                 (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
      }

      public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
         return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
      }

      public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
         return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
      }

      public static Vec3d getInterpolatedRenderPos(Entity player, float renderPartialTicks) {
         Vec3d b = new Vec3d(Client.getRenderPosX(), Client.getRenderPosX(), Client.getRenderPosZ());
         ;
         getInterpolatedAmount(player, renderPartialTicks).add(b);
         return b;
      }

      public static double getProjectileGravity(Item item) {
         //  if (item instanceof ItemSnowball)
         //      return 0.5;

         if (item instanceof ItemBow)
            return 0.05;

         if (item instanceof ItemPotion)
            return 0.4;

         if (item instanceof ItemFishingRod)
            return 0.15;

         return 0.03;
      }

      private boolean isThrowable(Item item) {
         return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl || item instanceof ItemPotion || item instanceof ItemFishingRod;
      }

      public static boolean isOnGround() {
         return mc.thePlayer.onGround;
      }

      public static void fakeJump(boolean playerMoveInputJump) {
         if (playerMoveInputJump)
            mc.thePlayer.movementInput.jump = true;

         mc.thePlayer.isAirBorne = true;
         mc.thePlayer.addStat(StatList.jumpStat, 1);
      }

      public float getSwingProgress(float partialTickTime) {
         float currentProgress = swingProgress - prevSwingProgress;

         if (!isSwingInProgress) {
            return mc.thePlayer.getSwingProgress(partialTickTime);
         }

         if (currentProgress < 0.0F) {
            ++currentProgress;
         }

         return prevSwingProgress + currentProgress * partialTickTime;
      }

      public static int getArmSwingAnimationEnd(EntityPlayerSP player) {
         return player.isPotionActive(Potion.digSpeed) ? 5 - player.getActivePotionEffect(Potion.digSpeed).getAmplifier() :
                 (player.isPotionActive(Potion.digSlowdown) ? 8 + player.getActivePotionEffect(Potion.digSlowdown).getAmplifier() * 2 : 6);
      }

      public static void isPlayerInRange(double dis) {
         getClosestPlayer(dis);
      }

      public static EntityPlayer getClosestPlayer(double dis) {
         if (mc.theWorld == null) return null;

         Iterator entities;
         entities = mc.theWorld.loadedEntityList.iterator();
         EntityPlayer cplayer = null;

         while (entities.hasNext()) {
            Entity en = (Entity) entities.next();
            if (en instanceof EntityPlayer && en != mc.thePlayer) {
               EntityPlayer pl = (EntityPlayer) en;
               if (mc.thePlayer.getDistanceToEntity(pl) < dis && !NewAntiBot.isBot(pl)) {
                  dis = mc.thePlayer.getDistanceToEntity(pl);
                  cplayer = pl;
               }
            }
         }

         return cplayer;
      }

      public static void place(BlockPos blockPos, EnumFacing facing, boolean mustHoldBlock) {
         if (facing == EnumFacing.UP)
            blockPos = blockPos.add(0, -1, 0);
         else if (facing == EnumFacing.NORTH)
            blockPos = blockPos.add(0, 0, 1);
         else if (facing == EnumFacing.EAST)
            blockPos = blockPos.add(-1, 0, 0);
         else if (facing == EnumFacing.SOUTH)
            blockPos = blockPos.add(0, 0, -1);
         else if (facing == EnumFacing.WEST)
            blockPos = blockPos.add(1, 0, 0);

         EntityPlayerSP player = mc.thePlayer;

         if (mustHoldBlock)
            if (!InvUtils.isPlayerHoldingBlock()) return;

         mc.playerController.onPlayerRightClick(player, mc.theWorld, player.getHeldItem(), blockPos, facing, new Vec3(0.5, 0.5, 0.5));
         double x = blockPos.getX() + 0.25 - player.posX;
         double z = blockPos.getZ() + 0.25 - player.posZ;
         double y = blockPos.getY() + 0.25 - player.posY;
         double distance = MathHelper.sqrt_double(x * x + z * z);
         float yaw = (float) (Math.atan2(z, x) * 180 / Math.PI - 90);
         float pitch = (float) -(Math.atan2(y, distance) * 180 / Math.PI);
         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(player.posX, player.posY, player.posZ, yaw, pitch, player.onGround));
      }

      public static void sendMessageToSelf(String message) {
         if (isPlayerInGame()) {
            String m = Utils.Client.reformat("&7[&d" + ravenNPlus.client.main.Client.name + "&7]&r " + message);
            mc.thePlayer.addChatMessage(new ChatComponentText(m));
         }
      }

      public static void sendClearMessageToSelf() {
         if (isPlayerInGame()) {
            sendMessageToSelf(null);
         }
      }

      public static boolean isPlayerInWater() {
         if (!isPlayerInGame()) return false;
         return mc.thePlayer.isInWater();
      }

      public static boolean isPlayerInGame() {
         return mc.thePlayer != null && mc.theWorld != null;
      }

      public static boolean isPlayerInContainer() {
         return mc.currentScreen instanceof GuiContainer;
      }

      public static boolean isPlayerInInv() {
         return mc.currentScreen instanceof GuiInventory;
      }

      public static boolean isPlayerInChat() {
         return mc.currentScreen instanceof GuiChat;
      }

      public static boolean isPlayerInIngameMenu() {
         return mc.currentScreen instanceof GuiIngameMenu;
      }

      public static boolean isPlayerInGui(GuiScreen guiScreen) {
         if (guiScreen == null) return false;

         return mc.currentScreen == guiScreen;
      }

      public static boolean isMoving() {
         return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
      }

      public static boolean isMovingForward() {
         return mc.thePlayer.moveForward != 0.0F || mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindForward.isKeyDown();
      }

      public static void aim(Entity entity, float pitchOffset, boolean packet, boolean silent) {
         if (entity != null) {
            float[] t = getTargetRotations(entity);
            if (t != null) {
               float yaw = t[0];
               float pitch = t[1] + 4.0F + pitchOffset;
               if (packet) {
                  mc.getNetHandler().addToSendQueue(new C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround));
               } else if (!silent) {
                  mc.thePlayer.rotationYaw = yaw;
                  mc.thePlayer.rotationPitch = pitch;
               } else {
                  mc.thePlayer.rotationYawHead = yaw;
               }
            }
         }
      }

      public static double fovFromEntity(Entity en) {
         return ((double) (mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
      }

      public static float fovToEntity(Entity en) {
         double x = en.posX - mc.thePlayer.posX;
         double z = en.posZ - mc.thePlayer.posZ;
         double yaw = Math.atan2(x, z) * 57.2957795D;
         return (float) (yaw * -1.0D);
      }

      public static boolean fov(Entity entity, float fov) {
         fov = (float) ((double) fov * 0.5D);
         double v = ((double) (mc.thePlayer.rotationYaw - fovToEntity(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
         return v > 0.0D && v < (double) fov || (double) (-fov) < v && v < 0.0D;
      }

      public static boolean fov(Entity entity, float fov, boolean draw) {
         fov = (float) ((double) fov * 0.5D);
         double v = ((double) (mc.thePlayer.rotationYaw - fovToEntity(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;

         if (draw) {
            ScaledResolution x = new ScaledResolution(mc);
            float width = x.getScaledWidth() / 2;
            float height = x.getScaledHeight() / 2;

            RenderUtils.drawCircle(width + fov, height + fov, 10, 70, 360);
         }

         return v > 0.0D && v < (double) fov || (double) (-fov) < v && v < 0.0D;
      }

      public static double getPlayerBPS(Entity en, int d) {
         double x = en.posX - en.prevPosX;
         double z = en.posZ - en.prevPosZ;
         double sp = Math.sqrt(x * x + z * z) * 20.0D;
         return Java.round(sp, d);
      }

      public static boolean playerOverAir(double distance) {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY - distance;
         double z = mc.thePlayer.posZ;
         BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
         return mc.theWorld.isAirBlock(p);
      }

      public static boolean playerUnderBlock() {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY + 2.0D;
         double z = mc.thePlayer.posZ;
         BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
         return mc.theWorld.isBlockFullCube(p) || mc.theWorld.isBlockNormalCube(p, false);
      }

      public static boolean playerOnBlock() {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY - 1.0D;
         double z = mc.thePlayer.posZ;
         BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
         return mc.theWorld.isBlockFullCube(p) || mc.theWorld.isBlockNormalCube(p, false);
      }

      public static boolean tryingToCombo() {
         return Mouse.isButtonDown(0) && Mouse.isButtonDown(1);
      }

      public static float[] getTargetRotations(Entity q) {
         if (q == null) {
            return null;
         } else {
            double diffX = q.posX - mc.thePlayer.posX;
            double diffY;
            if (q instanceof EntityLivingBase) {
               EntityLivingBase en = (EntityLivingBase) q;
               diffY = en.posY + (double) en.getEyeHeight() * 0.9D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            } else {
               diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            }

            double diffZ = q.posZ - mc.thePlayer.posZ;
            double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
            return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
                    mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
         }
      }

      public static void fixMovementSpeed(double speed, boolean enable) {
         if (!enable || isMoving()) {
            mc.thePlayer.motionX = -Math.sin(correctRotations()) * speed;
            mc.thePlayer.motionZ = Math.cos(correctRotations()) * speed;
         }
      }

      public static void bop(double speed) {
         double forward = mc.thePlayer.movementInput.moveForward;
         double strafe = mc.thePlayer.movementInput.moveStrafe;
         float yaw = mc.thePlayer.rotationYaw;
         if (forward == 0.0D && strafe == 0.0D) {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
         } else {
            if (forward != 0.0D) {
               if (strafe > 0.0D) {
                  yaw += (float) (forward > 0.0D ? -45 : 45);
               } else if (strafe < 0.0D) {
                  yaw += (float) (forward > 0.0D ? 45 : -45);
               }

               strafe = 0.0D;
               if (forward > 0.0D) {
                  forward = 1.0D;
               } else if (forward < 0.0D) {
                  forward = -1.0D;
               }
            }

            double rad = Math.toRadians(yaw + 90.0F);
            double sin = Math.sin(rad);
            double cos = Math.cos(rad);
            mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
         }
      }

      public static float correctRotations() {
         float yw = mc.thePlayer.rotationYaw;
         if (mc.thePlayer.moveForward < 0.0F) {
            yw += 180.0F;
         }

         float f;
         if (mc.thePlayer.moveForward < 0.0F) {
            f = -0.5F;
         } else if (mc.thePlayer.moveForward > 0.0F) {
            f = 0.5F;
         } else {
            f = 1.0F;
         }

         if (mc.thePlayer.moveStrafing > 0.0F) {
            yw -= 90.0F * f;
         }
         if (mc.thePlayer.moveStrafing < 0.0F) {
            yw += 90.0F * f;
         }

         yw *= 0.017453292F;
         return yw;
      }

      public static double pythagorasMovement() {
         return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
      }

      public static boolean isPlayerSneaking() {
         return mc.thePlayer.isSneaking();
      }

      public static boolean canPlaceBlock(BlockPos pos) {
         Block block = mc.theWorld.getBlockState(pos).getBlock();
         return !mc.theWorld.isAirBlock(pos) && !(block instanceof BlockLiquid);
      }

      public static void swing() {
         EntityPlayerSP p = mc.thePlayer;
         int armSwingEnd = p.isPotionActive(Potion.digSpeed) ? 6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier()) :
                 (p.isPotionActive(Potion.digSlowdown) ? 6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
         if (!p.isSwingInProgress || p.swingProgressInt >= armSwingEnd / 2 || p.swingProgressInt < 0) {
            p.swingProgressInt = -1;
            p.isSwingInProgress = true;
         }
      }

      public static void legitSwing() {
         EntityPlayerSP p = mc.thePlayer;
         int armSwingEnd = p.isPotionActive(Potion.digSpeed) ? 6 - (1 + p.getActivePotionEffect(Potion.digSpeed).getAmplifier()) :
                 (p.isPotionActive(Potion.digSlowdown) ? 6 + (1 + p.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
         if (!p.isSwingInProgress || p.swingProgressInt >= armSwingEnd / 2 || p.swingProgressInt < 0) {
            p.swingProgressInt = -1;
            p.isSwingInProgress = true;
         }
         mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
      }

      public static void FastSwing(double digSpeed) {
         EntityPlayerSP p = mc.thePlayer;
         if (!p.isSwingInProgress || p.swingProgressInt <= digSpeed / 2 || p.swingProgressInt < 0) {
            p.swingProgressInt = -1;
            p.isSwingInProgress = true;
         }
      }

      public static void SlowSwing(double digSlowdown) {
         EntityPlayerSP p = mc.thePlayer;
         if (!p.isSwingInProgress || p.swingProgressInt >= digSlowdown * 2 || p.swingProgressInt < 0) {
            p.swingProgressInt = -1;
            p.isSwingInProgress = true;
         }
      }

      public static void jump(int height) {
         mc.thePlayer.motionY += height;
         mc.thePlayer.jump();
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

      public static boolean isInLiquid() {
         return mc.thePlayer.isInWater() || mc.thePlayer.isInLava();
      }

   }

   public static class Client {

      public static void setMouseButtonState(int mouseButton, boolean held) {
         MouseEvent m = new MouseEvent();

         ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, m, mouseButton, "button");
         ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, m, held, "buttonstate");
         MinecraftForge.EVENT_BUS.post(m);

         ByteBuffer buttons = ObfuscationReflectionHelper.getPrivateValue(Mouse.class, null, "buttons");
         buttons.put(mouseButton, (byte) (held ? 1 : 0));
         ObfuscationReflectionHelper.setPrivateValue(Mouse.class, null, buttons, "buttons");
      }

      public static List<NetworkPlayerInfo> getPlayers() {
         List<NetworkPlayerInfo> e = new ArrayList<>();
         List<NetworkPlayerInfo> a = new ArrayList<>();
         try {
            e.addAll(mc.getNetHandler().getPlayerInfoMap());
         } catch (NullPointerException r) {
            return e;
         }

         for (NetworkPlayerInfo s : e) {
            if (!a.contains(s)) {
               a.add(s);
            }
         }

         return a;
      }

      static int howMuchPlayersIngameInt = 0;
      public static int howMuchPlayersIngame() {
         for (Entity en : mc.theWorld.getLoadedEntityList())
            if (en instanceof EntityPlayer && !NewAntiBot.isBot(en))
               howMuchPlayersIngameInt++;

         return howMuchPlayersIngameInt;
      }

      public static boolean othersExist() {
         for (Entity wut : mc.theWorld.getLoadedEntityList())
            if (wut instanceof EntityPlayer) return true;

         return false;
      }

      public static double ranModuleVal(DoubleSliderSetting a, Random r) {
         return a.getInputMin() == a.getInputMax() ? a.getInputMin() : a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin());
      }

      public static boolean isHyp() {
          return isServerIP("hypixel.net");
      }

      public static boolean isServerIP(String ip) {
         if (!Player.isPlayerInGame() || mc.isSingleplayer()) return false;

         try {
            return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(ip);
         } catch (Exception ee) {
            ee.printStackTrace();
            return false;
         }
      }

      public static void zoom(float value) {
         try {
            ObfuscationReflectionHelper.setPrivateValue(
                    EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, value, 15);
            ObfuscationReflectionHelper.setPrivateValue(
                    EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, value, 16);
         } catch (final Exception ex) {
            ex.printStackTrace();
         }
      }

      public static net.minecraft.client.network.NetworkPlayerInfo getNetworkPlayerInfo(String username) {
         return ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, Minecraft.getMinecraft().getNetHandler().getPlayerInfo(username), "getLocationSkin", "func_110311_f");
      }

      public static float timer_default = 1.0F;
      public static float timer_best = 2.0F;
      public static float timer_max = 5.0F;

      public static void resetTimer() {
         try {
            getTimer().timerSpeed = timer_default;
         } catch (NullPointerException ignored) {
         }
      }

      public static net.minecraft.util.Timer getTimer() {
         return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "timer", "field_71428_T");
      }

       public static EntityPlayerSP getPlayer() {
           return ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "thePlayer", "field_70475_c");
       }

      public static double getRenderPosX() {
          return ReflectionHelper.getPrivateValue(RenderManager.class, mc.getRenderManager(), "renderPosX");
      }

      public static double getRenderPosY() {
          return ReflectionHelper.getPrivateValue(RenderManager.class, mc.getRenderManager(), "renderPosY");
      }

      public static double getRenderPosZ() {
          return ReflectionHelper.getPrivateValue(RenderManager.class, mc.getRenderManager(), "renderPosZ");
      }


      private static int[][][] getMatrix() {
         int[][][] matrix = new int[0][][];
         if (matrix != null) {
            matrix = ObfuscationReflectionHelper.getPrivateValue(EntityMinecart.class, null, "field_70500_g");
         }
         return matrix;
      }

      public static boolean autoClickerClicking() {
         Module autoClicker = ravenNPlus.client.main.Client.moduleManager.getModuleByClazz(LeftClicker.class);
         if (autoClicker != null && autoClicker.isEnabled()) {
            return autoClicker.isEnabled() && Mouse.isButtonDown(0);
         } //else return mouseManager.getLeftClickCounter() > 1 && System.currentTimeMillis() - mouseManager.leftClickTimer < 300L;
         return false;
      }

      public static int rainbowDraw(long speed, long... delay) {
         long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
         return Color.getHSBColor((float) (time % (15000L / speed)) / (15000.0F / (float) speed), 1.0F, 1.0F).getRGB();
      }

      public static int astolfoColorsDraw(int yOffset, int yTotal, float speed) {
         float hue = (float) (System.currentTimeMillis() % (int) speed) + ((yTotal - yOffset) * 9);
         while (hue > speed) {
            hue -= speed;
         }
         hue /= speed;
         if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
         }
         hue += 0.5F;
         return Color.HSBtoRGB(hue, 0.5f, 1F);
      }

      public static int astolfoColorsDraw(int yOffset, int yTotal) {
         return astolfoColorsDraw(yOffset, yTotal, 2900F);
      }

      public static boolean openWebpage(String url) {
         try {
            URL linkURL;
            linkURL = new URL(url);

            return openWebpage(linkURL.toURI());
         } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
         }

         return false;
      }

      public static boolean openWebpage(URL url) {
         try {
            return openWebpage(url.toURI());
         } catch (URISyntaxException e) {
            e.printStackTrace();
         }

         return false;
      }

      public static boolean openWebpage(URI uri) {
         Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
         if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
               desktop.browse(uri);
               return true;
            } catch (Exception e) {
               e.printStackTrace();
            }
         }

         return false;
      }

      public static boolean copyToClipboard(String copyMessage) {
         try {
            StringSelection selection = new StringSelection(copyMessage);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            return true;
         } catch (Exception fuck) {
            fuck.printStackTrace();
            return false;
         }
      }

      public static boolean currentScreenMinecraft() {
         return mc.currentScreen == null;
      }

      public static List<String> getPlayersFromScoreboard() {
         List<String> lines = new ArrayList<>();
         if (mc.theWorld == null) {
            return lines;
         } else {
            Scoreboard scoreboard = mc.theWorld.getScoreboard();
            if (scoreboard != null) {
               ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
               if (objective != null) {
                  Collection<Score> scores = scoreboard.getSortedScores(objective);
                  List<Score> list = new ArrayList<>();
                  Iterator<Score> var5 = scores.iterator();

                  Score score;
                  while (var5.hasNext()) {
                     score = var5.next();
                     if (score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
                        list.add(score);
                     }
                  }

                  if (list.size() > 15) {
                     scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
                  } else {
                     scores = list;
                  }

                  var5 = scores.iterator();

                  while (var5.hasNext()) {
                     score = var5.next();
                     ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                     lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
                  }

               }
            }

            return lines;
         }
      }

      public static String reformat(String txt) {
         return txt.replace("&", "§");
      }
   }

   public static class Java {

      public static int getValue(JsonObject type, String member) {
         try {
            return type.get(member).getAsInt();
         } catch (NullPointerException er) {
            return 0;
         }
      }

      public static long getSystemTime() {
         return Sys.getTime() * 1000L / Sys.getTimerResolution();
      }

      public static Random rand() {
         return rand;
      }

      public static double round(double n, int d) {
         if (d == 0) {
            return (double) Math.round(n);
         } else {
            double p = Math.pow(10.0D, d);
            return (double) Math.round(n * p) / p;
         }
      }

      public static String str(String s) {
         char[] n = StringUtils.stripControlCodes(s).toCharArray();
         StringBuilder v = new StringBuilder();

         for (char c : n) {
            if (c < 127 && c > 20) {
               v.append(c);
            }
         }

         return v.toString();
      }

      public static String capitalizeWord(String s) {
         return s.substring(0, 1).toUpperCase() + s.substring(1);
      }

       public static String getOnlyTime() {
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH : mm : ss");
           LocalDateTime now = LocalDateTime.now();
           return dtf.format(now);
       }

       public static String getOnlyDate() {
           DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
           LocalDateTime now = LocalDateTime.now();
           return dtf.format(now);
       }

      public static String getDate() {
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
         LocalDateTime now = LocalDateTime.now();
         return dtf.format(now);
      }

      public static String joinStringList(String[] wtf, String okwaht) {
         if (wtf == null)
            return "Error: Text cant be empty";
         if (wtf.length <= 1)
            return "Error: Text length to small";

         StringBuilder finalString = new StringBuilder(wtf[0]);

         for (int i = 1; i < wtf.length; i++) {
            finalString.append(okwaht).append(wtf[i]);
         }

         return finalString.toString();
      }

      public static ArrayList<String> toArrayList(String[] fakeList) {
         return new ArrayList<>(Arrays.asList(fakeList));
      }

      public static List<String> StringListToList(String[] whytho) {
         List<String> l = new ArrayList<>();
         Collections.addAll(l, whytho);
         return l;
      }

      public static JsonObject getStringAsJson(String text) {
         return new JsonParser().parse(text).getAsJsonObject();
      }

      public static String randomChoice(String[] strings) {
         return strings[rand.nextInt(strings.length)];
      }

      public static int randomInt(double inputMin, double v) {
         return (int) (Math.random() * (v - inputMin) + inputMin);
      }

      public Object getValue() {
         return getValue();
      }

   }

   public static class URLS {

      public static final String base_url = "https://api.paste.ee/v1/pastes/";
      public static final String base_paste = "{\"description\":" + ravenNPlus.client.main.Client.name + "" +
              "Config\",\"expiration\":\"never\",\"sections\":[{\"name\":\"TitleGoesHere\",\"syntax\":\"text\",\"contents\":\"BodyGoesHere\"}]}";
      public static String hypixelApiKey = "";
      public static String pasteApiKey = "";

      public static boolean isHypixelKeyValid(String ak) {
         String c = getTextFromURL("https://api.hypixel.net/key?key=" + ak);
         return !c.isEmpty() && !c.contains("Invalid");
      }

      public static String getTextFromURL(String _url) {
         String r = "";
         HttpURLConnection con = null;

         try {
            URL url = new URL(_url);
            con = (HttpURLConnection) url.openConnection();
            r = getTextFromConnection(con);
         } catch (IOException ignored) {
         } finally {
            if (con != null) {
               con.disconnect();
            }

         }

         return r;
      }

      private static String getTextFromConnection(HttpURLConnection connection) {
         if (connection != null) {
            try {
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

               String result;
               try {
                  StringBuilder stringBuilder = new StringBuilder();

                  String input;
                  while ((input = bufferedReader.readLine()) != null) {
                     stringBuilder.append(input);
                  }

                  String res = stringBuilder.toString();
                  connection.disconnect();

                  result = res;
               } finally {
                  bufferedReader.close();
               }

               return result;
            } catch (Exception ignored) {
            }
         }

         return "";
      }

      public static boolean isLink(String string) {
         return string.startsWith("http") && string.contains(".") && string.contains("://");
      }

   }

   public static class Profiles {

      public static String getUUIDFromName(String n) {
         String u = "";
         String r = URLS.getTextFromURL("https://api.mojang.com/users/profiles/minecraft/" + n);
         if (!r.isEmpty()) {
            try {
               u = r.split("d\":\"")[1].split("\"")[0];
            } catch (ArrayIndexOutOfBoundsException var4) {
            }
         }

         return u;
      }

      public static int[] getHypixelStats(String UUID, DuelsStatsMode dm) {
         int[] s = new int[]{0, 0, 0};
         String u = UUID;

         String c = URLS.getTextFromURL("https://api.hypixel.net/player?key=" + URLS.hypixelApiKey + "&uuid=" + u);
         if (c.isEmpty()) {
            return null;
         } else if (c.equals("{\"success\":true,\"player\":null}")) {
            s[0] = -1;
            return s;
         } else {
            JsonObject d;
            try {
               JsonObject pr = parseJson(c).getAsJsonObject("player");
               d = pr.getAsJsonObject("stats").getAsJsonObject("Duels");
            } catch (NullPointerException var8) {
               return s;
            }

            switch (dm) {
               case OVERALL:
                  s[0] = getValueAsInt(d, "wins");
                  s[1] = getValueAsInt(d, "losses");
                  s[2] = getValueAsInt(d, "current_winstreak");
                  break;
               case BRIDGE:
                  s[0] = getValueAsInt(d, "bridge_duel_wins");
                  s[1] = getValueAsInt(d, "bridge_duel_losses");
                  s[2] = getValueAsInt(d, "current_winstreak_mode_bridge_duel");
                  break;
               case UHC:
                  s[0] = getValueAsInt(d, "uhc_duel_wins");
                  s[1] = getValueAsInt(d, "uhc_duel_losses");
                  s[2] = getValueAsInt(d, "current_winstreak_mode_uhc_duel");
                  break;
               case SKYWARS:
                  s[0] = getValueAsInt(d, "sw_duel_wins");
                  s[1] = getValueAsInt(d, "sw_duel_losses");
                  s[2] = getValueAsInt(d, "current_winstreak_mode_sw_duel");
                  break;
               case CLASSIC:
                  s[0] = getValueAsInt(d, "classic_duel_wins");
                  s[1] = getValueAsInt(d, "classic_duel_losses");
                  s[2] = getValueAsInt(d, "current_winstreak_mode_classic_duel");
                  break;
               case SUMO:
                  s[0] = getValueAsInt(d, "sumo_duel_wins");
                  s[1] = getValueAsInt(d, "sumo_duel_losses");
                  s[2] = getValueAsInt(d, "current_winstreak_mode_sumo_duel");
                  break;
               case OP:
                  s[0] = getValueAsInt(d, "op_duel_wins");
                  s[1] = getValueAsInt(d, "op_duel_losses");
                  s[2] = getValueAsInt(d, "current_winstreak_mode_op_duel");
            }

            return s;
         }
      }

      public static JsonObject parseJson(String json) {
         return (new JsonParser()).parse(json).getAsJsonObject();
      }

      public static int getValueAsInt(JsonObject jsonObject, String key) {
         try {
            return jsonObject.get(key).getAsInt();
         } catch (NullPointerException var3) {
            return 0;
         }
      }

      public enum DuelsStatsMode {
         OVERALL, BRIDGE,
         UHC, SKYWARS,
         CLASSIC, SUMO, OP
      }
   }

   public static class HUD {

      public static FontRenderer fontRender = Minecraft.getMinecraft().fontRendererObj;
      public static final int rc = -1089466352;
      private static final double p2 = 6.283185307179586D;
      private static final Minecraft mc = Minecraft.getMinecraft();
      public static boolean ring_c = false;

      public static void renderBlockOutlines(BlockPos bp, int color, boolean shade) {
         if (bp != null) {
            double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
            double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
            double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            GL11.glColor4d(r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
            if (shade) {
               dbb(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), r, g, b);
            }

            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
         }
      }

      public static void drawCircleAroundEntity(Entity e, double expand, float lineWidth, int sides, int color, boolean damage) {
         if (e instanceof EntityPlayer && damage && ((EntityPlayer) e).hurtTime != 0) {
            color = Color.RED.getRGB();
         }

         double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
         double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
         double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;

         d3p(x, y, z, 0.699999988079071D + expand, sides, lineWidth, color, color == 0);
      }

      public static void drawBoxAroundItem(EntityItem item, int type, double expand, int color) {
         if (item instanceof EntityItem) {
            double x = item.lastTickPosX + (item.posX - item.lastTickPosX) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
            double y = item.lastTickPosY + (item.posY - item.lastTickPosY) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
            double z = item.lastTickPosZ + (item.posZ - item.lastTickPosZ) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
            float d = (float) expand / 40.0F;

            GlStateManager.pushMatrix();
            if (type == 3) {
               GL11.glTranslated(x, y - 0.2D, z);
               GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
               GlStateManager.disableDepth();
               GL11.glScalef(0.03F + d, 0.03F + d, 0.03F + d);
               int outline = Color.black.getRGB();
               net.minecraft.client.gui.Gui.drawRect(-20, -1, -26, 75, outline);
               net.minecraft.client.gui.Gui.drawRect(20, -1, 26, 75, outline);
               net.minecraft.client.gui.Gui.drawRect(-20, -1, 21, 5, outline);
               net.minecraft.client.gui.Gui.drawRect(-20, 70, 21, 75, outline);
               if (color != 0) {
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, -25, 74, color);
                  net.minecraft.client.gui.Gui.drawRect(21, 0, 25, 74, color);
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, 24, 4, color);
                  net.minecraft.client.gui.Gui.drawRect(-21, 71, 25, 74, color);
               } else {
                  int st = Utils.Client.rainbowDraw(2L, 0L);
                  int en = Utils.Client.rainbowDraw(2L, 1000L);
                  dGR(-21, 0, -25, 74, st, en);
                  dGR(21, 0, 25, 74, st, en);
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, 21, 4, en);
                  net.minecraft.client.gui.Gui.drawRect(-21, 71, 21, 74, st);
               }

               GlStateManager.enableDepth();
            } else {
               int i;
               if (type == 6) {
                  d3p(x, y + 0.000, z, 0.699999988079071D, 45, 1.5F, color, color == 0);
               } else {
                  if (color == 0) {
                     color = Utils.Client.rainbowDraw(2L, 0L);
                  }

                  float a = (float) (color >> 24 & 255) / 255.0F;
                  float r = (float) (color >> 16 & 255) / 255.0F;
                  float g = (float) (color >> 8 & 255) / 255.0F;
                  float b = (float) (color & 255) / 255.0F;
                  if (type == 5) {
                     GL11.glTranslated(x, y - 0.2D, z);
                     GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                     GlStateManager.disableDepth();
                     GL11.glScalef(0.03F + d, 0.03F, 0.03F + d);
                     int base = 1;
                     d2p(0.0D, 95.0D, 10, 3, Color.black.getRGB());

                     for (i = 0; i < 6; ++i) {
                        d2p(0.0D, 95 + (10 - i), 3, 4, Color.black.getRGB());
                     }

                     for (i = 0; i < 7; ++i) {
                        d2p(0.0D, 95 + (10 - i), 2, 4, color);
                     }

                     d2p(0.0D, 95.0D, 8, 3, color);
                     GlStateManager.enableDepth();
                  } else {
                     AxisAlignedBB bbox = item.getEntityBoundingBox().expand(0.1D + expand, 0.1D + expand, 0.1D + expand);
                     AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - item.posX + x, bbox.minY - item.posY + y,
                             bbox.minZ - item.posZ + z, bbox.maxX - item.posX + x, bbox.maxY - item.posY + y, bbox.maxZ - item.posZ + z);
                     GL11.glBlendFunc(770, 771);
                     GL11.glEnable(3042);
                     GL11.glDisable(3553);
                     GL11.glDisable(2929);
                     GL11.glDepthMask(false);
                     GL11.glLineWidth(2.0F);
                     GL11.glColor4f(r, g, b, a);
                     if (type == 1) {
                        RenderGlobal.drawSelectionBoundingBox(axis);
                     } else if (type == 2) {
                        dbb(axis, r, g, b);
                     }

                     GL11.glEnable(3553);
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                     GL11.glDisable(3042);
                  }
               }
            }

            GlStateManager.popMatrix();
         }
      }

      public static void drawBoxAroundEntity(Entity e, int type, double expand, double shift, int color, boolean damage) {
         if (e instanceof EntityLivingBase) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
            float d = (float) expand / 40.0F;
            if (e instanceof EntityPlayer && damage && ((EntityPlayer) e).hurtTime != 0) {
               color = Color.RED.getRGB();
            }

            GlStateManager.pushMatrix();
            if (type == 3) {
               GL11.glTranslated(x, y - 0.2D, z);
               GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
               GlStateManager.disableDepth();
               GL11.glScalef(0.03F + d, 0.03F + d, 0.03F + d);
               int outline = Color.black.getRGB();
               net.minecraft.client.gui.Gui.drawRect(-20, -1, -26, 75, outline);
               net.minecraft.client.gui.Gui.drawRect(20, -1, 26, 75, outline);
               net.minecraft.client.gui.Gui.drawRect(-20, -1, 21, 5, outline);
               net.minecraft.client.gui.Gui.drawRect(-20, 70, 21, 75, outline);
               if (color != 0) {
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, -25, 74, color);
                  net.minecraft.client.gui.Gui.drawRect(21, 0, 25, 74, color);
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, 24, 4, color);
                  net.minecraft.client.gui.Gui.drawRect(-21, 71, 25, 74, color);
               } else {
                  int st = Utils.Client.rainbowDraw(2L, 0L);
                  int en = Utils.Client.rainbowDraw(2L, 1000L);
                  dGR(-21, 0, -25, 74, st, en);
                  dGR(21, 0, 25, 74, st, en);
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, 21, 4, en);
                  net.minecraft.client.gui.Gui.drawRect(-21, 71, 21, 74, st);
               }

               GlStateManager.enableDepth();
            } else {
               int i;
               if (type == 4) {
                  EntityLivingBase en = (EntityLivingBase) e;
                  double r = en.getHealth() / en.getMaxHealth();
                  int b = (int) (74.0D * r);
                  int hc = r < 0.3D ? Color.red.getRGB() : (r < 0.5D ? Color.orange.getRGB() : (r < 0.7D ? Color.yellow.getRGB() : Color.green.getRGB()));
                  GL11.glTranslated(x, y - 0.2D, z);
                  GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                  GlStateManager.disableDepth();
                  GL11.glScalef(0.03F + d, 0.03F + d, 0.03F + d);
                  i = (int) (21.0D + shift * 2.0D);
                  net.minecraft.client.gui.Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                  net.minecraft.client.gui.Gui.drawRect(i + 1, b, i + 4, 74, Color.darkGray.getRGB());
                  net.minecraft.client.gui.Gui.drawRect(i + 1, 0, i + 4, b, hc);
                  GlStateManager.enableDepth();

               } else if (type == 6) {
                  d3p(x, y + 0.000, z, 0.699999988079071D, 45, 1.5F, color, color == 0);
               } else {
                  if (color == 0) {
                     color = Utils.Client.rainbowDraw(2L, 0L);
                  }

                  float a = (float) (color >> 24 & 255) / 255.0F;
                  float r = (float) (color >> 16 & 255) / 255.0F;
                  float g = (float) (color >> 8 & 255) / 255.0F;
                  float b = (float) (color & 255) / 255.0F;
                  if (type == 5) {
                     GL11.glTranslated(x, y - 0.2D, z);
                     GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                     GlStateManager.disableDepth();
                     GL11.glScalef(0.03F + d, 0.03F, 0.03F + d);
                     int base = 1;
                     d2p(0.0D, 95.0D, 10, 3, Color.black.getRGB());

                     for (i = 0; i < 6; ++i) {
                        d2p(0.0D, 95 + (10 - i), 3, 4, Color.black.getRGB());
                     }

                     for (i = 0; i < 7; ++i) {
                        d2p(0.0D, 95 + (10 - i), 2, 4, color);
                     }

                     d2p(0.0D, 95.0D, 8, 3, color);
                     GlStateManager.enableDepth();
                  } else {
                     AxisAlignedBB bbox = e.getEntityBoundingBox().expand(0.1D + expand, 0.1D + expand, 0.1D + expand);
                     AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y,
                             bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                     GL11.glBlendFunc(770, 771);
                     GL11.glEnable(3042);
                     GL11.glDisable(3553);
                     GL11.glDisable(2929);
                     GL11.glDepthMask(false);
                     GL11.glLineWidth(2.0F);
                     GL11.glColor4f(r, g, b, a);
                     if (type == 1) {
                        RenderGlobal.drawSelectionBoundingBox(axis);
                     } else if (type == 2) {
                        dbb(axis, r, g, b);
                     }

                     GL11.glEnable(3553);
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                     GL11.glDisable(3042);
                  }
               }
            }

            GlStateManager.popMatrix();
         }
      }

      public static void dbb(AxisAlignedBB abb, float r, float g, float b) {
         float a = 0.25F;
         Tessellator ts = Tessellator.getInstance();
         WorldRenderer vb = ts.getWorldRenderer();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         ts.draw();
      }

      public static void drawTracerLine(Entity e, int color, float lw) {
         if (e != null) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
            double y = (double) e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(lw);
            GL11.glColor4f(r, g, b, a);
            GL11.glBegin(2);
            GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
            GL11.glVertex3d(x, y, z);
            GL11.glEnd();
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         }
      }

       public static void drawLine(AxisAlignedBB e, int color, float lw) {
           if (e != null) {
               /*
                double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
                double y = (double) e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
                double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
                */


               double x = e.minX + (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
               double y = e.minZ + (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
               double z = e.minZ + (double) Utils.Client.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
               float a = (float) (color >> 24 & 255) / 255.0F;
               float r = (float) (color >> 16 & 255) / 255.0F;
               float g = (float) (color >> 8 & 255) / 255.0F;
               float b = (float) (color & 255) / 255.0F;
               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glEnable(2848);
               GL11.glDisable(2929);
               GL11.glDisable(3553);
               GL11.glBlendFunc(770, 771);
               GL11.glEnable(3042);
               GL11.glLineWidth(lw);
               GL11.glColor4f(r, g, b, a);
               GL11.glBegin(2);
               GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
               GL11.glVertex3d(x, y, z);
               GL11.glEnd();
               GL11.glDisable(3042);
               GL11.glEnable(3553);
               GL11.glEnable(2929);
               GL11.glDisable(2848);
               GL11.glDisable(3042);
               GL11.glPopMatrix();
           }
       }

      public static void dGR(int left, int top, int right, int bottom, int startColor, int endColor) {
         int j;
         if (left < right) {
            j = left;
            left = right;
            right = j;
         }

         if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
         }

         float f = (float) (startColor >> 24 & 255) / 255.0F;
         float f1 = (float) (startColor >> 16 & 255) / 255.0F;
         float f2 = (float) (startColor >> 8 & 255) / 255.0F;
         float f3 = (float) (startColor & 255) / 255.0F;
         float f4 = (float) (endColor >> 24 & 255) / 255.0F;
         float f5 = (float) (endColor >> 16 & 255) / 255.0F;
         float f6 = (float) (endColor >> 8 & 255) / 255.0F;
         float f7 = (float) (endColor & 255) / 255.0F;
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.disableAlpha();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.shadeModel(7425);
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
         worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
         worldrenderer.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
         worldrenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
         worldrenderer.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
         worldrenderer.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
         tessellator.draw();
         GlStateManager.shadeModel(7424);
         GlStateManager.disableBlend();
         GlStateManager.enableAlpha();
         GlStateManager.enableTexture2D();
      }

      public static void db(int w, int h, int r) {
         int c = r == -1 ? -1089466352 : r;
         net.minecraft.client.gui.Gui.drawRect(0, 0, w, h, c);
      }

      public static void drawColouredText(String text, char lineSplit, int leftOffset, int topOffset, long colourParam1, long shift, boolean rect, FontRenderer fontRenderer) {
         int bX = leftOffset;
         int l = 0;
         long colourControl = 0L;

         for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == lineSplit) {
               ++l;
               leftOffset = bX;
               topOffset += fontRenderer.FONT_HEIGHT + 5;
               colourControl = shift * (long) l;
            } else {
               fontRenderer.drawString(String.valueOf(c), (float) leftOffset, (float) topOffset, Utils.Client.astolfoColorsDraw((int) colourParam1, (int) colourControl), rect);
               leftOffset += fontRenderer.getCharWidth(c);
               if (c != ' ') {
                  colourControl -= 90L;
               }
            }
         }
      }

      public static PositionMode getPostitionMode(int marginX, int marginY, double height, double width) {
         int halfHeight = (int) (height / 4);
         int halfWidth = (int) width;
         PositionMode positionMode = null;
         // up left

         if (marginY < halfHeight) {
            if (marginX < halfWidth) {
               positionMode = PositionMode.UPLEFT;
            }
            if (marginX > halfWidth) {
               positionMode = PositionMode.UPRIGHT;
            }
         }

         if (marginY > halfHeight) {
            if (marginX < halfWidth) {
               positionMode = PositionMode.DOWNLEFT;
            }
            if (marginX > halfWidth) {
               positionMode = PositionMode.DOWNRIGHT;
            }
         }

         return positionMode;
      }

      public static void d2p(double x, double y, int radius, int sides, int color) {
         float a = (float) (color >> 24 & 255) / 255.0F;
         float r = (float) (color >> 16 & 255) / 255.0F;
         float g = (float) (color >> 8 & 255) / 255.0F;
         float b = (float) (color & 255) / 255.0F;
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
         GlStateManager.enableBlend();
         GlStateManager.disableTexture2D();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.color(r, g, b, a);
         worldrenderer.begin(6, DefaultVertexFormats.POSITION);

         for (int i = 0; i < sides; ++i) {
            double angle = 6.283185307179586D * (double) i / (double) sides + Math.toRadians(180.0D);
            worldrenderer.pos(x + Math.sin(angle) * (double) radius, y + Math.cos(angle) * (double) radius, 0.0D).endVertex();
         }

         tessellator.draw();
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }

      public static void d3p(double x, double y, double z, double radius, int sides, float lineWidth, int color, boolean chroma) {
         float a = (float) (color >> 24 & 255) / 255.0F;
         float r = (float) (color >> 16 & 255) / 255.0F;
         float g = (float) (color >> 8 & 255) / 255.0F;
         float b = (float) (color & 255) / 255.0F;
         mc.entityRenderer.disableLightmap();
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(lineWidth);
         if (!chroma) {
            GL11.glColor4f(r, g, b, a);
         }

         GL11.glBegin(1);
         long d = 0L;
         long ed = 15000L / (long) sides;
         long hed = ed / 2L;

         for (int i = 0; i < sides * 2; ++i) {
            if (chroma) {
               if (i % 2 != 0) {
                  if (i == 47) {
                     d = hed;
                  }

                  d += ed;
               }

               int c = Utils.Client.rainbowDraw(2L, d);
               float r2 = (float) (c >> 16 & 255) / 255.0F;
               float g2 = (float) (c >> 8 & 255) / 255.0F;
               float b2 = (float) (c & 255) / 255.0F;
               GL11.glColor3f(r2, g2, b2);
            }

            double angle = 6.283185307179586D * (double) i / (double) sides + Math.toRadians(180.0D);
            GL11.glVertex3d(x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius);
         }

         GL11.glEnd();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glEnable(2929);
         GL11.glDisable(3042);
         GL11.glEnable(3553);
         mc.entityRenderer.enableLightmap();
      }

      public enum PositionMode {UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT}
   }

   public static class Modes {
      public enum ClickEvents {RENDER, TICK}

      public enum ClickTimings {RavenNPlus, SKID}

      public enum SprintResetTimings {PRE, POST}
   }

   public static class FriendUtils {
      public static ArrayList<Entity> friends = new ArrayList<>();

      public static void addFriend(Entity entityPlayer) {
         friends.add(entityPlayer);
      }

      public static boolean addFriend(String name) {
         boolean found = false;
         for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity.getName().equalsIgnoreCase(name) || entity.getCustomNameTag().equalsIgnoreCase(name)) {
               if (!isAFriend(entity)) {
                  addFriend(entity);
                  found = true;
               }
            }
         }

         return found;
      }

      public static boolean removeFriend(String name) {
         boolean removed = false;
         boolean found = false;
         for (NetworkPlayerInfo networkPlayerInfo : new ArrayList<>(mc.getNetHandler().getPlayerInfoMap())) {
            Entity entity = mc.theWorld.getPlayerEntityByName(networkPlayerInfo.getDisplayName().getUnformattedText());
            if (entity.getName().equalsIgnoreCase(name) || entity.getCustomNameTag().equalsIgnoreCase(name)) {
               removed = removeFriend(entity);
               found = true;
            }
         }

         return found && removed;
      }

      public static boolean removeFriend(Entity entityPlayer) {
         try {
            friends.remove(entityPlayer);
         } catch (Exception e) {
            e.printStackTrace();
            return false;
         }
         return true;
      }

      public static ArrayList<Entity> getFriends() {
         return friends;
      }

      public static int getFriendCount() {
         return friends.size();
      }

      public static boolean isAFriend(Entity entity) {
         if (entity == mc.thePlayer) return true;

         for (Entity en : friends) {
            if (en.equals(entity))
               return true;
         }
         try {
            EntityPlayer en = (EntityPlayer) entity;
            if (ravenNPlus.client.main.Client.debugger) {
               Utils.Player.sendMessageToSelf("unformatted / " + en.getDisplayName().getUnformattedText().replace("§", "%"));

               Utils.Player.sendMessageToSelf("substring entity / " + en.getDisplayName().getUnformattedText().substring(0, 2));
               Utils.Player.sendMessageToSelf("substring player / " + mc.thePlayer.getDisplayName().getUnformattedText().substring(0, 2));
            }
            if (mc.thePlayer.isOnSameTeam((EntityLivingBase) entity) || mc.thePlayer.getDisplayName().getUnformattedText().startsWith(en.getDisplayName().getUnformattedText().substring(0, 2)))
               return true;
         } catch (Exception ex) {
            if (ravenNPlus.client.main.Client.debugger) {
               Utils.Player.sendMessageToSelf(ex.getMessage());
            }
         }

         return false;
      }
   }

}