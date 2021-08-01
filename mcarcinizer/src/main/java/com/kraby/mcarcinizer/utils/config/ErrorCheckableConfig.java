package com.kraby.mcarcinizer.utils.config;

import java.util.List;

/**
 * A configuration that handles error checking.
 */
public interface ErrorCheckableConfig extends Config {
    /**
     * Get errors detected in this config file.
     * @return Textual descriptions of the errors.
     */
    List<String> getErrors();
}
