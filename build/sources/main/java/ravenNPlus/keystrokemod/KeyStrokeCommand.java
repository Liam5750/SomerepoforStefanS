package ravenNPlus.keystrokemod;

import net.minecraft.command.ICommandSender;

public class KeyStrokeCommand extends net.minecraft.command.CommandBase {
   public String getCommandName() {
      return "key";
   }

   public void processCommand(ICommandSender sender, String[] args) {
      KeyStrokeMod.toggleKeyStrokeConfigGui();
   }

   public String getCommandUsage(ICommandSender sender) {
      return "/key";
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }

   public boolean canCommandSenderUseCommand(ICommandSender sender) {
      return true;
   }

}