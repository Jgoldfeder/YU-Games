package corona.games.communication;
import java.util.UUID;

/**
 * Created by noamannenberg
 * on 4/5/20.
 */
public class GameInfo {
    public final String gameName;
    public final String name;
    public final String username;
    public final int curPlayers;
    public final int minPlayers;
    public final int maxPlayers;
    public final UUID id;
    
    public GameInfo(String gameName, String name, String username, int curPlayers,int minPlayers, int maxPlayers) {
        this.gameName = gameName;
        this.name = name;
        this.username = username;
        this.curPlayers = curPlayers;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Game: " + this.gameName + " Game Name: " + this.name + " Hostname: " + this.username + " Current # Players: " + this.curPlayers 
        + " Min. Players: " + this.minPlayers + " Max Players: " + this.maxPlayers +" UUID:"+id.toString(); 
    }
}
