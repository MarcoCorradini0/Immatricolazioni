package it.marcocorradini.immatricolazioni.web.resource;

import it.marcocorradini.immatricolazioni.persistence.entity.ImmatricolazioneEntity;
import it.marcocorradini.immatricolazioni.persistence.repository.ImmatricolazioneRepository;
import it.marcocorradini.immatricolazioni.web.model.ImmatricolazioneModel;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/immatricolazioni")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImmatricolazioneResource {
    @Inject
    ImmatricolazioneRepository repository;

    private void updateEntity(ImmatricolazioneEntity e, ImmatricolazioneModel m) {
        e.targa = m.targa;
        e.numeroTelaio = m.numeroTelaio;
        e.dataImmatricolazione = m.dataImmatricolazione;
        e.comuneImmatricolazione = m.comuneImmatricolazione;
        e.codiceFiscaleProprietario = m.codiceFiscaleProprietario;
        e.partitaIvaProprietario = m.partitaIvaProprietario;
        e.classeVeicolo = m.classeVeicolo;
        e.scadenzaRevisione = m.scadenzaRevisione;
        e.radiato = m.radiato != null ? m.radiato : false;
    }

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
        updateEntity(entity, model);
        repository.persist(entity);
        model.id=entity.id;
        return Response.status(Response.Status.CREATED)
                .entity(model)
                .build();
    }

    @GET
    public Response getAll(){
        return Response.ok(
                repository.list("eliminato=false")
        ).build();
    }

    @GET // By id
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id){
        ImmatricolazioneEntity entity=repository.findById(id);
        if (entity==null||Boolean.TRUE.equals(entity.eliminato)){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Immatricolazione non trovata")
                    .build();
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        ImmatricolazioneEntity entity=repository.findById(id);
        if (entity==null||Boolean.TRUE.equals(entity.eliminato)){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Immatricolazione non trovata")
                    .build();
        }
        entity.eliminato=true;
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateById(@PathParam("id") Long id, ImmatricolazioneModel model){
        ImmatricolazioneEntity entity=repository.findById(id);
        if (entity==null||Boolean.TRUE.equals(entity.eliminato)){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Immatricolazione non trovata")
                    .build();
        }
        if (!entity.targa.equals(model.targa) && repository.find("targa", model.targa).firstResult()!=null){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Targa già esistente")
                    .build();
        }
        if (!entity.getNumeroTelaio().equals(model.numeroTelaio) && repository.find("numeroTelaio", model.numeroTelaio).firstResult()!=null){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Telaio già esistente")
                    .build();
        }
        updateEntity(entity, model);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/search")
    public Response search(
            @QueryParam("targa") String targa,
            @QueryParam("owner") String owner
    ){
        // Ricerca targa
        if (targa!=null&&!targa.isBlank()){
            return Response.ok(
                    repository.list(
                            "targa like ?1 and eliminato = false",
                            "%"+targa+"%"
                    )
            ).build();
        }
        // Ricerca proprietario
        if (owner!=null&&!owner.isBlank()){
            return Response.ok(
                    repository.list(
                            "(codiceFiscaleProprietario like ?1"+
                                    "or partitaIvaProprietario like ?1)"+
                                    "and eliminato = false",
                            "%"+owner+"%"
                    )
            ).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Specificare targa o owner")
                .build();
    }
}












