-- Création de la table User
CREATE TABLE IF NOT EXISTS User (
    idUser INTEGER PRIMARY KEY AUTOINCREMENT,
    nomUser TEXT NOT NULL,
    prenomUser TEXT NOT NULL,
    usernameUser TEXT UNIQUE NOT NULL,
    passwordUser TEXT NOT NULL,
    role TEXT NOT NULL,
    state BOOLEAN DEFAULT true
);

-- Création de la table roles
CREATE TABLE IF NOT EXISTS roles (
    idRole INTEGER PRIMARY KEY AUTOINCREMENT,
    nameRole TEXT UNIQUE NOT NULL
);

-- Création de la table user_role pour la relation many-to-many
CREATE TABLE IF NOT EXISTS user_role (
    userId INTEGER,
    roleId INTEGER,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) REFERENCES User(idUser),
    FOREIGN KEY (roleId) REFERENCES roles(idRole)
);

-- Insertion des rôles par défaut
INSERT OR IGNORE INTO roles (nameRole) VALUES 
    ('SUPER_ADMIN'),
    ('ADMIN'),
    ('USER');
