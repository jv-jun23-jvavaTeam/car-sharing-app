package com.jvavateam.carsharingapp.repository;

public interface SpecificationProviderManager<T> {
    SpecificationBooleanProvider<T> getBooleanSpecificationProvider(String key);
    SpecificationLongProvider<T> getLongSpecificationProvider(String key);
}
