package ravenNPlus.client.command.commands;

import ravenNPlus.client.command.Command;
import ravenNPlus.client.module.modules.combat.AimAssist;
import net.minecraft.entity.Entity;
import ravenNPlus.client.utils.Utils;

import static ravenNPlus.client.clickgui.RavenNPlus.CommandPrompt.print;

public class Friends extends Command {
    public Friends() {
        super("friends", "Allows you to manage and view your friends list", 1, 2, new String[]{"add / remove / list", "Player's name"}, new String[] {"f", "amigos", "lonely4ever", "bros"});
    }

    @Override
    public void onCall(String[] args) {
        if (args.length == 0) {
            listFriends();
        }

        else if(args[0].equalsIgnoreCase("list")) {
            listFriends();
        }

        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("add")) {
                boolean added = Utils.FriendUtils.addFriend(args[1]);
                if (added) {
                    print("Successfully added " + args[1] + " to your friends list!");
                    print("You have "+Utils.FriendUtils.getFriendCount()+" Friends");
                } else {
                    print("An error occurred!");
                }
            }
            else if(args[0].equalsIgnoreCase("remove")) {
                boolean removed = Utils.FriendUtils.removeFriend(args[1]);
                if (removed) {
                    print("Successfully removed " + args[1] + " from your friends list!");
                } else {
                    print("An error occurred!");
                }
            }
        }
        else {
            this.incorrectArgs();
        }
    }

    public void listFriends() {
        if(Utils.FriendUtils.getFriends().isEmpty()) {
            print("You have no friends. L");
        }
        else {
            print("Your friends are:");
            for (Entity entity : Utils.FriendUtils.getFriends()) {
                print(entity.getName());
            }
        }
    }

}