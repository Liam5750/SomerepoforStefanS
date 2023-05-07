package ravenNPlus.client.module.setting.impl;

import com.google.gson.JsonObject;
import ravenNPlus.client.module.setting.Setting;
import ravenNPlus.client.clickgui.RavenNPlus.Component;
import ravenNPlus.client.clickgui.RavenNPlus.components.ModuleComponent;

public class SliderSetting extends Setting {

   private final String name;
   private double value;
   private final double max, min;
   private final double interval, defaultVal;

   public SliderSetting(String name, double defaultValue, double minValue, double maxValue, double addValue) {
      super(name);
      this.name = name;
      this.value = defaultValue;
      this.min = minValue;
      this.max = maxValue;
      this.interval = addValue;
      this.defaultVal = defaultValue;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public void resetToDefaults() {
      this.value = defaultVal;
   }

   @Override
   public JsonObject getConfigAsJson() {
      JsonObject data = new JsonObject();
      data.addProperty("type", getSettingType());
      data.addProperty("value", getValue());
      return data;
   }

   @Override
   public String getSettingType() {
      return "slider";
   }

   @Override
   public void applyConfigFromJson(JsonObject data) {
      if (!data.get("type").getAsString().equals(getSettingType()))
         return;

      setValue(data.get("value").getAsDouble());
   }

   @Override
   public Component createComponent(ModuleComponent moduleComponent) {
      return null;
   }

   public double getValue() {
      return r(this.value, 2);
   }

   public float getValueToFloat() {
      return (float) getValue();
   }

   public int getValueToInt() {
      return (int) getValue();
   }

   public long getValueToLong() {
      return (long) getValue();
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public double getInterval() {
      return this.interval;
   }

   public double getDefaultVal() {
      return defaultVal;
   }

   public double minMinusMax() {
      return this.min - this.max;
   }

   public double minPlusMax() {
      return this.min + this.max;
   }

   public double minTimesMax() {
      return this.min * this.max;
   }

   public double minHalfMax() {
      return this.min / this.max;
   }

   public double maxMinusMin() {
      return this.max - this.min;
   }

   public double maxPlusMin() {
      return this.max + this.min;
   }

   public double maxTimesMin() {
      return this.max * this.min;
   }

   public double maxHalfMin() {
      return this.max / this.min;
   }

   public double roundValue() {
      return Math.round(this.getValue());
   }

   public float roundValueToFloat() {
      return Math.round(this.getValueToFloat());
   }

   public int roundValueToInt() {
      return Math.round(this.getValueToInt());
   }

   public long roundValueToLong() {
      return Math.round(this.getValueToLong());
   }

   public void setValue(double n) {
      n = check(n, this.min, this.max);
      n = (double) Math.round(n * (1.0D / this.interval)) / (1.0D / this.interval);
      this.value = n;
   }

   public static double check(double defa, double max, double min) {
      defa = Math.max(max, defa);
      defa = Math.min(min, defa);
      return defa;
   }

   public static double r(double v, int p) {
      if (p < 0) {
         return 0.0D;
      } else {
         java.math.BigDecimal bd = new java.math.BigDecimal(v);
         bd = bd.setScale(p, java.math.RoundingMode.HALF_UP);

         return bd.doubleValue();
      }
   }

}