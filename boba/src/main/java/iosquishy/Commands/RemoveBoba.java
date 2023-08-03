package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class RemoveBoba {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("removeboba")
            .setDescription("Removes a boba from your cafe.");
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}
