package BetterTP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class WorldPermWriter {
    
    private final String path;
    private File file;
    private FileWriter fw;
    private BufferedWriter bw;
            
    private StringBuilder toAdd = new StringBuilder();
    
    public WorldPermWriter (World world ,String pth) {
        World worldN = world;
        String worldName = worldN.getName();
        this.path = pth + "/BetterTP" + "/" + worldName + ".txt";
        try {
            file = new File(path);
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
        } catch (IOException ex) {
            Bukkit.getLogger().info("File not found");
        }
        
        
    }
    
    public void forbidPerm (String command, CommandSender sender) {

        toAdd.append("\n" + command);
        sender.sendMessage(ChatColor.GREEN + "Commands which will be allowed in this world: \n" + toAdd.toString());
        
    }
    
    public boolean reWrite () {
         String toWrite = toAdd.toString();
        
        try {
            bw.write(toWrite);
            Bukkit.getLogger().info(toWrite);
            bw.close();
            fw.close();
            return true;
                   
        
  
        } catch (Exception e) {
            Bukkit.getLogger().info("File no found");
        }
        
        return false;
    }
    
    
    
}