package it.marcocorradini.immatricolazioni.web.resource;

import it.marcocorradini.immatricolazioni.persistence.entity.ImmatricolazioneEntity;
import it.marcocorradini.immatricolazioni.persistence.repository.ImmatricolazioneRepository;
import it.marcocorradini.immatricolazioni.web.model.ImmatricolazioneModel;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/immatricolazioni")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImmatricolazioneResource {
    @Inject
    ImmatricolazioneRepository repository;

    @POST
    @Transactional
    public Response create(ImmatricolazioneModel model){
        // Validazione
        if (model.targa==null||model.targa.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Targa obbligatoria")
                    .build();
        }
        if (model.numeroTelaio==null||model.numeroTelaio.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Numero telaio obbligatorio")
                    .build();
        }
        if (model.dataImmatricolazione==null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Data immatricolazione obbligatoria")
                    .build();
        }
        if (model.comuneImmatricolazione==null||model.comuneImmatricolazione.isBlank()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Comune obbligatorio")
                    .build();
        }
        if (model.classeVeicolo==null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Classe veicolo obbligatoria")
                    .build();
        }
        if (model.scadenzaRevisione==null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Scadenza revisione obbligatoria")
                    .build();
        }
        // CF / PIVA
        boolean cfVuoto=model.codiceFiscaleProprietario==null||model.codiceFiscaleProprietario.isBlank();
        boolean pivaVuota=model.partitaIvaProprietario==null||model.partitaIvaProprietario.isBlank();
        if (cfVuoto&&pivaVuota){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Codice fiscale o partita IVA obbligatori")
                    .build();
        }
        // Controllo targhe duplciate e telai
        if (repository.find("targa", model.targa).firstResult()!=null){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Targa già esistente")
                    .build();
        }
        if (repository.find("numeroTelaio", model.numeroTelaio).firstResult()!=null){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Telaio già esistente")
                    .build();
        }

        ImmatricolazioneEntity entity=new ImmatricolazioneEntity();
        entity.targa=model.targa;
        entity.numeroTelaio=model.numeroTelaio;
        entity.dataImmatricolazione=model.dataImmatricolazione;
        entity.comuneImmatricolazione=model.comuneImmatricolazione;
        entity.codiceFiscaleProprietario=model.codiceFiscaleProprietario;
        entity.partitaIvaProprietario=model.partitaIvaProprietario;
        entity.classeVeicolo=model.classeVeicolo;
        entity.scadenzaRevisione=model.scadenzaRevisione;
        entity.radiato=model.radiato!=null?model.radiato:false;

        repository.persist(entity);
        model.id=entity.id;

        return Response.status(Response.Status.CREATED)
                .entity(model)
                .build();
    }


}
