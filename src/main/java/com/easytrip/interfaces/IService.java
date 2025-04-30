package com.easytrip.interfaces;

import java.util.List;

public interface IService<T> {
    void ajouterEntity(T t);
    void supprimerEntity(T t);
    void modifierEntity(T t);
    List<T> getAllData();
}
