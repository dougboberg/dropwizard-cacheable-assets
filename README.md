# dropwizard-cacheable-assets
Tiny extension of the standard Dropwizard AssetsBundle which adds HTTP Header cache fields to
the HTTP Response of static asset resource files.

## Overview of available configuration options
* cacheDays
* resourcePath
* uriPath


# Setup

## Add the Dependency
The compiled binary for this project is hosted with JitPack.
https://jitpack.io/#com.ikonetics/dropwizard-cacheable-assets

Add the following to your project's Maven `pom.xml`
```xml
<!-- Add the JitPack repository -->
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<!-- Add the dependency -->
<dependency>
    <groupId>com.ikonetics</groupId>
    <artifactId>dropwizard-cacheable-assets</artifactId>
    <version>1.0</version>
</dependency>
```

# Usage
Modify the `initialize()` method of your Dropwizard Application as shown below.
You can add the `CacheableAssetsBundle` multiple times to serve resources with different uri, path, and caching properties.

```java
import com.ikonetics.dropwizard.cacheableassets.*;

@Override
public void initialize(Bootstrap<Configuration> bootstrap) {

    // The basic constructor takes 2 strings, a local file path and a web URI path, and defaults to a 180 day cache
    // Example: serve the file /app_assets/image.jpg from the web URI location /static/image.jpg
    bootstrap.addBundle(new CacheableAssetsBundle("/app_assets", "/static"));

    // The full constructor requires: cache days, file path, uripath, index file, bundle name, default media type
    // Example: 30 day cache, serving 'docs' at 'webpages', no index file, 'AUniqueName' to prevent conflict, and default to HTML media
    bootstrap.addBundle(new CacheableAssetsBundle(30, "/docs", "/webpages", null, "AUniqueName", "text/html"));

    // Another example: standard 'well-known' files, saved in a standard directory and served from a 'dot' path with default 180 day cache
    bootstrap.addBundle(new CacheableAssetsBundle("/well-known", "/.well-known"));
}
```

# References
* Dropwizard Assets Bundle https://www.dropwizard.io/en/latest/manual/core.html#serving-assets
