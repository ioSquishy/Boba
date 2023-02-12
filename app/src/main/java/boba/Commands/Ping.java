package boba.Commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;

public class Ping implements CommandExecutor {

    @Command(aliases = "ping", description = "Pong!")
    public String handleCommand (Message message) {
        return ("Pong! `" + message.getApi().getLatestGatewayLatency().toMillis() + "ms`");
    }
}