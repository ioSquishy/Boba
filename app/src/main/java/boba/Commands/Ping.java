package boba.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Ping {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("ping")
            .setDescription("Pong!");
    }

    public static void handleCommand (SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}