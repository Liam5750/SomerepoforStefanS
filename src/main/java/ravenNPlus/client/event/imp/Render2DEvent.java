package ravenNPlus.client.event.imp;

import net.minecraft.client.gui.ScaledResolution;
import ravenNPlus.client.event.EventStage;

public class Render2DEvent extends EventStage {

  public float partialTicks;
  public ScaledResolution scaledResolution;
  
  public Render2DEvent(float partialTicks, ScaledResolution scaledResolution) {
    this.partialTicks = partialTicks;
    this.scaledResolution = scaledResolution;
  }
  
  public void setPartialTicks(float partialTicks) {
    this.partialTicks = partialTicks;
  }
  
  public void setScaledResolution(ScaledResolution scaledResolution) {
    this.scaledResolution = scaledResolution;
  }
  
  public double getScreenWidth() {
    return this.scaledResolution.getScaledWidth();
  }
  
  public double getScreenHeight() {
    return this.scaledResolution.getScaledHeight();
  }

}