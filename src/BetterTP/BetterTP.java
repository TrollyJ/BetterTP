package BetterTP;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
                if (arguments.length > 0) {
                    String playerToTp = arguments[0];
                    doBTP(sender, playerToTp);
                    return true;
                } else {
                    BTPNoPlayer(sender, 0);
                    return true;
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
        }
        
    }
    
    public void doTPRequest(CommandSender rSender, String rAccepter) { //sends teleport request
        try {
            Player rSend = (Player) rSender;
            Player rAcc = getServer().getPlayer(rAccepter);
            TpRequest tpRequest = new TpRequest(rSend, rAcc, true);
            TpRequest.put(rSend.getName() + rAcc.getName(), tpRequest);
            rSend.sendMessage(GREEN + "Request sent to " + BOLDDARKRED + rAcc.getName() + ChatColor.RESET + GREEN +"\n" + "Request will time out in 30 seconds");
            rAcc.sendMessage(GREEN + "You have received teleport reuqest from " + BOLDDARKRED + rSend.getName());
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
    
}

