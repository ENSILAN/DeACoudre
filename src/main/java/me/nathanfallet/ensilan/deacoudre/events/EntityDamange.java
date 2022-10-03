package me.nathanfallet.ensilan.deacoudre.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.nathanfallet.ensilan.deacoudre.DeACoudre;
import me.nathanfallet.ensilan.deacoudre.utils.DACPlayer;
import me.nathanfallet.ensilan.deacoudre.utils.Game;

public class EntityDamange implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (
            event.getEntity().getWorld().getName().equals("DeACoudre") &&
            event.getEntity() instanceof Player &&
            event.getCause() == DamageCause.FALL
        ) {
            // Get the player and check for game
            Player player = (Player) event.getEntity();
            DACPlayer zp = DeACoudre.getInstance().getPlayer(player.getUniqueId());
            for (Game game : DeACoudre.getInstance().getGames()) {
                if (zp.getCurrentGame() == game.getGameNumber() && zp.isPlaying()) {
                    zp.setLives(zp.getLives() - 1);
                    game.nextJumper();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
    
}
