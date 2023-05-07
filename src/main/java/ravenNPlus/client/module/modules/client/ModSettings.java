package ravenNPlus.client.module.modules.client;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.*;

public class ModSettings extends Module {

    public static java.awt.Color slider_finalColor, slider_finalColor2;
    public static SliderSetting slider_color_green, slider_color_green_i, slider_color_blue_i, slider_color_blue, slider_color_red, text_xOff, text_yOff;
    public static SliderSetting slider_color_red_i, slider_name_Xoff, slider_name_Yoff, slider_widthRad, text_underline_off;
    public static TickSetting slider_color_c, text_color_c, text_underline, costumFont;
    public static DescriptionSetting desc;
    public static ModeSetting slider_mode;

    public ModSettings() {
        super("Module Settings", ModuleCategory.client, "Edit the UI of Settings in Modules");
        this.addSetting(desc = new DescriptionSetting("Module Settings"));
        this.addSetting(costumFont = new TickSetting("Custom Text Font", o));
        this.addSetting(text_color_c = new TickSetting("Custom Text Color", o));
        this.addSetting(text_underline = new TickSetting("Text Underline", o));
        this.addSetting(text_underline_off = new SliderSetting("Underline expand", 1.4, -10, 10, 0.1));
        this.addSetting(text_xOff = new SliderSetting("Text x Offset", 0, -50, 50, 1));
        this.addSetting(text_yOff = new SliderSetting("Text y Offset", 0, -50, 50, 1));
        this.addSetting(desc = new DescriptionSetting("Module Setting Settings"));
        this.addSetting(slider_mode = new ModeSetting("Mode", ModSettings.slider_modes.Round));
        this.addSetting(slider_color_c = new TickSetting("Custom Slider Color", o));
        this.addSetting(slider_color_red = new SliderSetting("Out Slider Red", 1, 1, 255, 1));
        this.addSetting(slider_color_green = new SliderSetting("Out Slider Green", 1, 1, 255, 1));
        this.addSetting(slider_color_blue = new SliderSetting("Out Slider Blue", 1, 1, 255, 1));
        this.addSetting(slider_color_red_i = new SliderSetting("In Slider Red", 1, 1, 255, 1));
        this.addSetting(slider_color_green_i = new SliderSetting("In Slider Green", 1, 1, 255, 1));
        this.addSetting(slider_color_blue_i = new SliderSetting("In Slider Blue", 1, 1, 255, 1));
        this.addSetting(slider_widthRad = new SliderSetting("Slider Round Range", 2, -10, 10, 1));
        this.addSetting(slider_name_Xoff = new SliderSetting("Slider Name x Offset", 0, -50, 50, 1));
        this.addSetting(slider_name_Yoff = new SliderSetting("Slider Name y Offset", 0, -50, 50, 1));
    }

    @Override
    public void onEnable() {
        this.disable();
    }

    @Override
    public void guiButtonToggled(TickSetting setting) {
        if (setting == text_underline && costumFont.isToggled()) {
            text_underline.disable();
        }
    }

    public void guiUpdate() {
        try {
            if (this.inGame() && (slider_color_c.isToggled() || text_color_c.isToggled())) {
                slider_finalColor = new java.awt.Color((int) slider_color_red.getValue(), (int) slider_color_green.getValue(), (int) slider_color_blue.getValue());
                slider_finalColor2 = new java.awt.Color((int) slider_color_red_i.getValue(), (int) slider_color_green_i.getValue(), (int) slider_color_blue_i.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum slider_modes {
        Normal, Round, Outlines
    }

}