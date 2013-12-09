package common;

/**
 * Represents possible states for the client/server system.
 *
 * Intended for assertions (ensuring that messages get sent
 * and received from the proper states).
 */
public enum SocketState {
    NOT_LOGGED_IN,
    LOGIN_PENDING,
    NOT_CONNECTED,
    CONNECT_PENDING,
    CONNECTED,
    DISCONNECT_PENDING;
}
