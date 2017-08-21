package BetterTP;

import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterTP extends JavaPlugin {
    
    Location tpFrom = null;
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    
    @Override
    public boolean onCommand (CommandSender sender, Command command, String navesti, String[] arguments) {
        if (navesti.equalsIgnoreCase("btp")) {
            if (sender instanceof Player) {
                if (arguments.length > 0) {
                    String playerToTp = arguments[0];
                    doBTP(sender, playerToTp);
                    return true;
                } else {
                    BTPNoPlayer(sender);
                }
            }
        }
        if (navesti.equalsIgnoreCase("back")) {
            if (sender instanceof Player) {
                back(sender);
                return true;
            }
        }
        return false;
    }
    
    public void doBTP (CommandSender sender, String playerToTP) {
        Player player = (Player) sender; 
        try {
        Player playerToTp = getServer().getPlayer(playerToTP);
        Location playerToTpLoc = playerToTp.getLocation();
        Location location = player.getLocation();
        World world = player.getWorld();
        double x = playerToTpLoc.getX();
        double y = playerToTpLoc.getY();
        double z = playerToTpLoc.getZ();
        
        String playerSt = player.getName();
        
        player.teleport(playerToTpLoc);
        
        tpFrom = location;
        
        LOG.info("[BetterTP] Teleported " + playerSt + " to " + x + " " + y + "" + z);
        } catch (Exception e) {
         player.sendMessage("Player is not online");
         LOG.info("[BetterTP] Player is not onlline");
        }
    }
    
    public void back (CommandSender sender) {
        Player player = (Player) sender;
        String playerSt = player.getName();
        try {
        
        player.teleport(tpFrom);
        } catch (Exception e) {
            player.sendMessage("You have not teleported yet.");
        }
        
        double x = tpFrom.getX();
        double y = tpFrom.getY();
        double z = tpFrom.getZ();
        
        LOG.info("[BetterTP] Teleported " + playerSt + " back to " + x + " " + y + " " + z);
    }
    
    public void BTPNoPlayer (CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage("Specify player to teleport to.");
    }
    
}