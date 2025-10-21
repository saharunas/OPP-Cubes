package ethanjones.cubes.side.server.commands;

import java.util.List;

import ethanjones.cubes.core.localization.Localization;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.side.common.Side;
import ethanjones.cubes.side.server.command.*;
import ethanjones.cubes.world.save.Gamemode;

public class GamemodeCommand {

    public static void init() {
    CommandBuilder gamemode = new CommandBuilder("gamemode").register().setCommandPermission(CommandPermission.Extended);

    gamemode.add("survival").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Side.getCubes().getClient().setGamemode(Gamemode.survival);
          ((Player) sender).setNoClip(false);
          sender.print(Localization.get("command.gamemode.survival"));
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });

    gamemode.add("creative").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Side.getCubes().getClient().setGamemode(Gamemode.creative);
          ((Player) sender).setNoClip(false);
          sender.print(Localization.get("command.gamemode.creative"));
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });

    gamemode.add("spectator").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Side.getCubes().getClient().setGamemode(Gamemode.spectator);
          ((Player) sender).setNoClip(true);
          sender.print(Localization.get("command.gamemode.spectator"));
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });

    gamemode.add("adventure").setCommandListener(new CommandListener() {
      @Override
      public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {
        if (sender instanceof Player) {
          Side.getCubes().getClient().setGamemode(Gamemode.adventure);
          ((Player) sender).setNoClip(false);
          sender.print(Localization.get("command.gamemode.adventure"));
        } else {
          sender.print(Localization.get("command.common.onlyPlayer"));
        }
      }
    });
  }
}
