package ethanjones.cubes.side.server.commands;

import ethanjones.cubes.block.Block;
import ethanjones.cubes.block.BlockDebugVisitor;
import ethanjones.cubes.block.Blocks;
import ethanjones.cubes.core.id.IDManager;
import ethanjones.cubes.side.server.command.CommandArgument;
import ethanjones.cubes.side.server.command.CommandBuilder;
import ethanjones.cubes.side.server.command.CommandListener;
import ethanjones.cubes.side.server.command.CommandPermission;
import ethanjones.cubes.side.server.command.CommandSender;
import ethanjones.cubes.side.server.command.CommandValue;
import ethanjones.cubes.core.id.IDManager;

import java.util.List;

public class BlockInfoCommand {

    public static void init() {
        CommandBuilder blockinfo = new CommandBuilder("blockinfo")
                .register()
                .setCommandPermission(CommandPermission.Extended);

        // /blockinfo <blockId>
        blockinfo.add(CommandValue.stringValue)
                .setCommandListener(new CommandListener() {
                    @Override
                    public void onCommand(CommandBuilder builder, List<CommandArgument> arguments,
                            CommandSender sender) {
                        // arguments.get(0) is "blockinfo"; arguments.get(1) is the string value
                        String id = (String) arguments.get(1).get();

                        Block block = IDManager.toBlock(id);
                        if (block == null) {
                            sender.print("No block with id '" + id + "'");
                            return;
                        }

                        String info = block.accept(new BlockDebugVisitor());
                        sender.print(info);
                    }
                });
    }
}
