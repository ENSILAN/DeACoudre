package me.nathanfallet.ensilan.deacoudre.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.nathanfallet.ensilan.deacoudre.DeACoudre;
import me.nathanfallet.ensilan.deacoudre.utils.DACPlayer;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		DACPlayer zp = DeACoudre.getInstance().getPlayer(e.getPlayer().getUniqueId());
		if (zp != null) {
			DeACoudre.getInstance().uninitPlayer(zp);
		}
	}

}
