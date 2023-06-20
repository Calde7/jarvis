package com.tenpo.jarvis.service.tracking;

import com.tenpo.jarvis.entity.Tracking;
import com.tenpo.jarvis.repository.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class TrackingServiceImpl implements TrackingService{

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private Executor executor;

    @Override
    @Async
    public CompletableFuture<Tracking> save(final Tracking tracking) {
        return CompletableFuture.supplyAsync( () -> {
            return trackingRepository.save(tracking);
        }, executor).exceptionally(ex -> { return null; });
    }

    public List<Tracking> findAll(final Integer offset, final Integer pageSize) {
        return trackingRepository.findAll(PageRequest.of(offset, pageSize)).getContent();
    }
}
