package BetterTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.gravitydevelopment.updater.Updater;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import static org.bukkit.Material.*;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterTP extends JavaPlugin {
    
    private static final String BOLDDARKRED = ChatColor.RESET.toString() + ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString();
    private static final String GREEN = ChatColor.RESET.toString() + ChatColor.GREEN.toString();
    private static final String RED = ChatColor.RESET.toString() + ChatColor.RED.toString();
    FileConfiguration homes = new YamlConfiguration();
    File homesFile;
    FileConfiguration config;
    Plugin plugin;
    
    
    HashMap<Player, Location> backLoc = new HashMap();
    HashMap<String , TpRequest> TpRequest = new HashMap();
    HashMap<Integer, World> Worlds = new HashMap();
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    
    @Override
    public void onEnable() {
        Updater update = new Updater(this, 275467, getFile(), Updater.UpdateType.DEFAULT, true);
        homesFile = new File(getDataFolder(), "homes.yml");
        
        createConfigFile();
        
        if (!homesFile.exists()) {
            try {
                homesFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            homes.load(homesFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
        
    }
    
    @Override
    public void onDisable() {
        LOG.info("[BetterTP] Plugin by TrollyJ.");
    }
    
    @Override
    public boolean onCommand (CommandSender sender, Command command, String navesti, String[] arguments) {
        
        Player p = (Player) sender;
        
        if (navesti.equalsIgnoreCase("btp")) {                          // for command /btp
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".btp") || sender.hasPermission("bettertp.bypass")) {
                if (!sender.hasPermission("bettertp.teleport")) {
                    sender.sendMessage(RED + "You are not permitted do that!");
                    return true;
                }
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
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("back")) {                         //for command /back
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".back") || sender.hasPermission("bettertp.bypass")) {
            if (!sender.hasPermission("bettertp.back")) {
                sender.sendMessage(RED + "You are not permitted do that!");
                return true;
            }
            if (sender instanceof Player) {
                back(sender);
                return true;
            }
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("btpa")) {                         //for command /btpa
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".btpa")|| sender.hasPermission("bettertp.bypass")) {
            if (!sender.hasPermission("bettertp.btpa")) {
                sender.sendMessage(RED + "You are not permitted do that!");
                return true;
            }
            if (sender instanceof Player) {
                if (arguments.length > 0) {
                    doTPRequest(sender, arguments[0]);
                    return true;
                } else {
                    BTPNoPlayer(sender, 0);
                    return true;
                } 
                }
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("btpaccept")) {                    //for command /btpaccept
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".btpaccept") || sender.hasPermission("bettertp.bypass")) {
                if (!sender.hasPermission("bettertp.btpaccept")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                if (sender instanceof Player) {
                    if (arguments.length > 0) {
                        doTpAccept(sender, arguments[0]);
                        return true;
                    } else {
                        BTPNoPlayer(sender, 1);
                        return true;
                    }
                }
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("btpdeny")) {                      //for command /btpdeny                   
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".btpdeny") || sender.hasPermission("bettertp.bypass")) {
                if (!sender.hasPermission("bettertp.btpdeny")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                if (sender instanceof Player) {
                    if (arguments.length > 0) {
                        doTpDeny(sender, arguments[0]);
                        return true;
                    } else {
                        BTPNoPlayer(sender, 1);
                        return true;
                    }
                }
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("btprandom")) {                    //for command /btprandom
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".btprandom") || sender.hasPermission("bettertp.bypass")) {
                if (!sender.hasPermission("bettertp.random")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                if (sender instanceof Player) {
                    randomTp(sender);
                    return true;
                }
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return true;
            }
        } 
        
        if (navesti.equalsIgnoreCase("btphere")) {
            if (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".btphere") || sender.hasPermission("bettertp.bypass")) {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("bettertp.tphere")) {
                        sender.sendMessage(RED + "You are not permitted to do that!");
                        return true;
                    }
                    if (arguments.length == 1) {
                        tpHere(sender, arguments[0]);
                        return true;
                    }
                }   
            } else {
                sender.sendMessage(RED + "You can't use this command in this world");
                return false;
            }
        }
        if (navesti.equalsIgnoreCase("bsetwp")) {
            if(sender instanceof Player) {
                if (!sender.hasPermission("bettertp.setperm")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                if (arguments.length == 2) {
                    setWorldPerm(sender, arguments[0], arguments[1]);
                    return true;
                } else {
                    return false;
                }
            }
        }
        
        if (navesti.equalsIgnoreCase("bsethome") && (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".bsethome") || sender.hasPermission("bettertp.bypass"))) {
            if (sender instanceof Player) {
                if (!sender.hasPermission("bettertp.sethome")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                setHome(sender);
                return true;
            }
        }
        if (navesti.equalsIgnoreCase("bdelhome") && (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".bdelhome") || sender.hasPermission("bettertp.bypass"))) {
            if (sender instanceof Player) {
                if (!sender.hasPermission("bettertp.delhome")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                delHome(sender);
                return true;
            }
        }
        
        if (navesti.equalsIgnoreCase("bhome") && (config.getBoolean("worldpermissions." + p.getWorld().getName() + ".bhome") || sender.hasPermission("bettertp.bypass"))) {
            if (sender instanceof Player) {
                if (!sender.hasPermission("bettertp.home")) {
                    sender.sendMessage(RED + "You are not permitted to do that!");
                    return true;
                }
                home(sender);
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
            
            player.sendMessage(GREEN + "You have teleported to " + BOLDDARKRED + getServer().getPlayer(playerToTP).getName());
            
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
            "/btpaccept " + ChatColor.RESET + GREEN + "to accept the request \nType " + BOLDDARKRED + "/btpdeny " + ChatColor.RESET + GREEN + "to decline the request" );
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
                rSend.sendMessage(BOLDDARKRED + rAcc.getName()+ ChatColor.RESET + GREEN + " has accepted your request");
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
        
        LOG.info("[BetterTP] " + playTp.getName() + " has teleported to " + x + " " + y + " " + z);
        
        playTp = null;
        
    }
    
    public void LoggerOutput (String playerTeleported) {
        
        Player playTp = getServer().getPlayer(playerTeleported);
        
        Location playTpLoc = playTp.getLocation();
        
        double x = playTpLoc.getX();
        double y = playTpLoc.getY();
        double z = playTpLoc.getZ();
        
        playTpLoc = null;
        
        LOG.info("[BetterTP] " + playTp.getName() + " has teleported to " + x + " " + y + " " + z);
        
        playTp = null;
        
    }
    
    public void randomTp (CommandSender sender) { //teleports player to random place in the world in which is the player
        
        Player player = (Player) sender;
        int X = (int) (Math.random() * 10000);
        int Z = (int) (Math.random()*10000);
        double x = X + 0.5;
        double z = Z + 0.5;
        int Y = top(sender, x, z);
        Location tpLoc = new Location(player.getWorld(), x, Y, z);
        
        if (Y == Integer.MAX_VALUE) {
            sender.sendMessage(GREEN + "You have been saved from teleporting there, try again");
            return;
        }
        
        if (backLoc.containsKey(player)) {
            backLoc.replace(player, player.getLocation());
            } else {
            backLoc.put(player, player.getLocation());
            }
        
        player.setInvulnerable(true);
        invTimeOut(player);
        player.teleport(tpLoc);
        player.sendMessage(GREEN + "You were randomly teleported");
        
        player = null;
        tpLoc = null;
        
        LoggerOutput(sender);
        
    }
    
    public int top (CommandSender sender ,double x, double z) { //gets the first block of air at the top of the world, prevents suffering
        Player player = (Player) sender;
        
        Location loc = new Location(player.getWorld(), x, 15, z);
        Location loc2 = loc;
        Location lavaLoc = loc2.add(0, -1, 0);
        
        do {
            loc.add(0, 1, 0);
            lavaLoc.add(0, 1, 0);
            lavaLoc = loc;
        }
        while (loc.getBlock().getType() != AIR );
        
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
    
    public boolean getWorldPerm (CommandSender sender, String command) {  //gets permissions for world, reads full file
        
        Player player = (Player) sender;
        World world = player.getWorld();
        String worldName = world.getName();
        String path = getFile().getPath();
        String bPath = path.replace("BetterTP.jar", "/BetterTP/");
        player = null;
        path = null;
        world = null;
        File file = new File(bPath + worldName + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                if (line.equalsIgnoreCase(command)) {
                    return true;
                }
                line = br.readLine();
            }
            line = null;
            br = null;
            
        } catch (IOException ex) {
            LOG.info("World permission file not found");
        }
        
        
        file = null;
        worldName = null;
        bPath = null;
        return false;
    }
    
    public void invTimeOut (Player invulnerable) { //starts the timer when request will time out
        
        Timer timer = new Timer();
        
        timer.schedule(new TimerTask() {
        @Override
        public void run() {
            
             invulnerable.setInvulnerable(false);
            
            }
        }, 5000);
        
        timer = null;
        
    }
    
    public void tpHere (CommandSender sender, String playerTeleported) {
        
        Player player = (Player) sender;
        Location pLoc = player.getLocation();
        try {
        getServer().getPlayer(playerTeleported).teleport(pLoc);
        LOG.info(getServer().getPlayer(playerTeleported).getName() + "was teleported to " + pLoc.getX() + " " + pLoc.getY() + " " + + pLoc.getZ() + " by " + player.getName());
        sender.sendMessage(GREEN + "You have teleported " + BOLDDARKRED + getServer().getPlayer(playerTeleported).getName() + ChatColor.RESET + GREEN + " to you");
        } catch (NullPointerException e) {
            sender.sendMessage(BOLDDARKRED + "This player is not online");
        }
    }
    
    public void createConfigFile () {
        
        plugin = this;
        LOG.info("[BetterTP] Loading config...");
        config = getConfig();
        
        if(!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        File configFile = new File(getDataFolder(), "config.yml");
        List<World> w = getServer().getWorlds();
        
        for (int i = 0; i <= w.size() - 1; i++) {
            config.set("worldpermissions." + w.get(i).getName() + ".btp", true);
            config.set("worldpermissions." + w.get(i).getName() + ".btpa", true);
            config.set("worldpermissions." + w.get(i).getName() + ".btpaccept", true);
            config.set("worldpermissions." + w.get(i).getName() + ".btpdeny", true);
            config.set("worldpermissions." + w.get(i).getName() + ".btprandom", true);
            config.set("worldpermissions." + w.get(i).getName() + ".btphere", true);
            config.set("worldpermissions." + w.get(i).getName() + ".bsethome", true);
            config.set("worldpermissions." + w.get(i).getName() + ".bdelhome", true);
            config.set("worldpermissions." + w.get(i).getName() + ".bhome", true);
            config.set("worldpermissions." + w.get(i).getName() + ".back", true);
        }
        
        if (!configFile.exists()) {
            saveConfig();
        }
        
    }
    
    public void setWorldPerm (CommandSender sender, String command, String bo) {
        
        Player p = (Player) sender;
        World w = p.getWorld();
        
        if (command.equalsIgnoreCase("btp")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("btpa")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("btpaccept")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("btpdeny")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("btprandom")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("btphere")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("back")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("bsethome")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("bdelhome")) {
            //nothing needed to do
        } else if (command.equalsIgnoreCase("bhome")) {
            //nothing needed to do
        } else {
            p.sendMessage(RED + "Not valid command");
            return;
        }
        
        if (bo.equalsIgnoreCase("true")) {
            config.set("worldpermissions." + w.getName() + "." + command, true);
            sender.sendMessage(GREEN + "You have allowed /" + RED + command + GREEN + "in this world");
            saveConfig();
        } else if (bo.equalsIgnoreCase("false")) {
            config.set("worldpermissions." + w.getName() + "." + command, false);
            sender.sendMessage(GREEN + "You have forbidden /" + RED + command + GREEN + "in this world");
            saveConfig();
        } else {
            p.sendMessage(RED + "Values allowed: true/false");
        }
    }
    
    public void setHome (CommandSender sender) {
        
        Player player = (Player) sender;
        homes.set("home." + player.getName() , player.getLocation());
        player.sendMessage(GREEN + "You have succesfully set home!");
        saveHomes();
        
    }
    
    public void delHome (CommandSender sender) {
        
        Player player = (Player) sender;
        
        if (homes.contains("home." + player.getName())) {
            homes.set("home." + sender.getName(), null);
            player.sendMessage(GREEN + "You have succesfully deleted your home");
            saveHomes();
        } else {
            player.sendMessage(RED + "You have not set home yet");
        }
        
    }
    
    public void home (CommandSender sender) {
        
        Player player = (Player) sender;
        
        if (homes.contains("home." + player.getName())){
            player.teleport((Location) (homes.get("home." + player.getName())));
            player.sendMessage(GREEN + "You have been teleported home");
        } else {
            player.sendMessage(RED + "You have not set home yet!");
        }
        
    }
    
    private void saveHomes () {
        
        try {
            homes.save(homesFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
    