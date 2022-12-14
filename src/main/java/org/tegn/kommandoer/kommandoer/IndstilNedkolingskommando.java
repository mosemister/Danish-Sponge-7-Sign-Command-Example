package org.tegn.kommandoer.kommandoer;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.tegn.kommandoer.SignerData;
import org.tegn.kommandoer.TegnNytte;

import java.io.IOException;
import java.util.Optional;

public class IndstilNedkolingskommando {

    private static final Text cooldownParameterNogle = Text.of("kol ned");

    public static class Kommandokorsel implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource kommandoAfsender, CommandContext kommandoerKontekst) throws CommandException {
            if(!(kommandoAfsender instanceof Player)){
                throw new CommandException(Text.of("Kun spillerkommando"));
            }
            Player spiller = (Player)kommandoAfsender;

            //henter nedkølingen fra kommandoen. Hvis en ikke indtastes, bruger den nedkølingsværdien 0
            long nedkoling = kommandoerKontekst.<Long>getOne(cooldownParameterNogle).orElse(0L);
            Optional<Location<World>> potationsskiltetsPlacering = TegnNytte.faSePaSkiltet(spiller);
            if(!potationsskiltetsPlacering.isPresent()){
                throw new CommandException(Text.of("Ser ikke på skiltet"));
            }
            Location<World> blokForanSpilleren = potationsskiltetsPlacering.get();
            SignerData kommandodata = new SignerData(blokForanSpilleren);
            try {
                kommandodata.indstilleNedkoling(nedkoling);
            } catch (IOException e) {
                //viser crash til konsollen
                e.printStackTrace();
                //Fortæller brugeren, at det ikke lykkedes at gemme
                throw new CommandException(Text.of("Ændringerne kunne ikke gemmes"));
            }

            return CommandResult.success();
        }
    }

    public static CommandSpec opretteKommando(){
        return CommandSpec
                .builder()
                //Indstiller, hvad der skal køre, når kommandoen udføres
                .executor(new Kommandokorsel())
                //Angiver, at der er 1 argument af en lang (64 bit heltal) type
                .arguments(GenericArguments.longNum(cooldownParameterNogle))
                .build();
    }
}
