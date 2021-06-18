package xyz.fbcf.pridecraft;

import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class ArrowTrail implements CommandExecutor, Listener {

    private HashMap<Projectile, BukkitTask> tasks = new HashMap<>();
    private boolean enabled = false;
    private JavaPlugin plugin;

    public ArrowTrail(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("arrowtrail")) {
        
            if (!(sender instanceof Player)) { sender.sendMessage("Hello Non-player"); return true; }
            
            final Player player = (Player) sender;

            if (!player.hasPermission("pridecraft.admin")) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou require the permission &4pridecraft.admin &cto use this command.")); return true; }
            
            if (args.length != 1) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /arrowtrail <donor>")); return true; }
            
            if (enabled) { sender.sendMessage("Already enabled, please enable once current duration over"); return true; }

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has enabled &4a&cr&6r&eo&aw&2 t&3r&ba&9i&1l&5s! &6Donate at &e(url here)"));
            enabled = true;
            // Bukkit.broadcastMessage("[DEBUG] ArrowTrail Enabled");
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable(){
                @Override
                public void run() {
                    enabled = false;
                    // Bukkit.broadcastMessage("[DEBUG] ArrowTrail Disabled");
                    player.sendMessage("ArrowTrail Disabled");
                }
            }, 6000);  // 6k = 5 mins
        }
        return false;
    }

    @EventHandler
    public void trail(ProjectileLaunchEvent e) {
        //Bukkit.broadcastMessage("onProjectileLaunch");
        if (e.getEntity().getShooter() instanceof Player && e.getEntity() instanceof Arrow && enabled) {
            
            final Arrow arrowF = (Arrow) e.getEntity();
            //Player shooter = (Player) e.getEntity().getShooter();

            
            tasks.put(e.getEntity(), new BukkitRunnable() {
                @Override
                public void run() {  
                    
                    //World w = arrowF.getWorld();
                    //Location l = arrowF.getLocation();  // get arrow
                    arrowF.setColor(Color.fromRGB(App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255)));  // set random colr
                    arrowF.playEffect(EntityEffect.LOVE_HEARTS);  // play smoke which should have random colour
                }
            }.runTaskTimer(plugin, 0L, 1L));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        // Bukkit.broadcastMessage("onProjectileHit");
        if (e.getEntity().getShooter() instanceof Player && enabled) {
            BukkitTask task = tasks.get(e.getEntity());
            if (task != null) {
                task.cancel();
                tasks.remove(e.getEntity());
            }
        }
    }


}