package org.openshift;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import java.util.function.Function;

import static java.lang.String.format;
import static java.lang.System.getenv;

public final class ServiceLocator {

    private static final ServiceLocator INSTANCE = new ServiceLocator();

    private final ClassToInstanceMap beans;

    private ServiceLocator() {
        this.beans = MutableClassToInstanceMap.create();
    }

    public static ServiceLocator getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private <T> T singleton(final Class<T> key, final Function<? super Class<T>, ? extends T> mappingFunction) {
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (key) {
            return (T) beans.computeIfAbsent(key, mappingFunction);
        }
    }

    public InsultGenerator insultGenerator() {
        return singleton(InsultGenerator.class, k -> new InsultGenerator(insultRepository()));
    }

    private InsultRepository insultRepository() {
        return singleton(InsultRepository.class, k -> new InsultRepository(repositoryTemplate()));
    }

    private RepositoryTemplate repositoryTemplate() {
        return singleton(RepositoryTemplate.class, k -> new RepositoryTemplate(
                format("jdbc:postgresql://%s/%s", getenv("POSTGRESQL_SERVICE_HOST"), getenv("POSTGRESQL_DATABASE")),
                getenv("POSTGRESQL_USER"),
                getenv("PGPASSWORD")));
    }

}
