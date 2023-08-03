package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class MyCafe {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("mycafe")
            .setDescription("See your cafe's menu and stats.")
            .setEnabledInDms(false);
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}