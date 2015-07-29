package at.jku.esh.fishbone.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ReporterProperties.
 */
public class FishboneProperties extends AbstractProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(FishboneProperties.class);

	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private final String propertiesPath;

	/**
	 * Instantiates a new builds the watcher properties.
	 * 
	 * @param propertiesPath
	 *            the properties path
	 */
	public FishboneProperties(final String propertiesPath) {
		this.propertiesPath = propertiesPath;
		this.load();
	}

	/**
	 * Load0.
	 */
	private void load() {

		LOGGER.info("Loading Properties.");
		try {
			super.properties.load(FishboneProperties.class.getClassLoader().getResourceAsStream(propertiesPath));
		} catch (IOException e) {
			LOGGER.error("Loading reporter properties (" + propertiesPath + ") failed", e);
		}

	}

	/**
	 * Log content.
	 */
	public void logContent() {
		for (Property p : Property.values()) {
			LOGGER.debug(p + " : " + super.getString(p));
		}
	}
}
