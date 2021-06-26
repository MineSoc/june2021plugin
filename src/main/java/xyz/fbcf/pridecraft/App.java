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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.entity.*;

public class App extends JavaPlugin implements org.bukkit.event.Listener {

    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Player> playersSu = new ArrayList<Player>();
    ArrayList<Player> playersC = new ArrayList<Player>();
    ArrayList<Player> playersA = new ArrayList<Player>();
    ArrayList<Player> playersSp = new ArrayList<Player>();

    private final String DONATE_URL = "https://bit.ly/2U6k9Kl";

    Material[] weapons = { Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.BOW, Material.CROSSBOW };

    @Override
    public void onEnable() {
        // runs on startup, reload, plugin reload
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Hello, MineSoc and EsportsSoc!");

        ArrowTrail ARROWTRAIL = new ArrowTrail(this);
        this.getCommand("arrowtrail").setExecutor(ARROWTRAIL);
        this.getServer().getPluginManager().registerEvents(ARROWTRAIL, this);
        Gay GAY = new Gay(this);
        this.getCommand("gay").setExecutor(GAY);

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
        if (min == max) return min;
        if (min > max) throw new IllegalArgumentException("max must be greater than min");

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
        ArrayList<Player> playersSuA = new ArrayList<Player>();
        playersSuA.addAll(playersA);
        playersSuA.addAll(playersSu);

        Player player = (Player) sender;
        if (!player.hasPermission("pridecraft.admin")) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou require the permission &4pridecraft.admin &cto use this command.")); return true; }
        if (args.length != 1) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /command <donor>")); return true; }
        //Location playerLoc = player.getLocation();

        // When someone donates for lava rain, spawn lava 10 blocks above them for 10 seconds, then remove
        if (label.equalsIgnoreCase("lavarain")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has triggered &4&llava rain&6! Donate at &e " + DONATE_URL));
            
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
        else if (label.equalsIgnoreCase("break")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has triggered &b&lblock breaker&6! Donate at &e " + DONATE_URL));

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
        else if (label.equalsIgnoreCase("launch")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has triggered &a&llaunch&6! A random player will be launched into the air. Donate at &e " + DONATE_URL));

            int playerIndex = getRandomNumberInRange(0, playersSuA.size()-1);
            playersSuA.get(playerIndex).setVelocity(new Vector(0, 1000, 0));

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', playersSuA.get(playerIndex).getDisplayName() + "&6 to the moon! Donate at &e " + DONATE_URL));

        }

        // Delete item in player's hand
        else if (label.equalsIgnoreCase("deletehand")) {
            // Announce that someone has donated
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has triggered &c&a&oDelete Hand&r&6! Say goodbye to your item! Donate at &e " + DONATE_URL));

            for (Player p: playersSu) p.getInventory().setItemInMainHand(null);
        }

        // Give everyone a random weapon
        else if (label.equalsIgnoreCase("randomweapon")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has given everyone a &a&lRandom Weapon&6! &6Donate at &e " + DONATE_URL));
            for (Player p : players) {
                p.getInventory().addItem(new ItemStack(weapons[getRandomNumberInRange(0, weapons.length-1)], 1));
            }
        }

        //spawn a jeb sheep on each player
        else if (label.equalsIgnoreCase("sheep")){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has given everyone a &a& sheep &6! &6Donate at &e " + DONATE_URL));
            Entity sheep;
            for (Player p : playersSuA) {
                sheep = p.getWorld().spawnEntity(p.getLocation(),EntityType.SHEEP);
                sheep.setCustomNameVisible(false);
                sheep.setCustomName("jeb_");
            }
        }

        // Swap 2 random players
        else if (label.equalsIgnoreCase("switchtwo")) {
            // If there are <= 2 players in adventure / survival mode, don't teleport people and send a warning.
            if (playersSuA.size() < 2) { 
                sender.sendMessage("There are less than two adventure/survival players, can't execute that command.");
                return true;
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has activated &b&lRandom teleport&6! &6Donate at &e " + DONATE_URL));

            // Get two random, distinct players and teleport them each to where the other was.
            int i1 = getRandomNumberInRange(0, playersSuA.size()-1);
            int i2 = getRandomNumberInRange(0, playersSuA.size()-2);
            if (i2 >= i1) i2++;
            Player p1 = playersSuA.get(i1);
            Player p2 = playersSuA.get(i2);
            Location l1 = p1.getLocation();
            Location l2 = p2.getLocation();
            p2.teleport(l1);
            p1.teleport(l2);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', p1.getDisplayName() + "&6 and &f" + p2.getDisplayName() + "&6 were swapped")); // Announce the teleport
        }

        else if (label.equalsIgnoreCase("superspeed")) {
            // Announce donation
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has given a random person &9&lSuper Speed&6! &6Donate at &e " + DONATE_URL));
            
            // choose random player
            int playerIndex = getRandomNumberInRange(0, playersSuA.size()-1);

            //apply effect
            playersSuA.get(playerIndex).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 100));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', playersSuA.get(playerIndex).getDisplayName() +"&6 was given &9&lSuper Speed&6!"));
        }

        else if (label.equalsIgnoreCase("superjump")) {
            // Announce donation
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has given a random person &9&lSuper Jump&6! &6Donate at &e " + DONATE_URL));
            
            // choose random player
            int playerIndex = getRandomNumberInRange(0, playersSuA.size()-1);

            //apply effect
            playersSuA.get(playerIndex).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 100));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', playersSuA.get(playerIndex).getDisplayName() +"&6 was given &9&lSuper Jump&6!"));
        }

        else if (label.equalsIgnoreCase("nausea")) {
            // Announce donation
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has given a random person &9&lNausea&6! &6Donate at &e " + DONATE_URL));
            
            // choose random player
            int playerIndex = getRandomNumberInRange(0, playersSuA.size()-1);

            //apply effect
            playersSuA.get(playerIndex).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 100));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', playersSuA.get(playerIndex).getDisplayName() +"&6 was given &9&lNausea&6!"));
        }

        else if (label.equalsIgnoreCase("blindness")) {
            // Announce donation
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has given a random person &9&lBlindness&6! &6Donate at &e " + DONATE_URL));
            
            // choose random player
            int playerIndex = getRandomNumberInRange(0, playersSuA.size()-1);

            //apply effect
            playersSuA.get(playerIndex).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 100));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', playersSuA.get(playerIndex).getDisplayName() +"&6 was given &9&lBlindness&6!"));
        }

        else if (label.equalsIgnoreCase("trap")){
            //announce trap
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has &9&lTrapped&6 a random person! Donate at &e " + DONATE_URL));

            // choose random player
            int playerIndex = getRandomNumberInRange(0, playersSuA.size()-1);
            Player p = playersSuA.get(playerIndex);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', p.getDisplayName() + "&6 is stuck in a maze! Donate at &e " + DONATE_URL));


            int[][] maze = MazeGen.generateMaze();
            for(int[] j : maze){
                for(int i : j){
                    if(i == 1) System.out.print("  ");
                    else System.out.print(i+ " ");
                }
                System.out.println();
            }

            for(int i = -1; i< maze.length-1; i++){
                for(int j = -1; j < maze.length-1; j++){
                    Location loc = p.getLocation();
                    loc.setX(loc.getX()+j);
                    loc.setZ(loc.getZ()+i);
                    loc.setY(loc.getY()-1);
                    p.getWorld().getBlockAt(loc).setType(Material.BEDROCK);
                    loc.setY(loc.getY()+3);
                    p.getWorld().getBlockAt(loc).setType(Material.BARRIER);
                }
            }

            for(int i = -1; i< maze.length-1; i++){
                for(int j = -1; j < maze.length-1; j++){
                    Location loc = p.getLocation();
                    loc.setX(loc.getX()+i);
                    loc.setZ(loc.getZ()+j);
                    if(maze[j+1][i+1] == 0){
                        loc.setY(loc.getY());
                        p.getWorld().getBlockAt(loc).setType(Material.BEDROCK);
                        loc.setY(loc.getY()+1);
                        p.getWorld().getBlockAt(loc).setType(Material.BEDROCK);
                    }
                    else{
                        loc.setY(loc.getY());
                        p.getWorld().getBlockAt(loc).setType(Material.AIR);
                        loc.setY(loc.getY()+1);
                        p.getWorld().getBlockAt(loc).setType(Material.AIR);
                    }
                }
            }

        }

        return true;
    }
}