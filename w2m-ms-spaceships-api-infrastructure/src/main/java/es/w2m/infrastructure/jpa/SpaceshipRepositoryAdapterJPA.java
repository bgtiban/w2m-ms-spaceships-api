package es.w2m.infrastructure.jpa;

import es.w2m.domain.ISpaceshipRepositoryPort;
import es.w2m.domain.PageableDomain;
import es.w2m.domain.SpaceshipDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@NoArgsConstructor
public class SpaceshipRepositoryAdapterJPA implements ISpaceshipRepositoryPort {

    @Autowired
    private ISpaceshipRepository repository;

    @Autowired
    private ISpaceShipPageableMapperInfra mapper;

    @Override
    public PageableDomain searchSpaceships(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return mapper.toPageableDomain(repository.findByNameContainingIgnoreCase(name, pageable));
    }

    @Override
    public SpaceshipDomain searchSpaceshipById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("No existe nave con id: " + id)));
    }

    @Override
    public SpaceshipDomain createSpaceship(SpaceshipDomain newSpaceship) {
        SpaceshipEntity spaceship = mapper.toEntity(newSpaceship);
        return mapper.toDomain(repository.save(spaceship));
    }

    @Override
    public SpaceshipDomain updateSpaceship(SpaceshipDomain spaceship) {
        return mapper.toDomain(repository.save(mapper.toEntity(spaceship)));
    }

    @Override
    public void deleteSpaceshipById(Long spaceshipId) {
        repository.deleteById(spaceshipId);
    }
}