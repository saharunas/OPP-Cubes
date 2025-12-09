package ethanjones.cubes.side.server.commands;

import ethanjones.cubes.core.localization.Localization;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.side.server.command.*;

import java.util.List;
import java.util.Set;

public class InventoryCommand {

    public static void init() {
        CommandBuilder inventory = new CommandBuilder("inventory")
                .register()
                .setCommandPermission(CommandPermission.Extended);

        // /inventory save <name>
        inventory.add("save")
                .add(CommandValue.stringValue)
                .setCommandListener(new CommandListener() {
                    @Override
                    public void onCommand(CommandBuilder builder, List<CommandArgument> arguments,
                            CommandSender sender) {
                        if (sender instanceof Player) {
                            String name = (String) arguments.get(2).get(); // ✅ use index 2
                            Player player = (Player) sender;
                            player.saveInventorySnapshot(name);
                            sender.print(Localization.get("command.inventory.save", name));
                        } else {
                            sender.print(Localization.get("command.common.onlyPlayer"));
                        }
                    }
                });

        // /inventory load <name>
        inventory.add("load")
                .add(CommandValue.stringValue)
                .setCommandListener(new CommandListener() {
                    @Override
                    public void onCommand(CommandBuilder builder, List<CommandArgument> arguments,
                            CommandSender sender) {
                        if (sender instanceof Player) {
                            String name = (String) arguments.get(2).get(); // ✅ use index 2
                            Player player = (Player) sender;
                            if (player.loadInventorySnapshot(name)) {
                                sender.print(Localization.get("command.inventory.load", name));
                            } else {
                                sender.print(Localization.get("command.inventory.notFound", name));
                            }
                        } else {
                            sender.print(Localization.get("command.common.onlyPlayer"));
                        }
                    }
                });

        // /inventory list
        inventory.add("list")
                .setCommandListener(new CommandListener() {
                    @Override
                    public void onCommand(CommandBuilder builder, List<CommandArgument> arguments,
                            CommandSender sender) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            Set<String> names = player.getInventorySnapshotNames();
                            if (names.isEmpty()) {
                                sender.print(Localization.get("command.inventory.list.empty"));
                            } else {
                                sender.print(Localization.get("command.inventory.list.header"));
                                for (String name : names) {
                                    sender.print(" - " + name);
                                }
                            }
                        } else {
                            sender.print(Localization.get("command.common.onlyPlayer"));
                        }
                    }
                });
    }
}
