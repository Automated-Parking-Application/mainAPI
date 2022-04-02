package com.capstone.parking.config;

import java.util.Arrays;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    Cache singlePostSeoUrlCache = new ConcurrentMapCache("parkingReservation");
    Cache parkingReservationByIdCache = new ConcurrentMapCache("parkingReservationById");

    Cache qrCache = new ConcurrentMapCache("qr");

    cacheManager.setCaches(Arrays.asList(singlePostSeoUrlCache, qrCache, parkingReservationByIdCache));
    return cacheManager;
  }
}
