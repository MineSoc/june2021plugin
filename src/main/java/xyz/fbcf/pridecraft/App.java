package xyz.fbcf.pridecraft;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.util.Vector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class App extends JavaPlugin implements org.bukkit.event.Listener {

    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Player> playersSu = new ArrayList<Player>();
    ArrayList<Player> playersC = new ArrayList<Player>();
    ArrayList<Player> playersA = new ArrayList<Player>();
    ArrayList<Player> playersSp = new ArrayList<Player>();
    Material[] weapons = {Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.BOW, Material.CROSSBOW};

    @Override
    public void onEnable() {
        // runs on startup, reload, plugin reload
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Hello, MineSoc and EsportsSoc!");

        ArrowTrail ARROWTRAIL = new ArrowTrail(this);
        this.getCommand("arrowtrail").setExecutor(ARROWTRAIL);
        this.getServer().getPluginManager().registerEvents(ARROWTRAIL, this);


    }
    @Override
    public void onDisable() {
        //runs on shutdown, reload, plugin reload
        getLogger().info("See ya later, MineSoc and EsportsSoc!");
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Welcome to the server, " + player.getDisplayName() + "&6!"));
    }

    static int getRandomNumberInRange(int min, int max) {
        if (min >= max) throw new IllegalArgumentException("max must be greater than min");

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // create players lists
        players = new ArrayList<Player>();
        playersSu = new ArrayList<Player>();
        playersC = new ArrayList<Player>();
        playersA = new ArrayList<Player>();
        playersSp = new ArrayList<Player>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.add(p);
            if (p.getGameMode() == GameMode.SURVIVAL) playersSu.add(p);
            else if (p.getGameMode() == GameMode.CREATIVE) playersC.add(p);
            else if (p.getGameMode() == GameMode.ADVENTURE) playersA.add(p);
            else if (p.getGameMode() == GameMode.SPECTATOR) playersSp.add(p);
        }

        Player player = (Player) sender;
        //Location playerLoc = player.getLocation();

        // When someone donates for lava rain, spawn lava 10 blocks above them for 10 seconds, then remove
        if (label.equalsIgnoreCase("lavarain")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + "&6 has triggered &4&llava rain&6! Donate at &e(url here)"));
            
            ArrayList<Block> blocks = new ArrayList<Block>();
            // for every p in players, add the block 10 above them to a list
            for (Player p : playersSu) {
                Location tenAbove = p.getLocation();
                tenAbove.setY(tenAbove.getY() + 5);
                blocks.add(p.getWorld().getBlockAt(tenAbove));
            }
            // for each block, set to lava
            for (Block b : blocks) b.setType(Material.LAVA);

            // for every lava block, replace with air 100 ticks later
            final ArrayList<Block> blocksF = blocks; // Create a final version of blocks arraylist to stop scheduler throwing a hissy fit
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    // method to run after delay
                    for (Block b : blocksF) b.setType(Material.AIR);
                }
            }, 100); // delay of 100ticks

        }

        // When someone donates for breaking blocks, force everyone to break the block in front of them with the item currently in their hand
        if (label.equalsIgnoreCase("break")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + "&6 has triggered &b&lblock breaker&6! Donate at &e(url here)"));

            for (Player p : playersSu) {
                int x = 1;
                int z = 1;
                int y = getRandomNumberInRange(2, 10);
                for (int xi = -x; xi <= x; xi++) {
                    for (int zi = -z; zi <= z; zi++) {
                        for (int yi = 0; yi <= y; yi++) {
                            Location loc = p.getLocation();
                            loc.setX(loc.getX() + xi);
                            loc.setY(loc.getY() - yi);
                            loc.setZ(loc.getZ() + zi);
                            //Bukkit.broadcastMessage(loc.getX() + " " + loc.getY() + " " + loc.getZ());
                            if (!p.getWorld().getBlockAt(loc).getType().equals(Material.BEDROCK)) 
                                p.getWorld().getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                }
            }
            
        }
        
        // Launch a random player
        if (label.equalsIgnoreCase("launch")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + "&6 has triggered &a&llaunch&6! A random player will be launched into the air. Donate at &e(url here)"));
            
            ArrayList<Player> allowedPlayers = new ArrayList<Player>();
            allowedPlayers.addAll(players);
            allowedPlayers.addAll(playersSu);

            int playerIndex = getRandomNumberInRange(0, allowedPlayers.size()-1);
            allowedPlayers.get(playerIndex).setVelocity(new Vector(0, 100, 0));

        }

        // Delete item in player's hand
        if (label.equalsIgnoreCase("deletehand")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + "&6 has triggered &c&a&oDelete Hand&r&6! Say goodbye to your item! Donate at &e(url here)"));

            for (Player p: playersSu) p.getInventory().setItemInMainHand(null);
        }

        // Give everyone a random weapon
        if (label.equalsIgnoreCase("randomweapon")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + "&6 has given everyone a &a&lRandom Weapon&6! &6Donate at &e(url here)"));
            for (Player p : players) {
                p.getInventory().addItem(new ItemStack(weapons[getRandomNumberInRange(0, weapons.length-1)], 1));
            }
        }

        return true;
    }
}