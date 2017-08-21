package BetterTP;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterTP extends JavaPlugin {
    
    HashMap<Player, Location> backLoc = new HashMap();
    
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
        double x = playerToTpLoc.getX();
        double y = playerToTpLoc.getY();
        double z = playerToTpLoc.getZ();
        
        String playerSt = player.getName();
        
        player.teleport(playerToTpLoc);
        
        backLoc.put(player, location);
        
        LOG.info("[BetterTP] Teleported " + playerSt + " to " + x + " " + y + "" + z);
        } catch (Exception e) {
         player.sendMessage("Player is not online");
         LOG.info("[BetterTP] Player is not onlline");
        }
    }
    
    public void back (CommandSender sender) {
        Player player = (Player) sender;
        String playerSt = player.getName();
        
        if (backLoc.get(player) == null) {
            player.sendMessage("You have not teleported yet");
            LOG.info("[BetterTP] Player " + playerSt + " has not teleported yet");
            return;
        }
        
        player.teleport(backLoc.get(player));
        
        double x = backLoc.get(player).getX();
        double y = backLoc.get(player).getY();
        double z = backLoc.get(player).getZ();
        
        LOG.info("[BetterTP] Teleported " + playerSt + " back to " + x + " " + y + " " + z);
    }
    
    public void BTPNoPlayer (CommandSender sender) {
        Player player = (Player) sender;
        player.sendMessage("Specify player to teleport to.");
    }
    
}