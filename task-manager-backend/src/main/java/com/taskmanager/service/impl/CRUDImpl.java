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
        LOG.infof("Creating entity: %s", t);
        PanacheRepository<T> repo = this.getRepo();
        repo.persist(t);
        return t;
    }

    @Override
    @Transactional
    public T update(T t) {
        LOG.infof("Updating entity: %s", t);
        PanacheRepository<T> repo = this.getRepo();
        return repo.getEntityManager().merge(t);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        LOG.infof("Deleting entity with ID: %s", id);
        PanacheRepository<T> repo = this.getRepo();
        repo.delete("id", id);
    }

    @Override
    public Optional<T> findById(ID id) {
        LOG.infof("Finding entity with ID: %s", id);
        PanacheRepository<T> repo = this.getRepo();
        return repo.findByIdOptional((Long)id);
    }

    @Override
    public List<T> findAll() {
        LOG.infof("Finding all entities");
        PanacheRepository<T> repo = this.getRepo();
        return repo.listAll();
    }
}