package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface Dao <K,E>{
    boolean delete(K id);
    Optional<E> findById(K id);
    boolean update(E ticket);
    List<E> findAll();
    E save(E ticket);
}