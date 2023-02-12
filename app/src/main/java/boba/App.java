package boba;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import boba.Commands.Ping;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("KANNA_TOKEN")).setAllIntents().login().join();

    public static void main(String[] args) {
        System.out.println("yahallo");
        
        CommandHandler handler = new JavacordHandler(api);
        handler.setDefaultPrefix("!");
        handler.registerCommand(new Ping());
        
    }
}
