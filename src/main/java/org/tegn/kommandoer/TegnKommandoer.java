package org.tegn.kommandoer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.tegn.kommandoer.kommandoer.IndstilNedkolingskommando;
import org.tegn.kommandoer.kommandoer.TilfojeKommandoTilUnderskrivKommando;

import java.util.LinkedHashSet;
import java.util.LinkedList;

@Plugin(id = "tegnkommandoer", version = "1.0.0", name = "Tegn Kommandoer")
public class TegnKommandoer {

    private final LinkedHashSet<Location<World>> tegnPaMedkoling = new LinkedHashSet<>();

    private static TegnKommandoer eksempel; //Dette giver dig mulighed for at vende tilbage til dine plugins-data

    public TegnKommandoer(){
        eksempel = this;
    }

    @Listener
    public void vedOpstartAfPlugin(GameInitializationEvent event){
        //registrerer kommandoerne
        CommandSpec underkommandoer = CommandSpec
                .builder()
                .child(IndstilNedkolingskommando.opretteKommando(), "kolned")
                .child(TilfojeKommandoTilUnderskrivKommando.opretteKommando(),"tilføje")
                .build();


        //registrerer alle underkommandoer under en enkelt kommando
        Sponge.getCommandManager().register(this, underkommandoer, "tegnkommandoer", "tk");
    }

    public void registrereSkiltNedkoling(Location<World> tegnerPlacering){
        this.tegnPaMedkoling.add(tegnerPlacering);
    }

    public boolean kolerNed(Location<World> tegnerPlacering){
        return this.tegnPaMedkoling.contains(tegnerPlacering);
    }

    public void afmeldSkilteCooldown(Location<World> tegnerPlacering){
        this.tegnPaMedkoling.remove(tegnerPlacering);
    }

    public static TegnKommandoer faEksempel(){
        return eksempel;
    }
}
