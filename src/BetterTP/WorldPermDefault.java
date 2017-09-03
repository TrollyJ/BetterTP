package BetterTP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldPermDefault {
    
    private final String PATH;
    
    public WorldPermDefault(String path) {
        
        this.PATH = path + "/BetterTP";
        
    }
    
    public String getPath() {
        return PATH;
    }
    
    public void writeDefault() {
        List<World> worlds = Bukkit.getServer().getWorlds();
        
        String content = "btp\nbtpa\nbtpaccept\nbtpdeny\nbtprandom\nback\nbtphere";
        
        try {
            int a = 1;
            int b = 0;
            while (a <= worlds.size()) {
                File file = new File(PATH + "/" + worlds.get(b).getName() + ".txt");
                File readMe = new File (PATH + "/README.txt");
                FileWriter fw1 = new FileWriter(readMe);
                BufferedWriter bw1 = new BufferedWriter(fw1);
                bw1.write("Edit world permissions ONLY with Notepad++ or thru command!");
                bw1.close();
                fw1.close();
                if (file.exists()) {
                    file = null;
                    return;
                }
                FileWriter fw = new FileWriter(PATH + "/" + worlds.get(b).getName() + ".txt");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
                fw.close();
                a++;
                b++;
                
                fw = null;
                bw = null;
                file = null;
            }
            
        } catch (Exception e){
            
        }
        
        worlds.clear();
        content = null;
        
    }
    
}