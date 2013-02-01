package com.vityuk.ginger;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.vityuk.ginger.loader.PropertiesLocalizationLoader;
import com.vityuk.ginger.loader.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DefaultLocalizationProvider implements LocalizationProvider {
    public static final char LOCALE_SEPARATOR = '_';
    public static final char FILE_EXTENSION_SEPARATOR = '.';

    private final LocaleResolver localeResolver;
    private final ResourceLoader resourceLoader;
    private final PropertiesLocalizationLoader localizationLoader;
    private final List<String> locations;

    private final LoadingCache<Locale, PropertyResolver> propertyResolverCache;

    private DefaultLocalizationProvider(Builder builder) {
        localeResolver = builder.localeResolver;
        resourceLoader = builder.resourceLoader;
        localizationLoader = builder.localizationLoader;
        locations = builder.locations;

        CacheBuilder<Object, Object> cacheBuilder = createCacheBuilder(builder);
        propertyResolverCache = cacheBuilder
                .build(new CacheLoader<Locale, PropertyResolver>() {
                    @Override
                    public PropertyResolver load(Locale locale) throws Exception {
                        return createPropertyResolver(locale);
                    }
                });
    }

    @Override
    public String getString(String key) {
        return getPropertyResolver().getString(key);
    }

    @Override
    public Boolean getBoolean(String key) {
        return getPropertyResolver().getBoolean(key);
    }

    @Override
    public Integer getInteger(String key) {
        return getPropertyResolver().getInteger(key);
    }

    @Override
    public Long getLong(String key) {
        return getPropertyResolver().getLong(key);
    }

    @Override
    public Float getFloat(String key) {
        return getPropertyResolver().getFloat(key);
    }

    @Override
    public Double getDouble(String key) {
        return getPropertyResolver().getDouble(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return getPropertyResolver().getList(key);
    }

    @Override
    public Map<String, String> getStringMap(String key) {
        return getPropertyResolver().getMap(key);
    }

    @Override
    public String getMessage(String key, Object... arguments) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private PropertyResolver getPropertyResolver() {
        Locale locale = localeResolver.getLocale();
        return getOrCreatePropertyResolver(locale);
    }

    private PropertyResolver getOrCreatePropertyResolver(Locale locale) {
        try {
            return propertyResolverCache.get(locale);
        } catch (ExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private PropertyResolver createPropertyResolver(Locale locale) {
        List<PropertyResolver> propertyResolvers = Lists.newArrayListWithCapacity(locations.size());
        for (String location : locations) {
            propertyResolvers.add(createPropertyResolver(location, locale));
        }
        return createMultiPropertyResolver(propertyResolvers);
    }

    private PropertyResolver createPropertyResolver(String location, Locale locale) {
        InputStream inputStream = openLocation(location, locale);
        try {
            return loadLocalization(location, inputStream);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }


    private InputStream openLocation(String location, Locale locale) {
        if (!resourceLoader.isSupported(location)) {
            throw new RuntimeException("Unsupported location: '" + location + "'");
        }
        return findResourceForLocale(location, locale);
    }

    private InputStream findResourceForLocale(String location, Locale locale) {
        int idx = location.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        if (idx == -1) {
            throw new RuntimeException("Localization resource should have extension in location: '" + location + "'");
        }
        String prefix = location.substring(0, idx);
        String suffix = location.substring(idx);

        for (Locale candidateLocale : createCandidateLocales(locale)) {
            String localizedLocation = createLocalizedLocation(prefix, suffix, candidateLocale);
            InputStream inputStream = openStream(localizedLocation);
            if (inputStream != null) {
                return inputStream;
            }
        }

        throw new RuntimeException("Unable to find localization resource: '" + location + "' for locale: '"
                + locale + "'");
    }

    private InputStream openStream(String location) {
        try {
            return resourceLoader.openStream(location);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open location: '" + location + "'", e);
        }
    }

    private PropertyResolver loadLocalization(String location, InputStream inputStream) {
        try {
            return localizationLoader.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load localization resource: '" + location + "'", e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static CacheBuilder<Object, Object> createCacheBuilder(Builder builder) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        if (builder.maxCacheTimeInSec >= 0) {
            cacheBuilder.expireAfterWrite(builder.maxCacheTimeInSec, TimeUnit.SECONDS);
        }
        return cacheBuilder;
    }

    private static List<Locale> createCandidateLocales(Locale locale) {
        List<Locale> locales = Lists.newArrayListWithCapacity(2);

        if (!locale.getCountry().isEmpty()) {
            locales.add(locale);
        }
        if (!locale.getLanguage().isEmpty()) {
            locales.add(locales.isEmpty() ? locale : new Locale(locale.getLanguage(), "", ""));
        }
        locales.add(Locale.ROOT);

        return locales;
    }

    private static String createLocalizedLocation(String locationPrefix, String locationSuffix, Locale locale) {
        StringBuilder builder = new StringBuilder(locationPrefix.length() + locationSuffix.length() + 6);

        builder.append(locationPrefix);
        if (!locale.getLanguage().isEmpty()) {
            builder.append(LOCALE_SEPARATOR);
            builder.append(locale.getLanguage());
        }
        if (!locale.getCountry().isEmpty()) {
            builder.append(LOCALE_SEPARATOR);
            builder.append(locale.getCountry());
        }
        builder.append(locationSuffix);

        return builder.toString();
    }

    private static PropertyResolver createMultiPropertyResolver(List<PropertyResolver> propertyResolvers) {
        if (propertyResolvers.size() == 1) {
            return propertyResolvers.get(0);
        }
        return new ChainedPropertyResolver(propertyResolvers);
    }

    public static class Builder {
        private LocaleResolver localeResolver;
        private ResourceLoader resourceLoader;
        private PropertiesLocalizationLoader localizationLoader;
        private List<String> locations;
        private int maxCacheTimeInSec = -1;

        public Builder withLocaleResolver(LocaleResolver localeResolver) {
            this.localeResolver = localeResolver;
            return this;
        }

        public Builder withResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
            return this;
        }

        public Builder withLocalizationLoader(PropertiesLocalizationLoader localizationLoader) {
            this.localizationLoader = localizationLoader;
            return this;
        }

        public Builder withLocations(List<String> locations) {
            this.locations = locations;
            return this;
        }

        public Builder withMaxCacheTimeInSec(int maxCacheTimeInSec) {
            this.maxCacheTimeInSec = maxCacheTimeInSec;
            return this;
        }

        public DefaultLocalizationProvider build() {
            return new DefaultLocalizationProvider(this);
        }
    }

    private static class ChainedPropertyResolver implements PropertyResolver {
        private final Collection<PropertyResolver> propertyResolvers;

        public ChainedPropertyResolver(Collection<PropertyResolver> propertyResolvers) {
            this.propertyResolvers = propertyResolvers;
        }

        @Override
        public String getString(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                String value = propertyResolver.getString(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }


        @Override
        public Boolean getBoolean(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                Boolean value = propertyResolver.getBoolean(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }


        @Override
        public Integer getInteger(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                Integer value = propertyResolver.getInteger(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }


        @Override
        public Long getLong(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                Long value = propertyResolver.getLong(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }


        @Override
        public Float getFloat(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                Float value = propertyResolver.getFloat(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }


        @Override
        public Double getDouble(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                Double value = propertyResolver.getDouble(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        @Override
        public List<String> getList(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                List<String> value = propertyResolver.getList(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        @Override
        public Map<String, String> getMap(String key) {
            for (PropertyResolver propertyResolver : propertyResolvers) {
                Map<String, String> value = propertyResolver.getMap(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }
}