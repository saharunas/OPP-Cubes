package ethanjones.cubes.side.server.commands;

import ethanjones.cubes.core.localization.Localization;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.NetworkingManager;
import ethanjones.cubes.networking.packets.PacketPlayerSkinColor;
import ethanjones.cubes.side.server.command.*;

import java.util.List;

public class SkinCommand {

  public static void init() {
    CommandBuilder skin = new CommandBuilder("skin").register().setCommandPermission(CommandPermission.Basic);

    // /skin red
    skin.add("red").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Player player = (Player) sender;
          player.setSkinColor("red");
          sender.print("Skin color changed to RED");
          
          // Broadcast to all clients
          PacketPlayerSkinColor packet = new PacketPlayerSkinColor();
          packet.playerUUID = player.uuid;
          packet.skinColor = "red";
          NetworkingManager.sendPacketToAllClients(packet);
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });

    // /skin green
    skin.add("green").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Player player = (Player) sender;
          player.setSkinColor("green");
          sender.print("Skin color changed to GREEN");
          
          // Broadcast to all clients
          PacketPlayerSkinColor packet = new PacketPlayerSkinColor();
          packet.playerUUID = player.uuid;
          packet.skinColor = "green";
          NetworkingManager.sendPacketToAllClients(packet);
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });

    // /skin blue
    skin.add("blue").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Player player = (Player) sender;
          player.setSkinColor("blue");
          sender.print("Skin color changed to BLUE");
          
          // Broadcast to all clients
          PacketPlayerSkinColor packet = new PacketPlayerSkinColor();
          packet.playerUUID = player.uuid;
          packet.skinColor = "blue";
          NetworkingManager.sendPacketToAllClients(packet);
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });

    // /skin reset (back to default black)
    skin.add("reset").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Player player = (Player) sender;
          player.setSkinColor("default");
          sender.print("Skin color reset to DEFAULT");
          
          // Broadcast to all clients
          PacketPlayerSkinColor packet = new PacketPlayerSkinColor();
          packet.playerUUID = player.uuid;
          packet.skinColor = "default";
          NetworkingManager.sendPacketToAllClients(packet);
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });
  }

}
