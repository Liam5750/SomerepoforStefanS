package ravenNPlus.client.module.setting;

import ravenNPlus.client.clickgui.RavenNPlus.components.ModuleComponent;

public abstract class Setting {
   public String settingName;
   public Setting(String name) { this.settingName = name; }
   public String getName() { return this.settingName; }
   public abstract void resetToDefaults();
   public abstract com.google.gson.JsonObject getConfigAsJson();
   public abstract String getSettingType();
   public abstract void applyConfigFromJson(com.google.gson.JsonObject data);
   public abstract ravenNPlus.client.clickgui.RavenNPlus.Component createComponent(ModuleComponent moduleComponent);
}