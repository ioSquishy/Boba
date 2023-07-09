package boba;
import boba.Commands.*;
import boba.Utility.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommandInteraction;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    //public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.GUILD_MESSAGES).login().join();

    public static void main(String[] args) {

        Data.initMongoDB();
        Data.updateAllUsers();
        //Data.test();
        
        /*BobaGod.initBobaMaps();
        SettingCompiler.initSettingMaps();
        
        System.out.println("yahallo");

        //Test Commands [REMOVE BEFORE RELEASE!]
        api.addMessageCreateListener(event -> {

        });
        
        //Create Commands
        Ping.createCommand().createGlobal(api).join();
        
        //Run Commands
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            switch (interaction.getCommandName().toString()) {
                case "ping" : Ping.handleCommand(interaction); break;
            }
        });*/
    }
}
