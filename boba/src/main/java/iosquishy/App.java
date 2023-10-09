package iosquishy;
import iosquishy.Commands.*;
import iosquishy.ImageGen.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static final DiscordApi api = new DiscordApiBuilder().setToken(Dotenv.load().get("TOKEN")).setAllNonPrivilegedIntentsAnd(Intent.MESSAGE_CONTENT).login().join();

    public static void main(String[] args) {

        //Data.initMongoDB();
        
        BobaGod.initBobaMaps();
        MenuCompiler.initMenuMaps();
        BobaGod.startBobaGodCleaner();
        
        System.out.println("Saiko is online!");

        //Test Commands [REMOVE BEFORE RELEASE!]
        api.addMessageCreateListener(event -> {
            String cmd = event.getMessageContent();
            switch (cmd) {
                case "ping" : event.getMessage().reply("idk lol"); break;
                case "embed" : event.getMessage().reply(new EmbedBuilder().setTitle("hai").setImage("https://cdn.discordapp.com/attachments/818275525797609472/1132459889692770416/image.png")).join(); break;
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
