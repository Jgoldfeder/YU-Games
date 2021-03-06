package corona.games.client.view.lobby;

import java.util.logging.Logger;

import corona.games.logger.Loggable;

/**
 * Created by noamannenberg
 * on 4/5/20.
 */
public class GameRoom implements Loggable{
    private Logger logger;

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(String msg) {
        if(this.logger == null) return;
        else this.logger.info(msg);
    }
}