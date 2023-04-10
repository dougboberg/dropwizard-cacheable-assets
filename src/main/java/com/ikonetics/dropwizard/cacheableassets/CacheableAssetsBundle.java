package com.ikonetics.dropwizard.cacheableassets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.slf4j.LoggerFactory;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.servlets.assets.AssetServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

// this is a bare minimum extension of the standard Dropwizard AssetsBundle
// this bundle uses the CacheableAssetsServlet which adds HTTP Header cache fields to the HTTP Response


public class CacheableAssetsBundle extends AssetsBundle {

	final int cacheDays; // Google recommends 180 days for long-term cacheable items

	// quick setup with default 180 day cache, name, and media type.
	// Example: URL /static_123slug/favicon.ico maps to File: /static/favicon.ico
	public CacheableAssetsBundle(String filepath, String urlpath) {
		this(180, filepath, urlpath, null, filepath + "(" + urlpath + ")", MediaType.TEXT_PLAIN);
	}


	// full constructor: cacheDays this class needs, plus everything super needs
	public CacheableAssetsBundle(int cacheDays, String resourceFilePath, String uriPath, String indexFileName, String bundleMappingName,
			String defaultMediaType) {
		super(resourceFilePath, uriPath, indexFileName, bundleMappingName, defaultMediaType);
		this.cacheDays = cacheDays;
	}


	@Override
	protected CacheableAssetsServlet createServlet() {
		return new CacheableAssetsServlet(this.cacheDays, getResourcePath(), getUriPath(), getIndexFile());
	}

	class CacheableAssetsServlet extends AssetServlet {
		final String headervalue;

		public CacheableAssetsServlet(int cacheDays, String resourcePath, String uriPath, String indexFile) {
			super(resourcePath, uriPath, indexFile, StandardCharsets.UTF_8);

			final long seconds = Duration.ofDays(cacheDays).toSeconds();

			if (seconds > 1) {
				headervalue = "public, max-age=" + seconds;
			} else {
				headervalue = "no-store";
			}

			LoggerFactory.getLogger(CacheableAssetsServlet.class).info(
					"HTTP cache duration set to {} seconds ({} days) for {}. Related GET requests will include '{}' header with a value of '{}'", seconds,
					cacheDays, uriPath, HttpHeaders.CACHE_CONTROL, headervalue);
		}


		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			resp.setHeader(HttpHeaders.CACHE_CONTROL, headervalue);
			super.doGet(req, resp);
		}
	}
}
