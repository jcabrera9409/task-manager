package com.taskmanager.service.impl;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;

import com.taskmanager.service.ICRUD;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {
    private static final Logger LOG = Logger.getLogger(CRUDImpl.class);

    protected abstract PanacheRepository<T> getRepo();

    @Override
    @Transactional
    public T create(T t) {
        LOG.info("Creating entity: " + t);
        PanacheRepository<T> repo = this.getRepo();
        repo.persist(t);
        return t;
    }

    @Override
    @Transactional
    public T update(T t) {
        LOG.info("Updating entity: " + t);
        PanacheRepository<T> repo = this.getRepo();
        return repo.getEntityManager().merge(t);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        LOG.info("Deleting entity with ID: " + id);
        PanacheRepository<T> repo = this.getRepo();
        repo.delete("id", id);
    }

    @Override
    public Optional<T> findById(ID id) {
        LOG.info("Finding entity with ID: " + id);
        PanacheRepository<T> repo = this.getRepo();
        return repo.findByIdOptional((Long)id);
    }

    @Override
    public List<T> findAll() {
        LOG.info("Finding all entities");
        PanacheRepository<T> repo = this.getRepo();
        return repo.listAll();
    }
}