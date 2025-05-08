package com.example.church_project.services;

import com.example.church_project.dao.UserDAO;
import com.example.church_project.models.User;
import com.example.church_project.models.Role;
import com.example.church_project.config.DBConfig;
import com.example.church_project.utils.AlertUtils;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthService {
    private static AuthService instance;
    private final UserDAO userDAO;
    private User currentUser;

    private AuthService() {
        this.userDAO = new UserDAO();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean userExists(String username) throws SQLException {
        return !userDAO.isUsernameAvailable(username);
    }

    public boolean register(User user) {
        try {
            if (!userDAO.isUsernameAvailable(user.getUsernameUser())) {
                return false;
            }

            // Toujours hasher, pas besoin de vérifier
            String hashedPassword = BCrypt.hashpw(user.getPasswordUser(), BCrypt.gensalt());
            user.setPasswordUser(hashedPassword);

            userDAO.register(user);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur d'inscription : " + e.getMessage());
            return false;
        }
    }

    private boolean isPasswordHashed(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }

    public boolean login(String username, String password) {
        try {
            System.out.println("Tentative de login pour : " + username);
            User user = userDAO.findByUsername(username);

            if (user == null) {
                System.out.println("Utilisateur non trouvé");
                return false;
            }

            // Vérifier si l'utilisateur est actif
            if (!user.isState()) {
                System.out.println("L'utilisateur est inactif");
                AlertUtils.showError("Ce compte est désactivé. Veuillez contacter l'administrateur.");
                return false;
            }

            System.out.println("Hash stocké : " + user.getPasswordUser());
            System.out.println("Mot de passe fourni : " + password);
            System.out.println("Rôle de l'utilisateur : " + user.getRole());
            
            // Vérifier si le mot de passe est déjà hashé
            if (isPasswordHashed(password)) {
                System.out.println("Le mot de passe fourni est déjà hashé");
                return false;
            }
            
            // Comparer le mot de passe en clair avec le hash stocké
            boolean passwordMatch = BCrypt.checkpw(password, user.getPasswordUser());
            System.out.println("Résultat vérification mot de passe : " + passwordMatch);

            if (passwordMatch) {
                currentUser = user;
                System.out.println("Connexion réussie - Rôle : " + currentUser.getRole());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers(false); // false = ne pas inclure les utilisateurs inactifs
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<User> getVisibleUsers(User currentUser) {
        try {
            // Si l'utilisateur est un superAdmin, il voit tous les utilisateurs (actifs et inactifs)
            if (isSuperAdmin(currentUser)) {
                return userDAO.getAllUsers(true); // true = inclure les utilisateurs inactifs
            }
            
            // Pour les autres rôles, on ne montre que les utilisateurs actifs
            List<User> allUsers = userDAO.getAllUsers(false); // false = ne pas inclure les utilisateurs inactifs
            
            // Si l'utilisateur est un admin, il ne voit pas les superAdmin
            if (currentUser.getRole() == Role.ADMIN) {
                return allUsers.stream()
                        .filter(user -> user.getRole() != Role.SUPER_ADMIN)
                        .collect(Collectors.toList());
            }
            
            // Pour les autres rôles, retourner une liste vide
            return new ArrayList<>();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs visibles : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean canAccessUserManagement(User user) {
        System.out.println("Vérification accès gestion utilisateurs - Rôle : " + (user != null ? user.getRole() : "null"));
        boolean canAccess = user != null && (user.getRole() == Role.SUPER_ADMIN || user.getRole() == Role.ADMIN);
        System.out.println("Accès autorisé : " + canAccess);
        return canAccess;
    }

    public boolean canDeleteUser(User currentUser, User userToDelete) {
        // Un superAdmin peut supprimer n'importe quel utilisateur sauf un autre superAdmin
        if (isSuperAdmin(currentUser)) {
            return userToDelete.getRole() != Role.SUPER_ADMIN;
        }
        
        // Un admin ne peut supprimer que les utilisateurs normaux
        if (currentUser.getRole() == Role.ADMIN) {
            return userToDelete.getRole() == Role.USER;
        }
        
        return false;
    }

    public boolean deleteUser(int userId) {
        // Vérifier si l'utilisateur à supprimer est un superAdmin
        String checkQuery = "SELECT role FROM User WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getString("role").equals(Role.SUPER_ADMIN.name())) {
                System.err.println("Impossible de supprimer un compte Super Admin");
                return false;
            }

            String query = "DELETE FROM User WHERE IdUser = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSuperAdmin(User user) {
        System.out.println("Vérification rôle superAdmin - Rôle : " + (user != null ? user.getRole() : "null"));
        boolean isSuper = user != null && user.getRole() == Role.SUPER_ADMIN;
        System.out.println("Est superAdmin : " + isSuper);
        return isSuper;
    }

    public boolean canManageSuperAdmin(User user) {
        return isSuperAdmin(user);
    }

    public boolean logicalDeleteUser(int userId) {
        try {
            return userDAO.logicalDelete(userId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User updatedUser) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        try {
            // Vérifier les permissions
            if (isSuperAdmin(currentUser)) {
                // Le superadmin peut modifier n'importe quel utilisateur
                return userDAO.updateUser(updatedUser);
            } else if (currentUser.getRole() == Role.ADMIN) {
                // L'admin ne peut modifier que les utilisateurs normaux
                User userToEdit = userDAO.getUserById(updatedUser.getIdUser());
                if (userToEdit != null && userToEdit.getRole() == Role.USER) {
                    // Forcer le rôle à USER pour l'admin
                    updatedUser.setRole(Role.USER);
                    return userDAO.updateUser(updatedUser);
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserState(int userId, boolean newState) {
        User currentUser = getCurrentUser();
        if (currentUser == null || !isSuperAdmin(currentUser)) {
            return false;
        }

        try {
            User userToUpdate = userDAO.getUserById(userId);
            if (userToUpdate == null) {
                return false;
            }

            // Empêcher la désactivation du dernier SuperAdmin
            if (!newState && userToUpdate.getRole() == Role.SUPER_ADMIN) {
                // Vérifier s'il reste d'autres SuperAdmin actifs
                List<User> allUsers = userDAO.getAllUsers(true);
                long activeSuperAdmins = allUsers.stream()
                    .filter(u -> u.getRole() == Role.SUPER_ADMIN && u.isState())
                    .count();
                
                if (activeSuperAdmins <= 1) {
                    AlertUtils.showError("Impossible de désactiver le dernier SuperAdmin actif");
                    return false;
                }
            }

            // Si on désactive l'utilisateur actuellement connecté
            if (!newState && currentUser.getIdUser() == userId) {
                AlertUtils.showError("Vous ne pouvez pas désactiver votre propre compte");
                return false;
            }

            boolean success = userDAO.updateUserState(userId, newState);
            if (success) {
                // Si l'utilisateur est désactivé et qu'il est connecté, le déconnecter
                if (!newState && currentUser.getIdUser() == userId) {
                    logout();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}