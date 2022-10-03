package me.nathanfallet.ensilan.deacoudre.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.nathanfallet.ensilan.deacoudre.DeACoudre;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		DeACoudre.getInstance().initPlayer(e.getPlayer());
	}

}
