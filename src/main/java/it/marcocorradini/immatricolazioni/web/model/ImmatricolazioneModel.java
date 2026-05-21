package it.marcocorradini.immatricolazioni.web.model;

import it.marcocorradini.immatricolazioni.persistence.entity.ClasseVeicolo;

import java.time.LocalDate;

public class ImmatricolazioneModel {
    public Long id;
    public String targa;
    public String numeroTelaio;
    public LocalDate dataImmatricolazione;
    public String comuneImmatricolazione;
    public String codiceFiscaleProprietario;
    public String partitaIvaProprietario;
    public ClasseVeicolo classeVeicolo;
    public LocalDate scadenzaRevisione;
    public Boolean radiato;
    public Boolean eliminato;
}
