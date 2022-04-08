package com.coffeerr.registry;

public interface ServiceRegistry {
    <T> void register(T service);

    <T> T getService(String serviceName);
}
