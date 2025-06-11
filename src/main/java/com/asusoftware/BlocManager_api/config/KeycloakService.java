package com.asusoftware.BlocManager_api.config;

import com.asusoftware.BlocManager_api.user.model.dto.LoginDto;
import com.asusoftware.BlocManager_api.user.model.dto.LoginResponseDto;
import com.asusoftware.BlocManager_api.user.model.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    /**
     * Creează un user în Keycloak.
     */
    public String createKeycloakUser(UserRegisterDto userDTO) {
        Keycloak keycloak = getKeycloakAdminInstance();

        // verifică dacă userul există deja
        List<UserRepresentation> existing = keycloak.realm(realm)
                .users().search(userDTO.getEmail(), true);
        if (!existing.isEmpty()) {
            throw new RuntimeException("User deja existent în Keycloak.");
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRealmRoles(Collections.singletonList(userDTO.getRole().name()));

        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setTemporary(false);
        password.setValue(userDTO.getPassword());
        user.setCredentials(Collections.singletonList(password));

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() == 201) {
            String location = response.getHeaderString("Location");
            return location.substring(location.lastIndexOf('/') + 1); // Keycloak ID
        } else if (response.getStatus() == 409) {
            throw new RuntimeException("Un cont cu acest email există deja.");
        } else {
            throw new RuntimeException("Eroare la crearea userului în Keycloak: " + response.getStatus());
        }
    }

    /**
     * Login standard username + password (returnează token + refresh).
     */
    public LoginResponseDto loginUser(LoginDto loginDto) {
        try {
            AccessTokenResponse token = obtainToken(loginDto.getEmail(), loginDto.getPassword());
            return LoginResponseDto.builder()
                    .accessToken(token.getToken())
                    .refreshToken(token.getRefreshToken())
                    .expiresIn(token.getExpiresIn())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Autentificare eșuată: " + e.getMessage(), e);
        }
    }

    /**
     * Reîmprospătare token (fără re-login).
     */
    public LoginResponseDto refreshToken(String refreshToken) {
        try {
            var client = javax.ws.rs.client.ClientBuilder.newClient();
            var target = client.target(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token");

            var form = new javax.ws.rs.core.Form();
            form.param("grant_type", "refresh_token");
            form.param("client_id", clientId);
            form.param("client_secret", clientSecret);
            form.param("refresh_token", refreshToken);

            AccessTokenResponse token = target.request()
                    .post(javax.ws.rs.client.Entity.form(form), AccessTokenResponse.class);

            return LoginResponseDto.builder()
                    .accessToken(token.getToken())
                    .refreshToken(token.getRefreshToken())
                    .expiresIn(token.getExpiresIn())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Reîmprospătarea tokenului a eșuat: " + e.getMessage(), e);
        }
    }

    /**
     * Ștergere utilizator din Keycloak.
     */
    public void deleteKeycloakUser(UUID keycloakId) {
        Keycloak keycloak = getKeycloakAdminInstance();
        keycloak.realm(realm).users().delete(keycloakId.toString());
    }

    /**
     * Obține token prin username + password (grant type: password).
     */
    private AccessTokenResponse obtainToken(String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build()
                .tokenManager()
                .getAccessToken();
    }

    /**
     * Instanță Keycloak pentru acces admin (realm: master).
     */
    private Keycloak getKeycloakAdminInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master")
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
