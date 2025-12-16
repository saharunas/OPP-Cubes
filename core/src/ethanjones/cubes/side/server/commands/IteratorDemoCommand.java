package ethanjones.cubes.side.server.commands;

import ethanjones.cubes.core.util.RunnableQueue;
import ethanjones.cubes.core.util.iterator.IteratorClient;
import ethanjones.cubes.core.util.iterator.PacketQueueCollection;
import ethanjones.cubes.core.util.iterator.PriorityPacketQueueCollection;
import ethanjones.cubes.core.util.iterator.RunnableQueueCollection;
import ethanjones.cubes.networking.packet.PacketQueue;
import ethanjones.cubes.networking.packet.PriorityPacketQueue;
import ethanjones.cubes.networking.packets.PacketPingReply;
import ethanjones.cubes.networking.packets.PacketPingRequest;
import ethanjones.cubes.side.server.command.*;

import java.util.List;

public class IteratorDemoCommand {

    public static void init() {
        new CommandBuilder("iterdemo")
                .register()
                .setCommandPermission(CommandPermission.All)
                .setCommandListener(new CommandListener() {
                    @Override
                    public void onCommand(CommandBuilder builder, List<CommandArgument> arguments, CommandSender sender) {

                        // 1) LinkedBlockingQueue backing structure
                        PacketQueue packetQueue = new PacketQueue();
                        packetQueue.add(new PacketPingRequest());
                        packetQueue.add(new PacketPingReply());

                        // 2) PriorityBlockingQueue backing structure
                        PriorityPacketQueue priorityPacketQueue = new PriorityPacketQueue();
                        priorityPacketQueue.add(new PacketPingRequest());
                        priorityPacketQueue.add(new PacketPingReply());

                        // 3) ConcurrentLinkedQueue backing structure
                        RunnableQueue runnableQueue = new RunnableQueue();
                        runnableQueue.add(new Runnable() {
                            @Override
                            public void run() {
                                // no-op (demo only)
                            }
                        });
                        runnableQueue.add(new Runnable() {
                            @Override
                            public void run() {
                                // no-op (demo only)
                            }
                        });

                        sender.print("Iterator demo: iteruojama per 3 skirtingas duomenų struktūras");
                        sender.print("- PacketQueue (LinkedBlockingQueue)");
                        printLines(sender, IteratorClient.inspectAll(new PacketQueueCollection(packetQueue), 20));

                        sender.print("- PriorityPacketQueue (PriorityBlockingQueue)");
                        printLines(sender, IteratorClient.inspectAll(new PriorityPacketQueueCollection(priorityPacketQueue), 20));

                        sender.print("- RunnableQueue (ConcurrentLinkedQueue)");
                        printLines(sender, IteratorClient.inspectAll(new RunnableQueueCollection(runnableQueue), 20));
                    }
                });
    }

    public static void autoRun() {
        System.out.println("===== ITERATOR PATTERN AUTO DEMO =====");

        // 1) LinkedBlockingQueue
        PacketQueue packetQueue = new PacketQueue();
        packetQueue.add(new PacketPingRequest());
        packetQueue.add(new PacketPingReply());

        // 2) PriorityBlockingQueue
        PriorityPacketQueue priorityPacketQueue = new PriorityPacketQueue();
        priorityPacketQueue.add(new PacketPingRequest());
        priorityPacketQueue.add(new PacketPingReply());

        // 3) ConcurrentLinkedQueue
        RunnableQueue runnableQueue = new RunnableQueue();
        runnableQueue.add(new Runnable() {
            @Override
            public void run() {
            }
        });

        runnableQueue.add(new Runnable() {
            @Override
            public void run() {
            }
        });

        System.out.println("-- PacketQueue (LinkedBlockingQueue)");
        printLinesStdout(
                IteratorClient.inspectAll(new PacketQueueCollection(packetQueue), 20)
        );

        System.out.println("-- PriorityPacketQueue (PriorityBlockingQueue)");
        printLinesStdout(
                IteratorClient.inspectAll(new PriorityPacketQueueCollection(priorityPacketQueue), 20)
        );

        System.out.println("-- RunnableQueue (ConcurrentLinkedQueue)");
        printLinesStdout(
                IteratorClient.inspectAll(new RunnableQueueCollection(runnableQueue), 20)
        );

        System.out.println("===== END ITERATOR DEMO =====");
    }

    private static void printLinesStdout(List<String> lines) {
        for (String line : lines) {
            System.out.println("  " + line);
        }
    }

    private static void printLines(CommandSender sender, List<String> lines) {
        for (String line : lines) {
            sender.print("  " + line);
        }
    }
}