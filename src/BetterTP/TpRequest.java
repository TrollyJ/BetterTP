package BetterTP;

import org.bukkit.entity.Player;

public class TpRequest {
    
    private final Player RSENDER;
    private final Player RACCEPTER;
    private final boolean REQUESTSENT;
    
    public TpRequest (Player rSender, Player rAccepter, boolean requestSent) {
        
        this.RSENDER = rSender;
        this.RACCEPTER = rAccepter;
        this.REQUESTSENT = requestSent;
        
    }
    
    public Player getRSender () {
        return RSENDER;
    }
    
    public Player getRAccepter () {
        return RACCEPTER;
    }
    
    public Boolean getRequestSent () {
        return REQUESTSENT;
    }
    
}