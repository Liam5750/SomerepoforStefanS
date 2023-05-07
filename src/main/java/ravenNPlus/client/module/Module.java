package ravenNPlus.client.module;

import ravenNPlus.client.utils.Utils;
import ravenNPlus.client.utils.SoundUtil;
import ravenNPlus.client.utils.RenderUtils;
import ravenNPlus.client.module.setting.Setting;
import ravenNPlus.client.utils.notifications.Type;
import ravenNPlus.client.utils.notifications.Render;
import ravenNPlus.client.utils.animations.Animate;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.modules.client.CategorySett;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import com.google.gson.JsonObject;

public abstract class Module {

   protected ArrayList<Setting> settings;
   private final String moduleName;
   private final String desc;
   private final ModuleCategory moduleCategory;
   protected boolean enabled = false;
   protected boolean showInHud = true;
   protected boolean defaultEnabled = enabled;
   protected int keycode = 0;
   protected int defualtKeyCode = keycode;
   protected static Minecraft mc;
   private boolean isToggled = false;
   public TickSetting showinArrayList;
   public int arrayInteger = 0;
   public boolean x = true;
   public boolean o = false;
   public boolean oldState;
   public Animate ani = new Animate();

   public Module(String name, ModuleCategory moduleCategory, String description) {
      showinArrayList = new TickSetting("Show in HUD", true);
      this.moduleName = name;
      this.moduleCategory = moduleCategory;
      this.desc = description;
      this.settings = new ArrayList<>();
      mc = Minecraft.getMinecraft();
   }

   public void drawDescription(String beforeDesc, int x, int y, int mode, int color) {
      mc.fontRendererObj.drawString(beforeDesc + getDescription(mode), x, y, color);
   }

   public String getDescription(int mode) {

      // Normal
      if (mode == 1)
         return this.desc;

      // Lower case
      if (mode == 2)
         return this.desc.toLowerCase();

      // Upper case
      if (mode == 3)
         return this.desc.toUpperCase();

      // Empty checks
      if (getDescription(mode).equals(" ") || getDescription(mode).equals(""))
         return "No description found";

      return "Wrong mode ?";
   }

   protected <E extends Module> E withKeycode(int i) {
      this.keycode = i;
      this.defualtKeyCode = i;
      return (E) this;
   }

   protected <E extends Module> E withEnabled(boolean i) {
      this.enabled = i;
      this.defaultEnabled = i;
      try {
         setToggled(i);
      } catch (Exception e) {
      }
      return (E) this;
   }

   public JsonObject getConfigAsJson() {
      JsonObject settings = new JsonObject();

      for (Setting setting : this.settings) {
         if (setting != null) {
            JsonObject settingData = setting.getConfigAsJson();
            settings.add(setting.settingName, settingData);
         }
      }

      JsonObject data = new JsonObject();
      data.addProperty("enabled", enabled);
      data.addProperty("keycode", keycode);
      data.addProperty("showInHud", showInHud);
      data.add("settings", settings);

      return data;
   }

   public void applyConfigFromJson(JsonObject data) {
      try {
         this.keycode = data.get("keycode").getAsInt();
         setToggled(data.get("enabled").getAsBoolean());
         JsonObject settingsData = data.get("settings").getAsJsonObject();
         for (Setting setting : getSettings()) {
            if (settingsData.has(setting.getName())) {
               setting.applyConfigFromJson(
                       settingsData.get(setting.getName()).getAsJsonObject()
               );
            }
         }

         this.showInHud = data.get("showInHud").getAsBoolean();
      } catch (NullPointerException ignored) {
      }
   }

   public void keybind() {
      if (this.keycode != 0 && this.canBeEnabled()) {
         if (!this.isToggled && Keyboard.isKeyDown(this.keycode)) {
            this.toggle();
            this.isToggled = true;
         } else if (!Keyboard.isKeyDown(this.keycode)) {
            this.isToggled = false;
         }
      }
   }

   public boolean showInHud() {
      return showInHud;
   }

   public void setVisableInHud(boolean vis) {
      this.showInHud = vis;
   }

   public boolean canBeEnabled() {
      return true;
   }

   protected boolean playSound() {
      String i = moduleName;

      return i.equals("Update") || i.equals("Module Settings") || i.equals("Category Settings")
              || i.equals("BurstClicker") || i.equals("Fake Chat") || i.equals("Trajectories")
              || i.equals("VClip") || i.equals("Stop Motion") || i.equals("Self Destruct")
              || i.equals("Weapon") || i.equals("Ladders") || i.equals("SlyPort")
              || i.equals("Healing") || i.equals("Armour") || i.equals("Pearl")
              || i.equals("Blocks") || i.equals("ClickGUI");
   }

   public void enable() {
      oldState = this.enabled;
      this.enabled = true;
      this.onEnable();
      MinecraftForge.EVENT_BUS.register(this);
      Render.change(this);

      if (CategorySett.sounds.isToggled()) {
         if (playSound()) return;

         if (CategorySett.enable_mode.getValue() == 1)
            SoundUtil.play(SoundUtil.click1, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());

         if (CategorySett.enable_mode.getValue() == 2)
            SoundUtil.play(SoundUtil.bowhit, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());

         if (CategorySett.enable_mode.getValue() == 3)
            SoundUtil.play(SoundUtil.playerHurt, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());

         if (CategorySett.enable_mode.getValue() == 4)
            SoundUtil.play(SoundUtil.playerDie, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());

         if (CategorySett.enable_mode.getValue() == 5)
            SoundUtil.play(SoundUtil.chestOpen, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());

         if (CategorySett.enable_mode.getValue() == 6)
            SoundUtil.play(SoundUtil.chestClose, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());

         if (CategorySett.enable_mode.getValue() == 7)
            SoundUtil.play(SoundUtil.tntExplosion, CategorySett.enable_volume.getValueToFloat(), CategorySett.enable_pitch.getValueToFloat());
      }
   }

   //----------------------------------------------------------------------------------------------------------

   public void crash() {
      System.exit((int) Float.NaN);
   }

   public void notification(Type type, String message, int length) {
      RenderUtils.notification(type, this.getName(), message, length);
   }

   public boolean inWater() {
      return player().isInWater();
   }

   public boolean inLava() {
      return player().isInLava();
   }

   public boolean inGame() {
      return Utils.Player.isPlayerInGame();
   }

   public boolean isAlive() {
      return player().isEntityAlive();
   }

   public boolean isMoving() {
      return Utils.Player.isMoving();
   }

   public boolean inFocus() {
      return mc.inGameHasFocus;
   }

   public boolean isSingleplayer() {
      return mc.isSingleplayer();
   }

   public boolean isEating() {
      return player().isEating();
   }

   public boolean isBlocking() {
      return player().isBlocking();
   }

   public boolean isSneaking() {
      return player().isSneaking();
   }

   public boolean onGround() {
      return player().onGround;
   }

   public boolean onLadder() {
      return player().isOnLadder();
   }

   public EntityPlayerSP player() {
      return mc.thePlayer;
   }

   public void sendPacketNetHandler(Packet packet) {
      mc.getNetHandler().addToSendQueue(packet);
   }

   public void sendPacketPlayer(Packet packet) {
      player().sendQueue.addToSendQueue(packet);
   }

   public void attackPlayer(Entity en) {
      mc.playerController.attackEntity(mc.thePlayer, en);
   }

   public void sendChatMessage(String message) {
      player().sendChatMessage(message);
   }

   public float screenWidth() {
      ScaledResolution sr = new ScaledResolution(mc);
      return (float) sr.getScaledWidth() / 2;
   }

   public float screenHeight() {
      ScaledResolution sr = new ScaledResolution(mc);
      return (float) sr.getScaledHeight() / 2;
   }

   public void swing(boolean silent) {
      if (!silent)
         Utils.Player.legitSwing();
      else
         Utils.Player.swing();
   }

   //----------------------------------------------------------------------------------------------------------

   public void disable() {
      if (this.arrayInteger != 0)
         this.arrayInteger = 0;

      oldState = this.enabled;
      this.enabled = false;
      this.onDisable();
      MinecraftForge.EVENT_BUS.unregister(this);
      Render.change(this);

      if (CategorySett.sounds.isToggled()) {
         if (playSound()) return;

         if (CategorySett.disable_mode.getValue() == 1)
            SoundUtil.play(SoundUtil.click1, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());

         if (CategorySett.disable_mode.getValue() == 2)
            SoundUtil.play(SoundUtil.bowhit, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());

         if (CategorySett.disable_mode.getValue() == 3)
            SoundUtil.play(SoundUtil.playerHurt, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());

         if (CategorySett.disable_mode.getValue() == 4)
            SoundUtil.play(SoundUtil.playerDie, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());

         if (CategorySett.disable_mode.getValue() == 5)
            SoundUtil.play(SoundUtil.chestOpen, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());

         if (CategorySett.disable_mode.getValue() == 6)
            SoundUtil.play(SoundUtil.chestClose, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());

         if (CategorySett.disable_mode.getValue() == 7)
            SoundUtil.play(SoundUtil.tntExplosion, CategorySett.disable_volume.getValueToFloat(), CategorySett.disable_pitch.getValueToFloat());
      }
   }

   public void setToggled(boolean enabled) {
      if (enabled) {
         enable();
      } else {
         disable();
      }
   }

   public String getName() {
      return this.moduleName;
   }

   public ArrayList<Setting> getSettings() {
      return this.settings;
   }

   public Setting getSettingByName(String name) {
      for (Setting setting : this.settings) {
         if (setting.getName().equalsIgnoreCase(name))
            return setting;
      }
      return null;
   }

   public boolean getSettingByNameAsBoolean(String name, boolean mustBeToggled) {
      for (Setting setting : this.settings) {
         if (setting.getName().equalsIgnoreCase(name) && !mustBeToggled)
            return true;
         else if (setting.getName().equalsIgnoreCase(name) && mustBeToggled && setting.getConfigAsJson().getAsJsonObject(String.valueOf(setting)).equals(true)) {
            return true;
         }
      }
      return false;
   }

   public void addSetting(Setting Setting) {
      this.settings.add(Setting);
   }

   public ModuleCategory moduleCategory() {
      return this.moduleCategory;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void onEnable() { }

   public void onDisable() { }

   public void toggle() {
      if (this.enabled) {
         this.disable();
      } else {
         this.enable();
      }
   }

   public void update() { }

   public void guiUpdate() { }

   public void guiButtonToggled(TickSetting b) { }

   public int getKeycode() {
      return this.keycode;
   }

   public void setbind(int keybind) {
      this.keycode = keybind;
   }

   public void resetToDefaults() {
      this.keycode = defualtKeyCode;
      this.setToggled(defaultEnabled);
      for (Setting setting : this.settings) {
         setting.resetToDefaults();
      }
   }

   public String getBindAsString() {
      return keycode == 0 ? "None" : Keyboard.getKeyName(keycode);
   }

   public void clearBinds() {
      this.keycode = 0;
   }

   public enum ModuleCategory {
      combat, movement,
      player, render,
      minigame, other,
      client, hotkey
   }

}