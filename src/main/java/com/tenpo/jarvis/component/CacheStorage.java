package com.tenpo.jarvis.component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Scope(value = "singleton")
public class CacheStorage {

    CacheLoader<String, String> loader = new CacheLoader<String, String>() {
        @Override
        public String load(String key) {
            return key.toUpperCase();
        }
    };;

    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(loader);

    public void persist(final String key, final String value){
        cache.put(key, value);
    }

    public void remove(){
        cache.cleanUp();
    }

    public BigDecimal getFromCache(final String key) throws ExecutionException {
        return new BigDecimal(cache.get(key));
    }

    public void setConfiguration(TimeUnit timeUnit, Integer duration) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(duration, timeUnit)
                .build(loader);
    }
}
