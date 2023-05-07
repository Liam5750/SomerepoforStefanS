package ravenNPlus.client.module.setting.impl;

import com.google.gson.JsonObject;
import ravenNPlus.client.module.setting.Setting;
import ravenNPlus.client.clickgui.RavenNPlus.Component;
import ravenNPlus.client.clickgui.RavenNPlus.components.ModuleComponent;

public class ModeSetting<T extends Enum<?>> extends Setting {

    private T[] options;
    private T currentOption;
    private final T defaultOption;

    public ModeSetting(String settingName, T defaultOption) {
        super(settingName);

        this.currentOption = defaultOption;
        this.defaultOption = defaultOption;
        try {
            this.options = (T[]) defaultOption.getClass().getMethod("values").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetToDefaults() {
        this.currentOption = defaultOption;
    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", getSettingType());
        data.addProperty("value", getMode().toString());
        return data;
    }

    @Override
    public String getSettingType() {
        return "mode";
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if(!data.get("type").getAsString().equals(getSettingType()))
            return;

        String x = data.get("value").getAsString();
        for(T opt : options) {
            if(opt.toString().equals(x))
                setMode(opt);
        }
    }

    @Override
    public Component createComponent(ModuleComponent moduleComponent) {
        return null;
    }

    public T getMode() {
        return this.currentOption;
    }

    public void setMode(T value) {
        this.currentOption = value;
    }

    public void nextMode() {
        if(options == null) return;

        for(int i = 0; i < options.length; i++) {
            if(options[i] == currentOption) {
                currentOption = options[(i + 1) % (options.length)];
                return;
            }
        }
    }

}