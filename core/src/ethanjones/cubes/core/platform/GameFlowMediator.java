package ethanjones.cubes.core.platform;

import ethanjones.cubes.world.save.Gamemode;
import ethanjones.cubes.world.save.Save;

public interface GameFlowMediator {

    // Menu navigation
    void openSingleplayerRootMenu();

    void openMultiplayerMenu();

    void openSingleplayerSaveCreateMenu();

    // Singleplayer world start flows
    void startTemporarySingleplayer(String generatorId, Gamemode gamemode, String seed);

    void createAndStartSingleplayerSave(String name, String generatorId, Gamemode gamemode, String seed);

    void startSingleplayerFromSave(Save save);
}
