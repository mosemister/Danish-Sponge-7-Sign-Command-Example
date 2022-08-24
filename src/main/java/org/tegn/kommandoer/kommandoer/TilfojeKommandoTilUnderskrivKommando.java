package org.tegn.kommandoer.kommandoer;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.tegn.kommandoer.SignerData;
import org.tegn.kommandoer.TegnNytte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TilfojeKommandoTilUnderskrivKommando {

    private static final Text kommandotast = Text.of("kommando");

    public static class Eksekutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource kommandoAfsender, CommandContext kommandoerKontekst) throws CommandException {
            if(!(kommandoAfsender instanceof Player)){
                throw new CommandException(Text.of("Kun spillerkommando"));
            }
            Player spiller = (Player)kommandoAfsender;

            //henter nedkølingen fra kommandoen. Hvis en ikke indtastes, bruger den nedkølingsværdien 0
            String tilfoje = kommandoerKontekst.<String>getOne(kommandotast).orElse("");
            Optional<Location<World>> potationsskiltetsPlacering = TegnNytte.faSePaSkiltet(spiller);
            if(!potationsskiltetsPlacering.isPresent()){
                throw new CommandException(Text.of("Ser ikke på skiltet"));
            }
            Location<World> blokForanSpilleren = potationsskiltetsPlacering.get();
            SignerData kommandodata = new SignerData(blokForanSpilleren);
            try {
                List<String> originaleKommandoer =kommandodata.faKommandoer();
                List<String> kopiAfKommandoer = new ArrayList<>(originaleKommandoer);
                kopiAfKommandoer.add(tilfoje);
                kommandodata.indstilleKommandoer(kopiAfKommandoer);
                spiller.sendMessage(Text.of("Tilføjet kommando til at underskrive"));
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
                .executor(new Eksekutor())
                //Angiver, at der er 1 argument af en lang (64 bit heltal) type
                .arguments(GenericArguments.remainingJoinedStrings(kommandotast))
                .build();
    }
}
