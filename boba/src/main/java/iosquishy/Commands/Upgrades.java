package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Upgrades {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("upgrades")
            .setDescription("Buy upgrades to make your cafe more successful!");
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}
