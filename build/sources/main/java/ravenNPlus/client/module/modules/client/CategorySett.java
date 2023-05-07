package ravenNPlus.client.module.modules.client;

import ravenNPlus.client.module.Module;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;

public class CategorySett extends Module {

    public static TickSetting Icon_client, Icon_move, Icon_comb, Icon_player, moduleDescriptions;
    public static TickSetting Icon_hotkey, Icon_render, Icon_other, Icon_mini, costum_color, sounds;
    public static SliderSetting disable_volume, disable_pitch, disable_mode, category_volume, category_pitch, category_mode;
    public static SliderSetting client_red, client_green, client_blue, TextOffset, comb_red, comb_green, comb_blue, iconSize;
    public static SliderSetting hotkey_red, hotkey_green, hotkey_blue, player_red, player_green, player_blue;
    public static SliderSetting enable_volume, enable_pitch, enable_mode, mini_red, mini_green, mini_blue;
    public static SliderSetting other_red, other_green, other_blue, render_red, render_green, render_blue;
    public static SliderSetting move_red, move_green, move_blue, xIconOffset, yIconOffset;
    public static DescriptionSetting desc, enable_mode_desc, disable_mode_desc, category_mode_desc;
    public static java.awt.Color client_color, move_color, other_color, mini_color, comb_color, player_color, hotkey_color, render_color;
    static String en = "Enable Mode: ", dn = "Disable Mode: ", cn = "Category Mode: ";

    public CategorySett() {
        super("Category Settings", ModuleCategory.client, "Settings for Categorys | icon path: assets/minecraft/ravenNPlus/icons/modules");
        this.addSetting(desc = new DescriptionSetting("Sound Settings"));
        this.addSetting(sounds = new TickSetting("Sounds", true));
        this.addSetting(enable_volume = new SliderSetting("Enable Sound Volume", 0.4, 0.0, 1.0, 0.1));
        this.addSetting(enable_pitch = new SliderSetting("Enable Sound Pitch", 0.8, 0.0, 1.0, 0.1));
        this.addSetting(enable_mode = new SliderSetting("Enable Sound  Mode", 1, 1, 7, 1));
        this.addSetting(enable_mode_desc = new DescriptionSetting(en));
        this.addSetting(disable_volume = new SliderSetting("Disable Sound Volume", 0.4, 0.0, 1.0, 0.1));
        this.addSetting(disable_pitch = new SliderSetting("Disable Sound Pitch", 0.7, 0.0, 1.0, 0.1));
        this.addSetting(disable_mode = new SliderSetting("Disable Sound Mode", 1, 1, 7, 1));
        this.addSetting(disable_mode_desc = new DescriptionSetting(dn));
        this.addSetting(category_volume = new SliderSetting("Category Sound Volume", 0.4, 0.0, 1.0, 0.1));
        this.addSetting(category_pitch = new SliderSetting("Category Sound Pitch", 0.7, 0.0, 1.0, 0.1));
        this.addSetting(category_mode = new SliderSetting("Category Sound Mode", 1, 1, 7, 1));
        this.addSetting(category_mode_desc = new DescriptionSetting(cn));
        this.addSetting(desc = new DescriptionSetting("Background Settings"));
        this.addSetting(moduleDescriptions = new TickSetting("Module Descriptions", false));
        this.addSetting(desc = new DescriptionSetting("Icon Settings"));
        this.addSetting(costum_color = new TickSetting("Costum Icon Colors", false));
        this.addSetting(TextOffset = new SliderSetting("Text Offset", 15, 1, 60, 1));
        this.addSetting(xIconOffset = new SliderSetting("x Icon Offset", 3, 1, 50, 1));
        this.addSetting(yIconOffset = new SliderSetting("y Icon Offset", 3, 1, 10, 1));
        this.addSetting(Icon_client = new TickSetting("Icon Client", true));
        this.addSetting(Icon_move = new TickSetting("Icon Movement", true));
        this.addSetting(Icon_comb = new TickSetting("Icon Combat", true));
        this.addSetting(Icon_player = new TickSetting("Icon Player", true));
        this.addSetting(Icon_hotkey = new TickSetting("Icon Hotkey", true));
        this.addSetting(Icon_render = new TickSetting("Icon Render", true));
        this.addSetting(Icon_other = new TickSetting("Icon Other", true));
        this.addSetting(Icon_mini = new TickSetting("Icon Minigame", true));
        this.addSetting(iconSize = new SliderSetting("Icon Size", 10, 1, 40, 1));
        this.addSetting(desc = new DescriptionSetting("Icon Color Settings"));
        this.addSetting(client_red = new SliderSetting("Client Icon Red", 1, 1, 255, 1));
        this.addSetting(client_green = new SliderSetting("Client Icon Green", 1, 1, 255, 1));
        this.addSetting(client_blue = new SliderSetting("Client Icon Blue", 1, 1, 255, 1));
        this.addSetting(move_red = new SliderSetting("Movement Icon Red", 1, 1, 255, 1));
        this.addSetting(move_green = new SliderSetting("Movement Icon Green", 1, 1, 255, 1));
        this.addSetting(move_blue = new SliderSetting("Movement Icon Blue", 1, 1, 255, 1));
        this.addSetting(comb_red = new SliderSetting("Combat Icon Red", 1, 1, 255, 1));
        this.addSetting(comb_green = new SliderSetting("Combat Icon Green", 1, 1, 255, 1));
        this.addSetting(comb_blue = new SliderSetting("Combat Icon Blue", 1, 1, 255, 1));
        this.addSetting(player_red = new SliderSetting("Player Icon Red", 1, 1, 255, 1));
        this.addSetting(player_green = new SliderSetting("Player Icon Green", 1, 1, 255, 1));
        this.addSetting(player_blue = new SliderSetting("Player Icon Blue", 1, 1, 255, 1));
        this.addSetting(hotkey_red = new SliderSetting("Hotkey Icon Red", 1, 1, 255, 1));
        this.addSetting(hotkey_green = new SliderSetting("Hotkey Icon Green", 1, 1, 255, 1));
        this.addSetting(hotkey_blue = new SliderSetting("Hotkey Icon Blue", 1, 1, 255, 1));
        this.addSetting(render_red = new SliderSetting("Render Icon Red", 1, 1, 255, 1));
        this.addSetting(render_green = new SliderSetting("Render Icon Green", 1, 1, 255, 1));
        this.addSetting(render_blue = new SliderSetting("Render Icon Blue", 1, 1, 255, 1));
        this.addSetting(other_red = new SliderSetting("Other Icon Red", 1, 1, 255, 1));
        this.addSetting(other_green = new SliderSetting("Other Icon Green", 1, 1, 255, 1));
        this.addSetting(other_blue = new SliderSetting("Other Icon Blue", 1, 1, 255, 1));
        this.addSetting(mini_red = new SliderSetting("Minigame Icon Red", 1, 1, 255, 1));
        this.addSetting(mini_green = new SliderSetting("Minigame Icon Green", 1, 1, 255, 1));
        this.addSetting(mini_blue = new SliderSetting("Minigame Icon Blue", 1, 1, 255, 1));
    }

    @Override
    public void onEnable() {
        this.disable();
    }

    public void guiUpdate() {
        if (this.inGame() && costum_color.isToggled()) {
            client_color = new java.awt.Color((int) client_red.getValue(), (int) client_green.getValue(), (int) client_blue.getValue());
            move_color = new java.awt.Color((int) move_red.getValue(), (int) move_green.getValue(), (int) move_blue.getValue());
            comb_color = new java.awt.Color((int) comb_red.getValue(), (int) comb_green.getValue(), (int) comb_blue.getValue());
            player_color = new java.awt.Color((int) player_red.getValue(), (int) player_green.getValue(), (int) player_blue.getValue());
            hotkey_color = new java.awt.Color((int) hotkey_red.getValue(), (int) hotkey_green.getValue(), (int) hotkey_blue.getValue());
            render_color = new java.awt.Color((int) render_red.getValue(), (int) render_green.getValue(), (int) render_blue.getValue());
            other_color = new java.awt.Color((int) other_red.getValue(), (int) other_green.getValue(), (int) other_blue.getValue());
            mini_color = new java.awt.Color((int) mini_red.getValue(), (int) mini_green.getValue(), (int) mini_blue.getValue());
        }

        String a = "click1", b = "bowhit", c = "player_hurt", d = "player_die";
        String e = "chest_open", f = "chest_close", g = "tnt_explosion", h = "?";

        switch ((int) enable_mode.getValue()) {
            case 1:
                enable_mode_desc.setDesc(en + a);
                break;
            case 2:
                enable_mode_desc.setDesc(en + b);
                break;
            case 3:
                enable_mode_desc.setDesc(en + c);
                break;
            case 4:
                enable_mode_desc.setDesc(en + d);
                break;
            case 5:
                enable_mode_desc.setDesc(en + e);
                break;
            case 6:
                enable_mode_desc.setDesc(en + f);
                break;
            case 7:
                enable_mode_desc.setDesc(en + g);
                break;
            default:
                enable_mode_desc.setDesc(en + h);
                break;
        }

        switch ((int) disable_mode.getValue()) {
            case 1:
                disable_mode_desc.setDesc(dn + a);
                break;
            case 2:
                disable_mode_desc.setDesc(dn + b);
                break;
            case 3:
                disable_mode_desc.setDesc(dn + c);
                break;
            case 4:
                disable_mode_desc.setDesc(dn + d);
                break;
            case 5:
                disable_mode_desc.setDesc(dn + e);
                break;
            case 6:
                disable_mode_desc.setDesc(dn + f);
                break;
            case 7:
                disable_mode_desc.setDesc(dn + g);
                break;
            default:
                enable_mode_desc.setDesc(dn + h);
                break;
        }

        switch ((int) category_mode.getValue()) {
            case 1:
                category_mode_desc.setDesc(cn + a);
                break;
            case 2:
                category_mode_desc.setDesc(cn + b);
                break;
            case 3:
                category_mode_desc.setDesc(cn + c);
                break;
            case 4:
                category_mode_desc.setDesc(cn + d);
                break;
            case 5:
                category_mode_desc.setDesc(cn + e);
                break;
            case 6:
                category_mode_desc.setDesc(cn + f);
                break;
            case 7:
                category_mode_desc.setDesc(cn + g);
                break;
            default:
                enable_mode_desc.setDesc(cn + h);
                break;
        }
    }

}