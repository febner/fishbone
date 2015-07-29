package at.jku.esh.fishbone.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * The Class AbstractProperties.
 */
public abstract class AbstractProperties {
	/** The properties. */
	protected final Properties properties = new Properties();

	/**
	 * Gets the boolean.
	 * 
	 * @param d
	 *            the d
	 * @return the boolean
	 */
	public Boolean getBoolean(final Property d) {
		return Boolean.parseBoolean(this.getString(d));
	}

	/**
	 * Gets the path.
	 * 
	 * @param d
	 *            the d
	 * @param more
	 *            the more
	 * @return the path
	 */
	public Path getPath(final Property d, final String... more) {
		return Paths.get(this.getString(d), more);
	}

	/**
	 * Gets the int.
	 * 
	 * @param d
	 *            the d
	 * @return the int
	 */
	public Integer getInt(final Property d) {
		return Integer.parseInt(this.getString(d));
	}

	/**
	 * Gets the string.
	 * 
	 * @param d
	 *            the d
	 * @return the string
	 */
	public String getString(final Property d) {
		return Objects.requireNonNull(properties.getProperty(d.toString()), d.name());
	}
}
