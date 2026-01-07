package com.finflow.portfolio.domain;

/**
 * Enumeration representing the connection status of an external account.
 */
public enum ConnectionStatus {
    /**
     * Account is successfully connected
     */
    CONNECTED,

    /**
     * Account is disconnected
     */
    DISCONNECTED,

    /**
     * Account connection has an error
     */
    ERROR,

    /**
     * Account is currently syncing
     */
    SYNCING
}
