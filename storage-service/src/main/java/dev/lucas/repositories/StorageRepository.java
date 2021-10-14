package dev.lucas.repositories;

public interface StorageRepository {

    int findStockById(String uuid);

    void save(int i);
}
