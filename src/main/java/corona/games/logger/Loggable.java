package corona.games.logger;

import java.util.logging.Logger;

/**
 * Created by noamannenberg
 * on 4/5/20.
 */
public interface Loggable {
    void setLogger(Logger logger);
    void log(String msg);
}
