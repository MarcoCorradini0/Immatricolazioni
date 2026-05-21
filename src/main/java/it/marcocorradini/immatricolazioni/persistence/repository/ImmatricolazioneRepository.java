package it.marcocorradini.immatricolazioni.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.marcocorradini.immatricolazioni.persistence.entity.ImmatricolazioneEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImmatricolazioneRepository implements PanacheRepository<ImmatricolazioneEntity> {
}
