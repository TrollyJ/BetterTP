package BetterTP;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import static org.bukkit.Material.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterTP extends JavaPlugin {
    
    public static final String BOLDDARKRED = ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    
    HashMap<Player, Location> backLoc = new HashMap();
    HashMap<String , TpRequest> TpRequest = new HashMap();
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    
    @Override
    public boolean onCommand (CommandSender sender, Command command, String navesti, String[] arguments) {
        if (navesti.equalsIgnoreCase("btp")) {                          // for command /btp
            if (sender instanceof Player) {
                if (arguments.length == 1) {
                    doBTP(sender, arguments[0]);
                    return true;
                    
                } else if (arguments.length == 3) {
    
                    doBTPLoc(sender ,arguments[0], arguments[1], arguments[2]);
                    return true;
                    
                } else {
                    BTPNoPlayer(sender, 2);
                    
                }
            }
        }
        if (navesti.equalsIgnoreCase("back")) {                         //for command /back
            if (sender instanceof Player) {
                back(sender);
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("btpa")) {                         //for command /btpa
            if (sender instanceof Player) {
                if (arguments.length > 0) {
                    doTPRequest(sender, arguments[0]);
                    return true;
                } else {
                    BTPNoPlayer(sender, 0);
                    return true;
                }
            }
        }
        if (navesti.equalsIgnoreCase("btpaccept")) {                    //for command /btpaccept
            if (sender instanceof Player) {
                if (arguments.length > 0) {
                    doTpAccept(sender, arguments[0]);
                    return true;
                } else {
                    BTPNoPlayer(sender, 1);
                    return true;
                }
            }
        }
        if (navesti.equalsIgnoreCase("btpdeny")) {                      //for command /btpdeny                   
            if (sender instanceof Player) {
                if (arguments.length > 0) {
                    doTpDeny(sender, arguments[0]);
                    return true;
                } else {
                    BTPNoPlayer(sender, 1);
                    return true;
                }
            }
        }
        if (navesti.equalsIgnoreCase("btprandom")) {                    //for command /btprandom
            if (sender instanceof Player) {
                randomTp(sender);
                return true;
            }
        }
        return false;
    }
    
    public void doBTP (CommandSender sender, String playerToTP) { //teleports player to target player
        try {
            Player player = (Player) sender; 
            Location loc1 = player.getLocation();
            
            backLoc.put(player, player.getLocation());
                
            player.teleport(getServer().getPlayer(playerToTP));
            
            if (loc1 != player.getLocation()) {
            LoggerOutput(sender);
            }
            player = null;                                          //thank you garbage collector
            loc1 = null;
        
        } catch (Exception e) {
            sender.sendMessage(BOLDDARKRED + "This player is not online");
        }
    }
    
    public void doBTPLoc (CommandSender sender, String sX, String sY, String sZ) { //teleports player to taget location (to coordinates)
        Player player = (Player) sender;
        Location loc = new Location(player.getWorld(), Double.parseDouble(sX), Double.parseDouble(sY), Double.parseDouble(sZ));
        
        if (backLoc.containsKey(player)) {
            backLoc.replace(player, player.getLocation());
            } else {
            backLoc.put(player, player.getLocation());
            }
        player.teleport(loc);
        player.sendMessage(GREEN + "You have teleported to " + BOLDDARKRED + Integer.parseInt(sX) + " " + Integer.parseInt(sY) + " " + Integer.parseInt(sZ));
        LoggerOutput(sender);
        
        player = null;
        loc = null;
    }
    
    public void back (CommandSender sender) { //teleports you back to the location from which you have teleported last time
        Player player = (Player) sender;
        
        if (backLoc.get(player) == null) {
            player.sendMessage(BOLDDARKRED + "You have not teleported yet");
            return;
        }
        
        player.teleport(backLoc.get(player));
        
        LoggerOutput(sender);
        
        player = null;
        
        
    }
    
    public void BTPNoPlayer (CommandSender sender, int index) { //sends and error message when there is no player specified to teleport
        
        switch (index) {
            
            case 0: sender.sendMessage(BOLDDARKRED + "Specify a player to teleport to");
                    break;
            case 1: sender.sendMessage(BOLDDARKRED + "Specify a player from which request came");
                    break;
            case 2: sender.sendMessage(BOLDDARKRED + "Specify player or coordinates to teleport to");
        }
        
    }
    
    public void doTPRequest(CommandSender rSender, String rAccepter) { //sends teleport request
        try {
            Player rSend = (Player) rSender;
            Player rAcc = getServer().getPlayer(rAccepter);
            TpRequest tpRequest = new TpRequest(rSend, rAcc, true);
            TpRequest.put(rSend.getName() + rAcc.getName(), tpRequest);
            rSend.sendMessage(GREEN + "Request sent to " + BOLDDARKRED + rAcc.getName() + ChatColor.RESET + GREEN +"\n" + "Request will time out in 30 seconds");
            rAcc.sendMessage(GREEN + "You have received teleport reuqest from " + BOLDDARKRED + rSend.getName() + ChatColor.RESET + GREEN + "\n" + "Type " + BOLDDARKRED + 
            "/btpaccept" + ChatColor.RESET + GREEN + "to accept the request \n" );
            requestTimeOut(rSend, rAcc, tpRequest);
            
            tpRequest = null;
            rSend = null;
            rAcc = null;
            
            } catch (Exception e){
                rSender.sendMessage(BOLDDARKRED + "This player is not online");
        }
    }
    
    public void doTpAccept (CommandSender rAccepter, String rSender) { //accepts teleport requst
        try {
            Player rAcc = (Player) rAccepter;
            Player rSend = getServer().getPlayer(rSender);
            if (TpRequest.containsKey(rSend.getName() + rAcc.getName())) {
                TpRequest.remove(rSend.getName() + rAcc.getName());
                if (backLoc.containsKey(rSend)) {
                    backLoc.replace(rSend, rSend.getLocation());
                    } else {
                    backLoc.put(rSend, rSend.getLocation());
                    }
                rSend.teleport(rAcc.getLocation());
                LoggerOutput(rSender);
                } else {
                
                rAcc.sendMessage(BOLDDARKRED + "You have no teleport request from this player");
                
                }
            
            rAcc = null;
            rSend = null;
            
            } catch (Exception e) {
            
        }
    }
    
    public void requestTimeOut (Player rSender, Player rAccepter, TpRequest tpRequest) { //starts the timer when request will time out
        
        Timer timer = new Timer();
        
        timer.schedule(new TimerTask() {
        @Override
        public void run() {
            if (TpRequest.containsKey(rSender.getName() + rAccepter.getName())) {
            TpRequest.remove(rSender.getName() + rAccepter.getName(), tpRequest);
            rSender.sendMessage(BOLDDARKRED + "Request timed out");
            }
        }
        }, 30000);
        
        timer = null;
        
    }
    
    public void doTpDeny (CommandSender rAccepter, String rSender) { //denies the teleport request
        
        try {
            Player rAcc = (Player) rAccepter;
            Player rSend = getServer().getPlayer(rSender);
        
            if (TpRequest.containsKey(rSend.getName() + rAcc.getName())) {
                TpRequest.remove(rSend.getName() + rAcc.getName());
                rSend.sendMessage(BOLDDARKRED + rAcc.getName() + GREEN + " has denied your request");
                } else {
                rAcc.sendMessage(BOLDDARKRED + "You have no teleport request from this player");
                }
        
            rAcc = null;
            rSend = null;
            } catch (Exception e) {
            rAccepter.sendMessage(BOLDDARKRED + "You have no teleport request from this player");
        }
        
    }
    
    public void LoggerOutput (CommandSender playerTeleported) { //sends information what plugin has done
        
        Player playTp = (Player) playerTeleported;
        
        Location playTpLoc = playTp.getLocation();
        
        double x = playTpLoc.getX();
        double y = playTpLoc.getY();
        double z = playTpLoc.getZ();
        
        playTpLoc = null;
        
        LOG.info(playTp.getName() + " has teleported to " + x + " " + y + " " + z);
        
        playTp = null;
        
    }
    
    public void LoggerOutput (String playerTeleported) {
        
        Player playTp = getServer().getPlayer(playerTeleported);
        
        Location playTpLoc = playTp.getLocation();
        
        double x = playTpLoc.getX();
        double y = playTpLoc.getY();
        double z = playTpLoc.getZ();
        
        playTpLoc = null;
        
        LOG.info(playTp.getName() + " has teleported to " + x + " " + y + " " + z);
        
        playTp = null;
        
    }
    
    public void randomTp (CommandSender sender) { //teleports player to random place in the world in which is the player
        
        Player player = (Player) sender;
        int x = (int) (Math.random()*10000 + Math.random()*1000 + Math.random()*100 + Math.random()*10 + Math.random());
        int z = (int) (Math.random()*10000 + Math.random()*1000 + Math.random()*100 + Math.random()*10 + Math.random());
        int y = top(sender, x, z);
        Location tpLoc = new Location(player.getWorld(), x, y, z);
        
        if (y == Integer.MAX_VALUE) {
            player.sendMessage(GREEN + "You have been saved from teleporting there :)");
            return;
        }
        
        
        if (backLoc.containsKey(player)) {
            backLoc.replace(player, player.getLocation());
            } else {
            backLoc.put(player, player.getLocation());
            }
        
        player.teleport(tpLoc);
        
        player = null;
        tpLoc = null;
        
        LoggerOutput(sender);
        
    }
    
    public int top (CommandSender sender ,int x, int z) { //gets the first block of air at the top of the world, prevents suffering
        Player player = (Player) sender;
        
        Location loc = new Location(player.getWorld(), x, 15, z);
        Location lavaLoc = loc;
        
        while (loc.getBlock().getType() != AIR) {
            loc.add(0, 1, 0);
            lavaLoc = loc;
        }
        if (loc.add(0, 1, 0).getBlock().getType() == AIR && lavaLoc.getBlock().getType() != LAVA && loc.getBlock().getType() != LAVA) {
            int Y = (int) loc.getY();
            player = null;
            loc = null;
            lavaLoc = null;
            return Y;
        } else {
            player = null;
            loc = null;
            lavaLoc = null;
            return Integer.MAX_VALUE;
        }
        
        
        
        
    }
    
}

