package me.nathanfallet.ensilan.deacoudre.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.nathanfallet.ensilan.core.Core;
import me.nathanfallet.ensilan.core.models.AbstractGame;
import me.nathanfallet.ensilan.core.models.EnsilanPlayer;
import me.nathanfallet.ensilan.deacoudre.DeACoudre;

public class Game extends AbstractGame {

	// Properties

	private Iterator<UUID> currentTurn;
	private Player currentJumper;

    // Initializer

	public Game(int gameNumber) {
		super(gameNumber);
	}

	// Methods

	// Countdown before the start of the game. Zero to disable
	@Override
    public int getCountdown() {
		return 30;
	}

    // Number of players required for the game to start
	@Override
    public int getMinPlayers() {
		return 2;
	}

    // Max number of players in the game
	@Override
    public int getMaxPlayers() {
		return 10;
	}

    // Name of the game
	@Override
    public String getGameName() {
		return "DeACoudre";
	}
    
    // Handle the start process of the game
	@Override
    public void start() {
		for (UUID uuid : getPlayers()) {
			DACPlayer zp = DeACoudre.getInstance().getPlayer(uuid);
			zp.setPlaying(true);
		}
		state = GameState.IN_GAME;
		loadArena();
	}

    // Handle the stop process of the game
	@Override
    public void stop() {
		if (state.equals(GameState.IN_GAME)) {
			state = GameState.FINISHED;
			Player p = Bukkit.getPlayer(getPlayers().get(0));
			if (p != null) {
				Bukkit.broadcastMessage("§e" + p.getName() + "§7 a gagné la partie de Dé à coudre !");
				p.getInventory().clear();
				p.updateInventory();
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage("§aTu as gagné la partie !");

				EnsilanPlayer ep = Core.getInstance().getPlayer(p.getUniqueId());
				DACPlayer zp = DeACoudre.getInstance().getPlayer(p.getUniqueId());
				ep.setVictories(ep.getVictories() + 1);
				ep.setScore(ep.getScore() + DeACoudre.SCORE);
				ep.setMoney(ep.getMoney() + DeACoudre.MONEY);
				zp.setVictories(zp.getVictories() + 1);
				zp.setScore(zp.getScore() + DeACoudre.SCORE);
			}
			currentCountValue = 0;
			Bukkit.getScheduler().scheduleSyncDelayedTask(DeACoudre.getInstance(), new Runnable() {
				@Override
				public void run() {
					for (UUID uuid : getAllPlayers()) {
						Player player = Bukkit.getPlayer(uuid);
						DACPlayer zp = DeACoudre.getInstance().getPlayer(uuid);
						zp.setCurrentGame(0);
						zp.setPlaying(false);
						player.teleport(Core.getInstance().getSpawn());
						player.setGameMode(GameMode.SURVIVAL);
						player.getInventory().clear();
						player.updateInventory();
					}
					state = GameState.WAITING;
                    resetArena();
				}
			}, 100);
		}
	}

    // Called every second
	@Override
    public void mainHandler() {
		// Check if current jumper is in water to place wool
		if (currentJumper != null) {
			Block block = currentJumper.getLocation().getBlock();
			DACPlayer zp = DeACoudre.getInstance().getPlayer(currentJumper.getUniqueId());
			if (block.getType().equals(Material.WATER)) {
				// Check for an extra life
				if (
					block.getLocation().clone().add(-1, 0, 0).getBlock().getType() != Material.WATER &&
					block.getLocation().clone().add(1, 0, 0).getBlock().getType() != Material.WATER &&
					block.getLocation().clone().add(0, 0, -1).getBlock().getType() != Material.WATER &&
					block.getLocation().clone().add(0, 0, 1).getBlock().getType() != Material.WATER
				) {
					zp.setLives(zp.getLives() + 1);
					for (UUID uuid : getAllPlayers()) {
						Player p = Bukkit.getPlayer(uuid);
						p.sendMessage("§e" + currentJumper.getName() + "§7 a fait un dé à coudre, et obtient une vie supplémentaire !");
					}
				}

				// Put wool
				block.setType(makeWool(zp.getColor()));
				nextJumper();
			}
		}

		int number = getPlayers().size();
		if (number == 0 || number == 1) {
			stop();
		}
	}

    // Get players participating in the game (excluding those who lost)
	@Override
	public ArrayList<UUID> getPlayers() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			DACPlayer zp = DeACoudre.getInstance().getPlayer(p.getUniqueId());
			if (zp.getCurrentGame() == getGameNumber()
					&& ((!state.equals(GameState.IN_GAME) && !state.equals(GameState.FINISHED)) || zp.isPlaying())
					&& !zp.isBuildmode()) {
				result.add(p.getUniqueId());
			}
		}
		return result;
	}

    // Get all players of the game, even those who lost
	@Override
	public ArrayList<UUID> getAllPlayers() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			DACPlayer zp = DeACoudre.getInstance().getPlayer(p.getUniqueId());
			if (zp.getCurrentGame() == getGameNumber() && !zp.isBuildmode()) {
				result.add(p.getUniqueId());
			}
		}
		return result;
	}

    // Make a player join this game
	@Override
    public void join(Player player, EnsilanPlayer ep) {
		// Make player part of this game
		DeACoudre.getInstance().getPlayer(player.getUniqueId()).setCurrentGame(getGameNumber());
	}

	public Material makeWool(int color) {
		switch (color) {
			case 1:
				return Material.ORANGE_WOOL;
			case 2:
				return Material.MAGENTA_WOOL;
			case 3:
				return Material.LIGHT_BLUE_WOOL;
			case 4:
				return Material.YELLOW_WOOL;
			case 5:
				return Material.LIME_WOOL;
			case 6:
				return Material.PINK_WOOL;
			case 7:
				return Material.GRAY_WOOL;
			case 8:
				return Material.LIGHT_GRAY_WOOL;
			case 9:
				return Material.CYAN_WOOL;
			case 10:
				return Material.PURPLE_WOOL;
			case 11:
				return Material.BLUE_WOOL;
			case 12:
				return Material.BROWN_WOOL;
			case 13:
				return Material.GREEN_WOOL;
			case 14:
				return Material.RED_WOOL;
			case 15:
				return Material.BLACK_WOOL;
		}
		return null;
	}

	// Get arena hub
	public Location getHub() {
		Location l = new Location(
            Bukkit.getWorld("DeACoudre"),
            (gameNumber - 1) * 32 + 8, 42, 2
        );
		return l;
	}

    // Load arena
    public void loadArena() {
        Location l = getHub();

		int color = 1;
        for (UUID uuid : getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			DACPlayer zp = DeACoudre.getInstance().getPlayer(uuid);
			
			player.teleport(l);
			player.setGameMode(GameMode.SPECTATOR);
			zp.setPlaying(true);
			zp.setLives(1);
			zp.setColor(color);
			player.getInventory().clear();
			player.getInventory().addItem(new ItemStack(makeWool(color)));
			player.updateInventory();
			color++;
		}

		nextJumper();
    }

	// Go to next jumper
	public void nextJumper() {
		// Get players again if needed
		if (currentTurn == null || !currentTurn.hasNext()) {
			currentTurn = getPlayers().iterator();
		}

		// Handle current jumper
		if (currentJumper != null) {
			DACPlayer zp = DeACoudre.getInstance().getPlayer(currentJumper.getUniqueId());
			if (zp.getLives() == 0) {
				zp.setPlaying(false);
                currentJumper.sendMessage("§7Vous avez perdu !");
                for (UUID uuid : getAllPlayers()) {
                    Player p = Bukkit.getPlayer(uuid);
                    p.sendMessage("§e" + currentJumper.getName() + "§7 a perdu !");
                }
			}
			currentJumper.setGameMode(GameMode.SPECTATOR);
			currentJumper.teleport(getHub());
		}

		// Get next player
		DACPlayer next = null;
		do {
			currentJumper = Bukkit.getPlayer(currentTurn.next());
			next = DeACoudre.getInstance().getPlayer(currentJumper.getUniqueId());
		} while (!next.isPlaying());
		
		// Make it play
		currentJumper.teleport(getHub());
		currentJumper.setGameMode(GameMode.SURVIVAL);
		currentJumper.sendMessage("§aC'est à ton tour !");

		// Reset arena if filled
		if (isArenaFilled()) {
			resetArena();
		}
	}

	// Check if arena is full
	public boolean isArenaFilled() {
		for (int x = 3; x < 13; x++) {
			for (int z = 6; z < 15; z++) {
				if (
					Bukkit.getWorld("DeACoudre")
						.getBlockAt((gameNumber - 1) * 32 + x, 1, z)
						.getType() == Material.WATER
				) {
					return false;
				}
			}
		}
		return true;
	}

    // Reset arena
    public void resetArena() {
        for (int x = 3; x < 13; x++) {
			for (int z = 6; z < 15; z++) {
				Bukkit.getWorld("DeACoudre")
					.getBlockAt((gameNumber - 1) * 32 + x, 1, z)
					.setType(Material.WATER);
			}
		}
    }
    
}
