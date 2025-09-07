package com.taskmanager.service.impl;

import java.util.List;
import java.util.Optional;

import com.taskmanager.service.ICRUD;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

    protected abstract PanacheRepository<T> getRepo();

    @Override
    @Transactional
    public T create(T t) {
        PanacheRepository<T> repo = getRepo();
        repo.persist(t);
        return t;
    }

    @Override
    @Transactional
    public T update(T t) {
        PanacheRepository<T> repo = getRepo();
        return repo.getEntityManager().merge(t);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        PanacheRepository<T> repo = getRepo();
        if (id instanceof Integer) {
            repo.delete("id", id);
        } else {
            repo.deleteById((Long) id);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        PanacheRepository<T> repo = getRepo();
        if (id instanceof Integer) {
            return repo.find("id", id).firstResultOptional();
        } else {
            return repo.findByIdOptional((Long) id);
        }
    }

    @Override
    public List<T> findAll() {
        PanacheRepository<T> repo = getRepo();
        return repo.listAll();
    }
}