package BetterTP;

import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterTP extends JavaPlugin {
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    
    @Override
    public boolean onCommand (CommandSender sender, Command command, String navesti, String[] arguments) {
        if (navesti.equalsIgnoreCase("btp")) {
            if (sender instanceof Player) {
                doBTP(sender);
                return true;
            }
        }
        return false;
    }
    
    public void doBTP (CommandSender sender) {
        Player player = (Player) sender;
        Location location = player.getLocation();
        World world = player.getWorld();
        
        
        LOG.info("[BetterTP] Teleported to ");
    }
    
}