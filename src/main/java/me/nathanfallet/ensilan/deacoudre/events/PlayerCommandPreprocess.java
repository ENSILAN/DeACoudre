package me.nathanfallet.ensilan.deacoudre.events;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.nathanfallet.ensilan.core.models.AbstractGame.GameState;
import me.nathanfallet.ensilan.deacoudre.DeACoudre;
import me.nathanfallet.ensilan.deacoudre.utils.DACPlayer;
import me.nathanfallet.ensilan.deacoudre.utils.Game;

public class PlayerCommandPreprocess implements Listener {

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		DACPlayer zp = DeACoudre.getInstance().getPlayer(e.getPlayer().getUniqueId());
		if (zp.getCurrentGame() != 0) {
			for (Game g : DeACoudre.getInstance().getGames()) {
				if (g.getGameNumber() == zp.getCurrentGame() && g.getState().equals(GameState.IN_GAME)) {
					for (UUID uuid : g.getAllPlayers()) {
						if (e.getPlayer().getUniqueId().equals(uuid)) {
							if (!e.getMessage().equalsIgnoreCase("/deacoudre leave")) {
								e.setCancelled(true);
								e.getPlayer().sendMessage(
										"§cVous ne pouvez utiliser que la commande &4/deacoudre leave &cpendant une partie !");
							}
							return;
						}
					}
				}
			}
		}
	}

}
