package com.dataiku.millenium.exceptions;

/**
 * DataSourceFormatNotSupported is a custom exception that is thrown when the code encounters a URL protocol for
 * the data source that is not supported.
 * The supported protocols are "file" and "jar".
 */
public class DataSourceFormatNotSupported extends RuntimeException {

    public DataSourceFormatNotSupported(String message) {
        super(message);
    }
}

