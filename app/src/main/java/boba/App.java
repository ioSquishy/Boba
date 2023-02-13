package boba;
import boba.Commands.*;
import boba.Utility.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).login().join();

    public static void main(String[] args) {
        BobaGod.initBobaMaps();
        SettingCompiler.initSettingMaps();
        
        System.out.println("yahallo");
        
        //Create Commands
        Ping.createCommand().createGlobal(api).join();
        
        //Run Commands
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            switch (interaction.getCommandName().toString()) {
                case "ping" : Ping.handleCommand(interaction); break;
            }
        });
    }
}
