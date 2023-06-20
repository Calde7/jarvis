package com.tenpo.jarvis.service.generic;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DaoHelper <T> {
    CompletableFuture<T> save(final T t);
    List<T> findAll(final Integer offset, final Integer pageSize);
}
