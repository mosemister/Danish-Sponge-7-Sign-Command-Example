package org.tegn.kommandoer;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class SignerData {

    private final Location<World> tegnPlacering; //Skiltenes placering
    private final File datafil; //Datafilen tilhørende dette skilt


    private static final Object[] kommandonode = {"kommandoer"};
    private static final Object[] nedkolingsknude = {"kol ned"};

    public SignerData(Location<World> tegnPlacering){
        this.tegnPlacering = tegnPlacering; //Dette tildeler parameteren (data, der kommer ind) til feltet
        File dataMappe = Sponge.getConfigManager().getPluginConfig(TegnKommandoer.faEksempel()).getDirectory().toFile();
        //Opretter en fil med det samme af placeringerne XYZ efterfulgt af verdens unikke id
        this.datafil = new File(dataMappe, "tegn/" + tegnPlacering.getBlockX() + "-" + tegnPlacering.getBlockY() + "-" + tegnPlacering.getBlockZ() + "-" + tegnPlacering.getExtent().getUniqueId() + ".conf");
    }

    public Location<World> faPlacering(){
        return this.tegnPlacering;
    }

    public boolean kolerNed(){
        return TegnKommandoer.faEksempel().kolerNed(this.tegnPlacering);
    }

    public void aetAfkoling(){
        TegnKommandoer.faEksempel().registrereSkiltNedkoling(this.tegnPlacering);
        //Planlægger koden inde til at køre for nedkølingstiden i flueben
        Sponge
                .getScheduler()
                .createTaskBuilder()
                //Sætter nedkølingen i flueben
                .delayTicks(this.faNedkoling())
                //indstiller, hvordan den skal køre. Kald i dette tilfælde aetKoletNed-metoden
                .execute(this::aetKoletNed)
                //Indstiller navnet på planlæggeren
                .name("Forsink indtil nedkølingen stopper")
                //Starter skemalæggeren
                .submit(TegnKommandoer.faEksempel());
    }

    public void aetKoletNed(){
        TegnKommandoer.faEksempel().afmeldSkilteCooldown(this.tegnPlacering);
    }

    public long faNedkoling(){
        HoconConfigurationLoader filindaeser = HoconConfigurationLoader.builder().setFile(this.datafil).build();
        ConfigurationNode rodfilNode;
        //Forsøg på at indlæse indholdet af filen. Hvis det mislykkes, vil det returnere en nedkøling på 0
        try {

            rodfilNode= filindaeser.load();
        } catch (IOException e) {
            return 0;
        }
        //Forsøg på at få nedkøling fra filen. Hvis det mislykkes, vil det returnere en nedkøling på 0
        return rodfilNode.getNode(nedkolingsknude).getLong(0);
    }

    public List<String> faKommandoer(){
        //Dette er, hvad du vil gøre
        throw new RuntimeException("erstatte mig med din kode");
    }

    public void indstilleNedkoling(long kolned) throws IOException {
        //Dette er, hvad du vil gøre
    }

    public void indstilleKommandoer(Collection<String> kommandoer) throws IOException {
        HoconConfigurationLoader filindaeser = HoconConfigurationLoader.builder().setFile(this.datafil).build();
        ConfigurationNode rodfilNode;
        //Forsøg på at indlæse indholdet af filen, hvis det mislykkes, opretter det en tom udgave af filen
        try {
            rodfilNode = filindaeser.load();
        } catch (IOException e) {
            rodfilNode = filindaeser.createEmptyNode();
        }
        //Gemmer værdien ved den angivne node
        rodfilNode.getNode(kommandonode).setValue(kommandoer);
        //Gemmer rodnoden til filen på computeren
        filindaeser.save(rodfilNode);
    }


}
