package ethanjones.cubes.core.platform;

import ethanjones.cubes.graphics.menus.InfoMenu;
import ethanjones.cubes.graphics.menus.MultiplayerConnectMenu;
import ethanjones.cubes.graphics.menus.SingleplayerSaveCreateMenu;
import ethanjones.cubes.graphics.menus.SingleplayerLoadingMenu;
import ethanjones.cubes.graphics.menus.SingleplayerSavesMenu;
import ethanjones.cubes.graphics.menus.SingleplayerTemporarySaveMenu;
import ethanjones.cubes.world.client.ClientSaveManager;
import ethanjones.cubes.world.generator.GeneratorManager;
import ethanjones.cubes.world.save.Gamemode;
import ethanjones.cubes.world.save.Save;

public class DefaultGameFlowMediator implements GameFlowMediator {

    @Override
    public void openSingleplayerRootMenu() {
        // Previously in MainMenu
        if (Compatibility.get().functionModifier()) {
            Adapter.setMenu(new SingleplayerTemporarySaveMenu());
        } else {
            Adapter.setMenu(new SingleplayerSavesMenu());
        }
    }

    @Override
    public void openMultiplayerMenu() {
        Adapter.setMenu(new MultiplayerConnectMenu());
    }

    @Override
    public void openSingleplayerSaveCreateMenu() {
        Adapter.setMenu(new SingleplayerSaveCreateMenu());
    }

    @Override
    public void startTemporarySingleplayer(String generatorId, Gamemode gamemode, String seed) {
        Save save = ClientSaveManager.createTemporarySave(generatorId, gamemode, seed);
        Adapter.setMenu(new SingleplayerLoadingMenu(save));
    }

    @Override
    public void createAndStartSingleplayerSave(String name, String generatorId, Gamemode gamemode, String seed) {
        Save save = ClientSaveManager.createSave(name, generatorId, gamemode, seed);
        Adapter.setMenu(new SingleplayerLoadingMenu(save));
    }

    @Override
    public void startSingleplayerFromSave(Save save) {
        if (save == null)
            return;

        // This logic used to live in SingleplayerSavesMenu
        if (GeneratorManager.terrainGeneratorExists(save.getSaveOptions().worldType)) {
            Adapter.setMenu(new SingleplayerLoadingMenu(save));
        } else {
            Adapter.setMenu(new InfoMenu(
                    "Terrain generator '" + save.getSaveOptions().worldType + "' does not exist", true));
        }
    }
}
