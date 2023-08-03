package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Help {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("help")
            .setDescription("Get an overview of what to do!");
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}
