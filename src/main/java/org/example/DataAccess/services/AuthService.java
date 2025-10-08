package org.example.DataAccess.services;

import org.example.Domain.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


public class AuthService {
    private final SessionFactory sessionFactory;

    // Configurable constants for password hashing
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public AuthService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------
    // User Registration
    // -------------------------
    public User register(String username, String email, String password, String role) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            // Check if username or email already exists
            if (getUserByUsername(username) != null || getUserByEmail(email) != null) {
                throw new IllegalArgumentException("Username or email already in use");
            }

            String salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setSalt(salt);
            user.setPasswordHash(hashedPassword);
            user.setRole(role);

            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();

            return user;
        }
    }

    // -------------------------
    // User Login
    // -------------------------
    public boolean login(String usernameOrEmail, String password) {
        User user = getUserByUsername(usernameOrEmail);

        if (user == null) {
            user = getUserByEmail(usernameOrEmail);
        }
        if (user == null) {
            return false;
        }

        String hashedInput = hashPassword(password, user.getSalt());
        return hashedInput.equals(user.getPasswordHash());
    }

    // -------------------------
    // Helper Queries
    // -------------------------
    public User getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    // -------------------------
    // Password Hashing Utilities
    // -------------------------
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }
}
