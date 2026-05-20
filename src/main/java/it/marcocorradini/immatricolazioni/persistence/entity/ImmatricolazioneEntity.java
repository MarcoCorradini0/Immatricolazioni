package it.marcocorradini.immatricolazioni.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="immatricolazioni")
public class ImmatricolazioneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String targa;
    @Column(nullable = false, unique = true)
    public String numeroTelaio;
    @Column(nullable = false)
    public LocalDate dataImmatricolazione;
    @Column(nullable = false)
    public String comuneImmatricolazione;
    public String codiceFiscaleProprietario;
    public String partitaIvaProprietario;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ClasseVeicolo classeVeicolo;
    @Column(nullable = false)
    public LocalDate scadenzaRevisione;
    @Column(nullable = false)
    public Boolean radiato=false;

    public Boolean getEliminato() {
        return eliminato;
    }

    public void setEliminato(Boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Boolean getRadiato() {
        return radiato;
    }

    public void setRadiato(Boolean radiato) {
        this.radiato = radiato;
    }

    public LocalDate getScadenzaRevisione() {
        return scadenzaRevisione;
    }

    public void setScadenzaRevisione(LocalDate scadenzaRevisione) {
        this.scadenzaRevisione = scadenzaRevisione;
    }

    public ClasseVeicolo getClasseVeicolo() {
        return classeVeicolo;
    }

    public void setClasseVeicolo(ClasseVeicolo classeVeicolo) {
        this.classeVeicolo = classeVeicolo;
    }

    public String getPartitaIvaProprietario() {
        return partitaIvaProprietario;
    }

    public void setPartitaIvaProprietario(String partitaIvaProprietario) {
        this.partitaIvaProprietario = partitaIvaProprietario;
    }

    public String getCodiceFiscaleProprietario() {
        return codiceFiscaleProprietario;
    }

    public void setCodiceFiscaleProprietario(String codiceFiscaleProprietario) {
        this.codiceFiscaleProprietario = codiceFiscaleProprietario;
    }

    public String getComuneImmatricolazione() {
        return comuneImmatricolazione;
    }

    public void setComuneImmatricolazione(String comuneImmatricolazione) {
        this.comuneImmatricolazione = comuneImmatricolazione;
    }

    public LocalDate getDataImmatricolazione() {
        return dataImmatricolazione;
    }

    public void setDataImmatricolazione(LocalDate dataImmatricolazione) {
        this.dataImmatricolazione = dataImmatricolazione;
    }

    public String getNumeroTelaio() {
        return numeroTelaio;
    }

    public void setNumeroTelaio(String numeroTelaio) {
        this.numeroTelaio = numeroTelaio;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public Boolean eliminato=false;
}
