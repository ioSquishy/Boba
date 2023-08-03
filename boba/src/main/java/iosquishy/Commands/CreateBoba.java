package iosquishy.Commands;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class CreateBoba {
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("createboba")
            .setDescription("Create a new boba!")
            .setEnabledInDms(false);
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder().setContent("Pong! `" + interaction.getApi().getLatestGatewayLatency().toMillis() + "ms`").respond();
    }
}
