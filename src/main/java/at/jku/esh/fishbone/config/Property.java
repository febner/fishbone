package at.jku.esh.fishbone.config;

/**
 * The Enum Property.
 */
public enum Property {

	// TODO enter further properties here
	PROPERTY("propertyname");

	/** The prop. */
	private String prop;

	/**
	 * Instantiates a new name.
	 * 
	 * @param prop
	 *            the prop
	 */
	Property(final String prop) {
		this.prop = prop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return prop;
	}
}
