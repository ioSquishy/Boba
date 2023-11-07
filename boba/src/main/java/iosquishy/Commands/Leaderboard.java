package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionType;

public class Leaderboard {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("leaderboard")
            .setDescription("See who has the best cafe!")
            .addOption(new SlashCommandOptionBuilder().setName("global").setDescription("View global leaderboard").setType(SlashCommandOptionType.BOOLEAN).setRequired(true).build())
            .setEnabledInDms(false);
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}
