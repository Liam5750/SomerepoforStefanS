package ravenNPlus.client.clickgui.RavenNPlus;

import ravenNPlus.client.main.Client;
import ravenNPlus.client.utils.ColorUtil;
import ravenNPlus.client.utils.CoolDown;
import ravenNPlus.client.utils.RoundedUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.util.ArrayList;

public class CommandPrompt implements Component {

    public static int titleColor = 0xff088000;
    private int x, y, width, height, barHeight, border;
    private int minWidth, minHeight, resizeButtonSize;
    public boolean opened = false, hidden = false;
    private boolean resizing = false, focused = false;
    private CoolDown keyDown = new CoolDown(500);
    private int backCharsCursor = 0;
    public final int[] acceptableKeycodes = {41,0,2,3,4,5,6,7,8,9,10,11,12,13,26,27,39,40,0,51,52,53,41,145,144,147,146,57,16,17,18,19,20,21,22,23,24,25,30,31,32,33,34,35,36,37,38,44,45,46,47,48,49,50};
    private final Minecraft mc;
    private final FontRenderer fr;
    private String inputText = "discord";
    private static ArrayList<String> out = new ArrayList<>();
    private final String prefix = ". ";
    private boolean dragging = false;
    private double windowStartDragX, windowStartDragY, mouseStartDragX, mouseStartDragY;

    public CommandPrompt() {
        this.x = 0; this.y = 0; this.width = 300; this.minWidth = 125; this.height = 214;
        this.minHeight = 37; this.barHeight = 13; this.mc = Minecraft.getMinecraft();
        this.fr = mc.fontRendererObj; this.border = 1; this.resizeButtonSize = 6;
    }

    public static void clear() { out.clear(); }
    public void show() { this.hidden = false; }
    public void hide() { this.hidden = true; }

    @Override
    public void draw() {

        if(hidden) return;
        //this.barHeight = mc.displayHeight / 90;
        double desiredTextSize = barHeight * 0.65;
        double scaleFactor = desiredTextSize/ fr.FONT_HEIGHT;
        double coordFactor = 1/scaleFactor;
        double margin = (int)((barHeight - desiredTextSize) * 0.8);
        float textY = (float) ((y + margin) * coordFactor);
        float textX = (float) ((x + margin) * coordFactor);
        float buttonX = (float) ((x + width - margin - fr.getStringWidth(opened ? "-" : "+")) * coordFactor);
        int cursorX = 0;
        int cursorY = 0;
        float outStartX = (float) ((x + margin + border));
        float outFinishX = (float) ((x + width - (margin + border)));
        float outStartY = (float) ((y + barHeight + margin));
        float outFinishY = (float) ((y + height - margin - border));
        float maxTextWidth = outFinishX - outStartX;
        int maxLines = Math.floorDiv((int) (outFinishY - outStartY), (int) (desiredTextSize + margin));
        int linesPrinted = 0;
        cursorX = (int) outStartX;
        outStartX *= coordFactor;
        outFinishX *= coordFactor;
        outStartY *= coordFactor;
        outFinishY *= coordFactor;

        Gui.drawRect(x, y, x + width, y + barHeight, ColorUtil.color_commandPrompt1);

        if(opened) {
            Gui.drawRect(x,
                    y + barHeight,
                    x + width,
                    y + height,
                    ColorUtil.color_commandPrompt3
            );
            Gui.drawRect(x,
                    y + barHeight,
                    x + border,
                    y + height,
                    ColorUtil.color_commandPrompt1
            );
            Gui.drawRect(x + width,
                    y + barHeight,
                    x + width - border,
                    y + height,
                    ColorUtil.color_commandPrompt1
            );
            Gui.drawRect(x,
                    y + height - border,
                    x + width,
                    y + height,
                    ColorUtil.color_commandPrompt1
            );

            //resize button
            RoundedUtils.drawSmoothRoundedRect(x + width - resizeButtonSize,
                    y + height - resizeButtonSize-1,
                    x + width-1,
                    y + height-1, 2,
                    ColorUtil.color_commandPrompt2
            );
        }

        GL11.glPushMatrix();
        GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
        fr.drawString(
                "Command Prompt 2022",
                textX,
                textY,
                titleColor, //here
                false
        );
        fr.drawString(
                //        -     +
                opened ? "x" : "-",
                buttonX,
                textY,
                ColorUtil.color_full,
                false
        );
        if(opened) {
            ArrayList<String> currentOut = new ArrayList<>(out);
            currentOut.add(prefix + inputText);
            String currentLine;
            ArrayList<String> finalOut = new ArrayList<>();
            int end = currentOut.size() >= maxLines ? currentOut.size() - maxLines : 0;
            for (int j = currentOut.size() - 1; j >= end; j--) {
                currentLine = currentOut.get(j);
                String[] splitUpLine = splitUpLine(currentLine, maxTextWidth, scaleFactor);
                for(int i  = splitUpLine.length - 1; i >= 0; i--) {
                    if (linesPrinted >= maxLines) {
                        break;
                    }
                    finalOut.add(splitUpLine[i]);
                    linesPrinted++;
                }
            }

            String[] inputTextLineSplit = splitUpLine(prefix + inputText.substring(0, inputText.length() - backCharsCursor), maxTextWidth, scaleFactor);
            String finalInputLine = inputTextLineSplit[inputTextLineSplit.length - 1];
            cursorX += (int) (fr.getStringWidth(finalInputLine) * scaleFactor);

            for (int j = finalOut.size() - 1; j >= 0; j--) {
                currentLine = finalOut.get(j);
                int topMargin = (int) (
                        ((finalOut.size() - 1 - j) * (desiredTextSize + margin) * coordFactor)
                                + outStartY
                );
                fr.drawString(
                        currentLine,
                        (int) outStartX,
                        topMargin,
                        new Color(32,194,14).getRGB()
                );
                if(currentLine.startsWith(finalInputLine))
                    cursorY = (int) (topMargin / coordFactor);
            }
        }
        GL11.glPopMatrix();
        if(opened) {
            Gui.drawRect(
                    cursorX,
                    cursorY,
                    cursorX + 1,
                    (int) (cursorY + desiredTextSize),
                    ColorUtil.color_full
            );
        }
    }

    private String[] splitUpLine(String currentLine, float maxTextWidth, double scaleSize) {
        if(fr.getStringWidth(currentLine) * scaleSize <= maxTextWidth) {
            return new String[] {currentLine};
        } else {
            for(int i = currentLine.length(); i >= 0; i--) {
                String newLine = currentLine.substring(0, i);
                if(fr.getStringWidth(newLine) * scaleSize <= maxTextWidth) {
                    return mergeArray(
                            new String[]{newLine},
                            splitUpLine(currentLine.substring(i, currentLine.length()), maxTextWidth, scaleSize)
                    );
                }
            }
        }

        return new String[]{""};
    }

    public static void print(String message) { out.add(message); }
    public static String[] mergeArray(String[] arr1, String[] arr2) {  return  ArrayUtils.addAll(arr1, arr2); }

    @Override
    public void update(int x, int y) {
        if(hidden) return;
        if(dragging) {
            this.x = (int) (windowStartDragX + (x - mouseStartDragX));
            this.y = (int) (windowStartDragY + (y - mouseStartDragY));
        } else if(resizing) {
            int newWidth = Math.max(x, this.x + minWidth) - this.x;
            int newHeight = Math.max(y, this.y + minHeight) - this.y;
            this.width = newWidth;
            this.height = newHeight;
        }
    }

    @Override
    public void mouseDown(int x, int y, int b) {
        focused = false;
        if(hidden) return;
        if(overToggleButton(x, y) && b == 0) {
            this.opened = !opened;
        } else if(overBar(x, y)) {
            if(b == 0) {
                dragging = true;
                mouseStartDragX = x;
                mouseStartDragY = y;
                windowStartDragX = this.x;
                windowStartDragY = this.y;
            } else if(b==1) {
                this.opened = !opened;
            }
        } else if (overResize(x, y) && b == 0) {
            this.resizing = true;
        } else if(overWindow(x, y) && b == 0) {
            this.focused = true;
        }
    }

    @Override
    public void mouseReleased(int x, int y, int m) {
        if(hidden) return;
        if(dragging) {
            dragging = false;
        } else if (resizing) {
            resizing = false;
        }
    }

    @Override
    public void keyTyped(char t, int k) {
        if(!focused) return;

        if(k == 28) { //enter
            out.add(prefix + inputText);
            proccessInput();
            inputText = "";
            backCharsCursor=0;
        } else if(k == 14) { //backspace
            if(inputText.substring(0, inputText.length() - backCharsCursor).length() > 0) {
                if(backCharsCursor == 0) {
                    inputText = inputText.substring(0, inputText.length() - 1);
                } else {
                    String deletable = inputText.substring(0, inputText.length() - backCharsCursor);
                    String appendable = inputText.substring(inputText.length() - backCharsCursor, inputText.length());
                    if(deletable.length() > 0) {
                        deletable = deletable.substring(0, deletable.length() - 1);
                    }
                    inputText = deletable + appendable;
                }
            }
        } else if(k == 15) { // tab
            addCharToInput("    ");
        } else if(k == 203) {
            if(backCharsCursor < inputText.length()) {
                backCharsCursor++;
            }
        } else if(k == 205) {
            if(backCharsCursor > 0) {
                backCharsCursor--;
            }
        } else {
            if (!containsElement(acceptableKeycodes, k)) {
                return;
            }
            String e = String.valueOf(t);
            if(!e.isEmpty())
                addCharToInput(e);
        }
        /*/
        /  - up arrow 200
        /  - down 208
        /  - left 203
        /  - right 205
        /  - enter 28
        /  - backspace 14
        /*/
    }

    private boolean containsElement(int[] acceptableKeycodes, int k) {
        for(int i : acceptableKeycodes) {
            if(i == k)
                return true;
        }
        return false;
    }

    private void addCharToInput(String e) {
        if(backCharsCursor == 0) {
            inputText += e;
        } else {
            String deletable = inputText.substring(0, inputText.length() - backCharsCursor);
            String appendable = inputText.substring(inputText.length() - backCharsCursor, inputText.length());
            inputText = deletable + e + appendable;
        }
    }

    private void proccessInput() {
        if (!inputText.isEmpty()) {
            try{
                String command = inputText.split(" ")[0];
                boolean hasArgs = inputText.contains(" ");
                String[] args = hasArgs ? inputText.substring(command.length() + 1, inputText.length()).split(" ") : new String[0];

                Client.commandManager.executeCommand(command, args);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    @Override
    public void setComponentStartAt(int n) { }

    @Override
    public int getHeight() {
        return height;
    }

    public boolean overPosition(int x, int y) {
        if(hidden) return false;
        return opened ? overWindow(x, y) : overBar(x, y);
    }

    public boolean overBar(int x, int y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + barHeight;
    }

    public boolean overWindow(int x, int y) {
        if(!opened) return false;
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }

    public boolean overResize(int x, int y) {
        return x >= this.x + width - resizeButtonSize && x <= this.x + width && y >= this.y + height - resizeButtonSize && y <= this.y + height;
    }

    public boolean overToggleButton(int x, int y) {
        return x >= this.x + width - barHeight && x <= this.x + width && y >= this.y && y <= this.y + barHeight;
    }

    public void setLocation(int x, int y) { this.x = x; this.y = y; }
    public void setSize(int width, int height) { this.width = width; this.height = height; }
    public boolean hidden() { return hidden; }
    public int getY() { return y; }
    public int getX() { return x; }
    public int getWidth() { return width; }

}