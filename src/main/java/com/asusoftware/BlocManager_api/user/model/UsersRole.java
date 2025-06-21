package com.asusoftware.BlocManager_api.user.model;

public enum UsersRole {
    ADMIN_ASSOCIATION,  // Poate crea și gestiona asociația + blocuri
    BLOCK_ADMIN,        // Admin pe un singur bloc
    APARTMENT_OWNER,            // Locatar normal (proprietar)
    TENANT,            // Locatar (chiriaș)
    CO_OWNER,          // Co-proprietar al apartamentului
}
