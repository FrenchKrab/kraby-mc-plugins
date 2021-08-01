package com.kraby.mcarcinizer.utils.config;

/**
 * A configuration that can be enabled or not.
 */
public interface EnablableConfig extends Config {

    /**
     * Wether or not the module is enabled.
     * @return
     */
    boolean isEnabled();
}
