package com.tenpo.jarvis.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CacheStorageTest {

    @InjectMocks
    CacheStorage cacheStorage;

    @Test
    void test_storage_cached_value_with_empty_cache() throws ExecutionException {
        cacheStorage.setConfiguration(TimeUnit.MILLISECONDS, 200);

        NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> {
            BigDecimal percentage = cacheStorage.getFromCache("percentage");
        });
    }

    @Test
    void test_storage_cached_value_ok() throws ExecutionException {
        cacheStorage.persist("percentage", "10");
        BigDecimal percentage = cacheStorage.getFromCache("percentage");
        assertTrue("10".equals(percentage.toString()));
    }

    @Test
    void test_percentage_storage_cached_value_expired() throws ExecutionException, InterruptedException {
        cacheStorage.setConfiguration(TimeUnit.MILLISECONDS, 200);
        cacheStorage.persist("percentage", "10");
        Thread.sleep(250);
        NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> {
            BigDecimal percentage = cacheStorage.getFromCache("percentage");
        });
    }
}
