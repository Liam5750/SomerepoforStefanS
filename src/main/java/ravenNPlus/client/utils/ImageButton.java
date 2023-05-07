package ravenNPlus.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import java.awt.*;

public class ImageButton {

    protected ResourceLocation image;
    protected int x, y, width, height, ani = 0, target;
    protected String description;
    protected Minecraft mc;

    public ImageButton(ResourceLocation image, int x, int y, int width, int height, String description, int target) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.description = description;
        this.target = target;
        this.mc = Minecraft.getMinecraft();
    }

    public void draw(int mouseX, int mouseY, Color c) {
        this.hoverAnimation(mouseX, mouseY);
        if(this.ani > 0) {
            RenderUtils.draw2DImage(this.image, this.x-this.ani, this.y-this.ani, this.width+this.ani*2, this.height+this.ani*2, c);

            double descWidth = mc.fontRendererObj.getStringWidth(description);
            Gui.drawRect(this.x - (int)descWidth/2, this.y+this.height+10, this.x+this.width+(int) descWidth/2,
                    this.y+this.height+10+mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            mc.fontRendererObj.drawString(this.description, this.x+this.width/2-this.mc.fontRendererObj.getStringWidth(this.description)/2,
                    this.y+this.height+11, Color.white.getRGB());
        } else {
            RenderUtils.draw2DImage(this.image, this.x, this.y, this.width, this.height, c);
        }
    }

    public void onClick(int mouseX, int mouseY) {
        if(this.isHovered(mouseX, mouseY)) {
            if(this.target == 1) {
                mc.shutdown();
            }
        }
    }

    protected void hoverAnimation(int mouseX, int mouseY) {
        if(this.isHovered(mouseX, mouseY)) {
            if (this.ani < 5)
                this.ani++;
        } else {
            if(this.ani > 0)
                this.ani--;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return RenderUtils.isHovered(this.x, this.y, this.x+this.width, this.y+this.height, mouseX, mouseY);
    }

}