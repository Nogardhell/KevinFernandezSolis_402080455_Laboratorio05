package org.example.Server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Thread-safe map: token -> username
    private static final Map<String, String> sessions = new ConcurrentHashMap<>();

    /**
     * Create a session for the given username.
     * Returns the generated token.
     */
    public static String createSession(String username) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, username);
        return token;
    }

    /**
     * Check if a session token is valid.
     */
    public static boolean isValid(String token) {
        return token != null && sessions.containsKey(token);
    }

    /**
     * Get the username associated with a token.
     */
    public static String getUsername(String token) {
        return sessions.get(token);
    }

    /**
     * Remove a session (logout).
     */
    public static void removeSession(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
