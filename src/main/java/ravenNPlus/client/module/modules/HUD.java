package ravenNPlus.client.module.modules;

import ravenNPlus.client.utils.*;
import ravenNPlus.client.module.*;
import ravenNPlus.client.main.Client;
import ravenNPlus.client.module.setting.impl.TickSetting;
import ravenNPlus.client.utils.ColorUtil;
import ravenNPlus.client.module.setting.impl.SliderSetting;
import ravenNPlus.client.module.modules.client.ModSettings;
import ravenNPlus.client.module.setting.impl.DescriptionSetting;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Comparator;

public class HUD extends Module {

    public static TickSetting editPosition, dropShadow, alphabeticalSort, costumColor, clientName, clientAuthor, background, ani;
    public static DescriptionSetting colourModeDesc;
    public static SliderSetting colourMode, red, green, blue;
    public static Utils.HUD.PositionMode positionMode;
    private static int hudX = 5, hudY = 70;
    public static boolean showedError, t;
    public static final String HUDX_prefix = "HUDX~ ", HUDY_prefix = "HUDY~ ";

    public HUD() {
        super("ArrayList", ModuleCategory.client, "Array Lists you all enabled Cheats");
        this.addSetting(colourMode = new SliderSetting("Color Mode: ", 1, 1, 5, 1));
        this.addSetting(colourModeDesc = new DescriptionSetting("Mode: " + colourMode.getName()));
        this.addSetting(editPosition = new TickSetting("Edit position", o));
        this.addSetting(ani = new TickSetting("Animations", x));
        this.addSetting(background = new TickSetting("Background", x));
        this.addSetting(dropShadow = new TickSetting("Drop shadow", x));
        this.addSetting(alphabeticalSort = new TickSetting("Alphabetical sort", o));
        this.addSetting(clientName = new TickSetting("Client Name", x));
        this.addSetting(clientAuthor = new TickSetting("Client Author", o));
        this.addSetting(costumColor = new TickSetting("Costum Color", o));
        this.addSetting(red = new SliderSetting("Color Red", 0, 0, 255, 1));
        this.addSetting(green = new SliderSetting("Color Green", 0, 0, 255, 1));
        this.addSetting(blue = new SliderSetting("Color Blue", 0, 0, 255, 1));
        showedError = false;
        this.showInHud = false;
    }

    public void guiUpdate() {
        colourModeDesc.setDesc(Utils.md + ColourModes.values()[colourMode.getValueToInt() - 1]);
    }

    public void onEnable() {
        Client.moduleManager.sort();
    }

    public void guiButtonToggled(TickSetting b) {
        if (b == editPosition) {
            editPosition.disable();
            mc.displayGuiScreen(new EditHudPositionScreen());
        } else if (b == alphabeticalSort) {
            Client.moduleManager.sort();
        }
    }

    @SubscribeEvent
    public void a(RenderTickEvent ev) {
        if (ev.phase == Phase.END && this.inGame()) {
            if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
                return;
            }

            // sort
            int margin = 2, y = hudY, del = 0;
            if (!alphabeticalSort.isToggled()) {
                if (positionMode == Utils.HUD.PositionMode.UPLEFT || positionMode == Utils.HUD.PositionMode.UPRIGHT) {
                    Client.moduleManager.sortShortLong();
                } else if (positionMode == Utils.HUD.PositionMode.DOWNLEFT || positionMode == Utils.HUD.PositionMode.DOWNRIGHT) {
                    Client.moduleManager.sortLongShort();
                }
            }

            // client name
            if (clientName.isToggled())
                if (ModSettings.costumFont.isToggled()) {
                    FontUtils.comfortaa(Client.name, 5, 5, Utils.Client.rainbowDraw(2L, 1500L));
                } else {
                    mc.fontRendererObj.drawString(Client.name, 5, 5, Utils.Client.rainbowDraw(2L, 1500L));
                }

            // client author
            if (clientAuthor.isToggled()) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.8, 0.8, 0.8);
                if (ModSettings.costumFont.isToggled()) {
                    FontUtils.comfortaa("by " + Client.author, 13, 18, Utils.Client.rainbowDraw(2L, 1500L));
                } else {
                    mc.fontRendererObj.drawString("by " + Client.author, 13, 18, Utils.Client.rainbowDraw(2L, 1500L));
                    GlStateManager.popMatrix();
                }
            }

            // variables
            List<Module> en = new ArrayList<>(Client.moduleManager.getModules());
            if (en.isEmpty()) return;

            int textBoxWidth = Client.moduleManager.getLongestActiveModule(mc.fontRendererObj);
            int textBoxHeight = Client.moduleManager.getBoxHeight(mc.fontRendererObj, margin);

            if (hudX < 0) {
                hudX = margin;
            }

            if (hudY < 0) { {
                    hudY = margin;
                } }

            if (hudX + textBoxWidth > mc.displayWidth / 2) {
                hudX = mc.displayWidth / 2 - textBoxWidth - margin;
            }

            if (hudY + textBoxHeight > mc.displayHeight / 2) {
                hudY = mc.displayHeight / 2 - textBoxHeight;
            }

            for (Module m : en) {
                int m_width = mc.fontRendererObj.getStringWidth(m.getName()), m_height = mc.fontRendererObj.FONT_HEIGHT;

                // animation disable
                if (!ani.isToggled())
                    m.arrayInteger = m_width;

                // animation start
                if (m.isEnabled() && ani.isToggled()) {
                    if (m.arrayInteger < m_width)
                        m.arrayInteger++;
                }

                // animation reset
                // the reset is in the m.disable();

                if (m.isEnabled() && m.showInHud() && m.showinArrayList.isToggled()) {

                    // background
                    if (background.isToggled()) {
                        int backGroundOff = 1;
                        Gui.drawRect(hudX - (backGroundOff * 2), y - backGroundOff, hudX + backGroundOff + m.arrayInteger, y + m_height + backGroundOff, ColorUtil.color_int_min);
                    }

                    // variables
                    int rgb;
                    if (!HUD.costumColor.isToggled())
                        rgb = Utils.Client.rainbowDraw(2L, del);
                    else
                        rgb = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();

                    // string options
                    if (HUD.positionMode == Utils.HUD.PositionMode.DOWNRIGHT || HUD.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
                        float ff = hudX + (textBoxWidth - m_width);

                        if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.RAVENB) {
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, y, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, (float) y, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 120;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.RAVENB2) {
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, y, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, (float) y, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 10;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.ASTOLFO) {
                            if (!HUD.costumColor.isToggled())
                                rgb = Utils.Client.astolfoColorsDraw(10, 14);
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, y, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, (float) y, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 120;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.ASTOLFO2) {
                            if (!HUD.costumColor.isToggled())
                                rgb = Utils.Client.astolfoColorsDraw(10, del);
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, y, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, (float) y, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 120;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.ASTOLFO3) {
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, y, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, (float) y, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 10;
                        }
                    } else {
                        float ff = m.arrayInteger-m_width+hudX, tt = y+1;

                        if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.RAVENB) {
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, (int) tt, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, tt, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 120;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.RAVENB2) {
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, (int) tt, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, tt, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 10;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.ASTOLFO) {
                            if (!HUD.costumColor.isToggled())
                                rgb = Utils.Client.astolfoColorsDraw(10, 14);
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, (int) tt, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, tt, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 120;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.ASTOLFO2) {
                            if (!HUD.costumColor.isToggled())
                                rgb = Utils.Client.astolfoColorsDraw(10, del);
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, (int) tt, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, tt, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 120;
                        } else if (ColourModes.values()[(int) colourMode.getValue() - 1] == ColourModes.ASTOLFO3) {
                            if (!HUD.costumColor.isToggled())
                                rgb = Utils.Client.astolfoColorsDraw(10, del);
                            if (ModSettings.costumFont.isToggled()) {
                                FontUtils.comfortaa(m.getName(), (int) ff, (int) tt, rgb, dropShadow.isToggled());
                            } else {
                                mc.fontRendererObj.drawString(m.getName(), ff, tt, rgb, dropShadow.isToggled());
                            }
                            y += m_height + margin;
                            del -= 10;
                        }
                    }
                    // ---
                }
            }
        }
    }

    static class EditHudPositionScreen extends GuiScreen {
        final String hudTextExample = "Example-PlayerESP-KillAura-Scaffold-InvMove-AntiVoid-Velocity-Sprint-";
        GuiButtonExt resetPosButton;
        ScaledResolution sr;
        boolean mouseDown = false;
        int textBoxStartX = 0, textBoxStartY = 0, textBoxEndX = 0, textBoxEndY = 0;
        int marginX = 5, marginY = 70, lastMousePosX = 0, lastMousePosY = 0, sessionMousePosX = 0, sessionMousePosY = 0;

        public void initGui() {
            super.initGui();
            this.buttonList.add(this.resetPosButton = new GuiButtonExt(1, this.width - 90, 5, 85, 20, "Reset Position"));
            this.marginX = HUD.hudX;
            this.marginY = HUD.hudY;
            sr = new ScaledResolution(mc);
            HUD.positionMode = Utils.HUD.getPostitionMode(marginX, marginY, sr.getScaledWidth(), sr.getScaledHeight());
        }

        public void drawScreen(int mX, int mY, float pt) {
            drawRect(0, 0, this.width, this.height, -1308622848);
            drawRect(0, this.height / 2, this.width, this.height / 2 + 1, 0x9936393f);
            drawRect(this.width / 2, 0, this.width / 2 + 1, this.height, 0x9936393f);
            int textBoxStartX = this.marginX;
            int textBoxStartY = this.marginY;
            int textBoxEndX = textBoxStartX + 50;
            int textBoxEndY = textBoxStartY + 32;
            this.drawArrayList(this.mc.fontRendererObj, this.hudTextExample);
            this.textBoxStartX = textBoxStartX;
            this.textBoxStartY = textBoxStartY;
            this.textBoxEndX = textBoxEndX;
            this.textBoxEndY = textBoxEndY;
            HUD.hudX = textBoxStartX;
            HUD.hudY = textBoxStartY;
            ScaledResolution res = new ScaledResolution(this.mc);
            int descriptionOffsetX = res.getScaledWidth() / 2 - 84;
            int descriptionOffsetY = res.getScaledHeight() / 2 - 20;
            fontRendererObj.drawStringWithShadow("Edit the position by dragging.", descriptionOffsetX, descriptionOffsetY, Utils.Client.astolfoColorsDraw(10, 28, 4890));
            try {
                this.handleInput();
            } catch (IOException ignored) {
            }
            super.drawScreen(mX, mY, pt);
        }

        private void drawArrayList(FontRenderer fr, String t) {
            int x = this.textBoxStartX, gap = this.textBoxEndX - this.textBoxStartX, y = this.textBoxStartY;
            double marginY = fr.FONT_HEIGHT + 2;
            String[] var4 = t.split("-");
            ArrayList<String> var5 = Utils.Java.toArrayList(var4);
            if (HUD.positionMode == Utils.HUD.PositionMode.UPLEFT || HUD.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
                var5.sort((o1, o2) -> Utils.mc.fontRendererObj.getStringWidth(o2) - Utils.mc.fontRendererObj.getStringWidth(o1));
            } else if (HUD.positionMode == Utils.HUD.PositionMode.DOWNLEFT || HUD.positionMode == Utils.HUD.PositionMode.DOWNRIGHT) {
                var5.sort(Comparator.comparingInt(o2 -> Utils.mc.fontRendererObj.getStringWidth(o2)));
            }
            if (HUD.positionMode == Utils.HUD.PositionMode.DOWNRIGHT || HUD.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
                for (String s : var5) {
                    fr.drawString(s, (float) x + (gap - fr.getStringWidth(s)), (float) y, Color.white.getRGB(), HUD.dropShadow.isToggled());
                    y += marginY;
                }
            } else {
                for (String s : var5) {
                    fr.drawString(s, (float) x, (float) y, Color.white.getRGB(), HUD.dropShadow.isToggled());
                    y += marginY;
                }
            }
        }

        protected void mouseClickMove(int mousePosX, int mousePosY, int clickedMouseButton, long timeSinceLastClick) {
            super.mouseClickMove(mousePosX, mousePosY, clickedMouseButton, timeSinceLastClick);
            if (clickedMouseButton == 0) {
                if (this.mouseDown) {
                    this.marginX = this.lastMousePosX + (mousePosX - this.sessionMousePosX);
                    this.marginY = this.lastMousePosY + (mousePosY - this.sessionMousePosY);
                    sr = new ScaledResolution(mc);
                    HUD.positionMode = Utils.HUD.getPostitionMode(marginX, marginY, sr.getScaledWidth(), sr.getScaledHeight());

                    //in the else if statement, we check if the mouse is clicked AND inside the "text box"
                } else if (mousePosX > this.textBoxStartX && mousePosX < this.textBoxEndX && mousePosY > this.textBoxStartY && mousePosY < this.textBoxEndY) {
                    this.mouseDown = true;
                    this.sessionMousePosX = mousePosX;
                    this.sessionMousePosY = mousePosY;
                    this.lastMousePosX = this.marginX;
                    this.lastMousePosY = this.marginY;
                }
            }
        }

        protected void mouseReleased(int mX, int mY, int state) {
            super.mouseReleased(mX, mY, state);
            if (state == 0) {
                this.mouseDown = false;
            }
        }

        public void actionPerformed(GuiButton b) {
            if (b == this.resetPosButton) {
                this.marginX = HUD.hudX = 5;
                this.marginY = HUD.hudY = 70;
            }
        }

        public boolean doesGuiPauseGame() {
            return false;
        }
    }

    public enum ColourModes {RAVENB, RAVENB2, ASTOLFO, ASTOLFO2, ASTOLFO3, KOPAMED}

    public static int getHudX() {
        return hudX;
    }

    public static int getHudY() {
        return hudY;
    }

    public static void setHudX(int hudX) {
        HUD.hudX = hudX;
    }

    public static void setHudY(int hudY) {
        HUD.hudY = hudY;
    }

}