package iosquishy;
import iosquishy.Commands.*;
import iosquishy.Utility.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommandInteraction;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.GUILD_MESSAGES).login().join();

    public static void main(String[] args) {

        Data.initMongoDB();
        
        BobaGod.initBobaMaps();
        MenuCompiler.initMenuMaps();
        
        System.out.println("yahallo");

        //Test Commands [REMOVE BEFORE RELEASE!]
        api.addMessageCreateListener(event -> {
            String cmd = event.getMessageContent();
            switch (cmd) {
                case "ping" : event.getMessage().reply("idk lol"); break;
            }
        });
        
        //Create Commands
        Ping.createCommand().createGlobal(api).join();
        
        //Run Commands
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            //if database is down dont run commands
            if (!Data.mongoOK) { 
                interaction.createImmediateResponder().setContent("Database is currently down :(").respond();
                return;
            }
            //actual commands
            switch (interaction.getCommandName().toString()) {
                case "ping" : Ping.runCommand(interaction); break;
            }
        });
    }
}
