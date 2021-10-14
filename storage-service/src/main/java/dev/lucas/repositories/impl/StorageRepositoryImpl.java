package dev.lucas.repositories.impl;

import dev.lucas.repositories.StorageRepository;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StorageRepositoryImpl implements StorageRepository {

    @Override
    public int findStockById(String uuid) {
        return new Random().nextInt(2);
    }

    @Override
    public void save(int i) {

    }
}
