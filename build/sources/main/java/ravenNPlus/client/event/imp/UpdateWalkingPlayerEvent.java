package ravenNPlus.client.event.imp;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import ravenNPlus.client.event.EventStage;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage {

  public UpdateWalkingPlayerEvent(int stage) {
    super(stage);
  }

}