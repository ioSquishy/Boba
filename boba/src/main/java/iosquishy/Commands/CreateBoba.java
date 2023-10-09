package iosquishy.Commands;

import java.util.concurrent.TimeUnit;

import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import iosquishy.Player;
import iosquishy.ImageGen.BobaGod;

public class CreateBoba {
    private static final short[] coinCost = {0, 500, 1000, 1500, 2000};
    private static final byte maxBobas = (byte) coinCost.length;
    public static SlashCommandBuilder createCommand () {
        return new SlashCommandBuilder()
            .setName("createboba")
            .setDescription("Create a new boba!")
            .setEnabledInDms(false);
    }

    public static void runCommand(SlashCommandInteraction interaction) {
        long userID = interaction.getUser().getId();
        byte futureBobaQuant = (byte) Player.getBobas(userID, interaction.getChannel().orElse(null))[0].length;
        if (futureBobaQuant >= maxBobas) {
            interaction.createImmediateResponder().setContent("You have reached your boba capacity! Delete one to make another. You can still play around with the editor though.").respond();
        }
        if (Player.getCoins(userID) < coinCost[futureBobaQuant]) { //if player doesnt have enough coins
                interaction.createImmediateResponder().setContent("You need `" + coinCost[futureBobaQuant] + "` coins to purchase a new boba! You can still play around with the editor though.").respond();
        }
        BobaGod bobaGod = new BobaGod(userID);
    }
}
