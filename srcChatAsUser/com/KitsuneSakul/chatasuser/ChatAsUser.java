package com.KitsuneSakul.chatasuser;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatAsUser extends JavaPlugin{
    
@Override
public void onEnable(){
    getConfig().options().copyDefaults(true);
    saveConfig();
}
  
@Override
public void onDisable(){
}
  
@Override
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    if ((cmd.getName().equalsIgnoreCase("cau")) && ((sender.hasPermission("cau.use")) || (sender.hasPermission("cau.masteruse")))){
        
      if (args.length < 2){
        sender.sendMessage(getConfig().getString("invalidCommand"));
      }
      else if (args.length >= 2){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++){
            sb.append(args[i]).append(" ");
        }
        
        String cauText = sb.toString().trim();
        Player cauTarget = Bukkit.getServer().getPlayer(args[0]);
        
        if (cauTarget != null){
            if ((!cauTarget.hasPermission("cau.bypass")) || (sender.hasPermission("cau.masteruse") == true)){
                sender.sendMessage(getConfig().getString("controlMessage").replaceAll("%player%", cauTarget.getName()));
                cauTarget.chat(cauText);
                }
            
            else if (cauTarget.hasPermission("cau.bypass")){
                sender.sendMessage(getConfig().getString("cantControl").replaceAll("%player%", cauTarget.getName()));
                }
        }
        
        else if(cauTarget == null && "@a".equals(args[0]) && sender.hasPermission("cau.masteruse")){
            for(Player p : getServer().getOnlinePlayers()) {
                p.chat(cauText);
                }
            sender.sendMessage(getConfig().getString("forcingAll"));
        }
      
        else if(cauTarget == null && "@ab".equals(args[0]) && sender.hasPermission("cau.masteruse")){
            for(Player p : getServer().getOnlinePlayers()) {
                if(p.hasPermission("cau.bypass") || p.hasPermission("cau.masteruse")){
                    //Nothing
                }
              else{
                  p.chat(cauText);
              }
          }
          sender.sendMessage(getConfig().getString("forcingAllWithoutBypass"));
        }
      
        else if(cauTarget == null && "@am".equals(args[0]) && sender.hasPermission("cau.masteruse")){
            for(Player p : getServer().getOnlinePlayers()) {
                if( p.hasPermission("cau.masteruse")){
                    //Nothing
                }
                else{
                    p.chat(cauText);
                }
          }
          sender.sendMessage(getConfig().getString("forcingAllWithoutMaster"));
        }
      
      else{
        sender.sendMessage(getConfig().getString("targetOffline").replaceAll("%player%", args[0]));
        
        }
        sb = null;          
        cauText = null;     
        cauTarget = null;   
        System.gc();            //makes more space for other other not unuseless objects
      }
    }
    return true;
   }
}