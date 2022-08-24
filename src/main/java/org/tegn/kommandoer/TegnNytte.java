package org.tegn.kommandoer;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public final class TegnNytte {

    private TegnNytte(){
        throw new RuntimeException("Du bør ikke skabe dette");
    }

    public static Optional<Location<World>> faSePaSkiltet(Entity enhed){
        BlockRay<World> blokKiggerPa = BlockRay
                //strålespor fra spillerens hoved
                .from(enhed)
                //strålespor med en maksimal afstand på 7 blokke
                .distanceLimit(7)
                //filtrer resultaterne til ikke-luftblokke
                .select(BlockRay.<World>notAirFilter())
                //filtrer resultaterne til kun 1 blok, der er ingen luft
                .whilst(BlockRay.continueAfterFilter(BlockRay.<World>onlyAirFilter(), 1))
                //Opret reglerne for strålesporing
                .build();
        BlockRayHit<World> blokIFrontResultat = blokKiggerPa.next();
        Location<World> blokForanSpilleren = blokIFrontResultat.getLocation();
        Optional<TileEntity> potentieltBlokererSaerligeData = blokForanSpilleren.getTileEntity();
        //Tjek om de særlige data er der
        if(!potentieltBlokererSaerligeData.isPresent()){
            return Optional.empty();
        }

        TileEntity saerligeBlokdata = potentieltBlokererSaerligeData.get();
        //Kontrollerer, om de særlige data er et tegns særlige data
        if(!(saerligeBlokdata instanceof Sign)){
            return Optional.empty();
        }
        return Optional.of(blokForanSpilleren);
    }


}
