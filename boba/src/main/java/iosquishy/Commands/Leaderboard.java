package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Leaderboard {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("leaderboard")
            .setDescription("See who has the best cafe!")
            .setBooleanOption("global")
            .setEnabledInDms(false);
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}
