package com.asusoftware.BlocManager_api.user.model;

public enum UsersRole {
    ADMIN_ASSOCIATION,  // Poate crea și gestiona asociația + blocuri
    BLOCK_ADMIN,        // Admin pe un singur bloc
    LOCATAR             // Locatar normal (proprietar sau chiriaș)
}
