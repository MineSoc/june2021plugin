package xyz.fbcf.pridecraft;

import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
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
import org.bukkit.Particle.DustOptions;
import org.bukkit.material.*;

import net.md_5.bungee.api.ChatColor;

public class Gay implements CommandExecutor, Listener {

    private HashMap<Player, BukkitTask> tasks = new HashMap<>();
    private boolean enabled = false;
    private JavaPlugin plugin;

    public Gay(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("gay")) {
            if (!(sender instanceof Player)) { 
                sender.sendMessage("Hello Non-player"); 
                return true;
            }
            final Player player = (Player) sender;
            if (!player.hasPermission("pridecraft.admin")) { 
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou require the permission &4pridecraft.admin &cto use this command.")); 
                return true;
            }
            
            if (args.length != 1) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /gay <donor>")); return true; }
            
            if (enabled) { sender.sendMessage("Already enabled, please enable once current duration over"); return true; }

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', args[0] + "&6 has enabled &rainbowrainbow particle effects&6! &6Donate at &e(url here)"));
            enabled = true;
            for (final Player p : Bukkit.getOnlinePlayers()) {
                Bukkit.broadcastMessage(p.getDisplayName());
                //for (final Entity e : p.getNearbyEntities(20, 20, 20)) {
                    //if (e instanceof Player) {
                        tasks.put(p, new BukkitRunnable() {
                            @Override
                            public void run() {  
                                //Potion pot = new Potion(PotionType.AWKWARD, )
                                //p.setColor(Color.fromRGB(App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255)));  // set random colr
                                //p.playEffect(EntityEffect.LOVE_HEARTS);  // play smoke which should have random colour
                               
                                //p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), 0, App.getRandomNumberInRange(0, 255)/255.0f, App.getRandomNumberInRange(0, 255)/255.0f, App.getRandomNumberInRange(0, 255)/255.0f, 1, new DustOptions(Color.fromRGB(App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255)), 1));
                                p.getWorld().spawnParticle(Particle.HEART, p.getLocation().getX(), p.getLocation().getY()+2, p.getLocation().getZ(), 1);
                                p.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, p.getLocation().getX(), p.getLocation().getY()+2, p.getLocation().getZ(), 1);
                                p.getWorld().spawnParticle(Particle.GLOW, p.getLocation().getX(), p.getLocation().getY()+2, p.getLocation().getZ(), 1);
                                p.getWorld().spawnParticle(Particle.BLOCK_CRACK, p.getLocation().getX(), p.getLocation().getY()+2, p.getLocation().getZ(), 1, new MaterialData(Material.STONE));
                                //Particle bbr = new Particle()
                            }
                        }.runTaskTimer(plugin, 0L, 5L));
                    //}
                //}
                // tasks.put(p, new BukkitRunnable() {
                //     @Override
                //     public void run() {  
                //         p.spawnParticle(Particle.BUBBLE_POP, p.getLocation(), 0, App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255), App.getRandomNumberInRange(0, 255), 1);
                //     }
                // }.runTaskTimer(plugin, 0L, 5L));
            }

        }
        return true;
    }

}