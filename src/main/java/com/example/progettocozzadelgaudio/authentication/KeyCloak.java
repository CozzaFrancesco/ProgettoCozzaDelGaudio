package com.example.progettocozzadelgaudio.authentication;

import com.example.progettocozzadelgaudio.entities.Farmacia;
import com.example.progettocozzadelgaudio.repositories.FarmaciaRepository;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeyCloak {
    Keycloak keycloak;
    String username_admin = "admin";
    String password_admin = "admin";
    String nome_client = "admin-client";
    String ruoloFarmacia = "farmacia";
    String ruoloGestore="gestore";
    String ruoloCliente="cliente";
    String serverUrl = "http://localhost:8080";
    String realm = "sistemaFarmaceutico-realm";
    String clientId = "admin-client";

    //String clientSecret = "rdOc9u6hf5eGYzfxa0tgqaUaP0m9IQdr";

    public KeyCloak() {

        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                /*.clientSecret(clientSecret)*/
                .username(username_admin)
                .password(password_admin)
                .build();
    }

    public boolean registraFarmacia( String nome, String partitaIva, String password ) {
            try {
                // Define user
                UserRepresentation user = new UserRepresentation();
                user.setEnabled(true);
                user.setUsername(partitaIva);
                user.setFirstName(nome);
                user.setEmail(partitaIva+"@sistemaFarmaceutico.com");

                user.setAttributes(Collections.singletonMap("origin" , Arrays.asList("demo")));

                // Get realm
                RealmResource realmResource = keycloak.realm(realm);
                UsersResource usersResource = realmResource.users();

                // Create user (requires manage-users role)
                Response response = usersResource.create(user);
                System.out.printf("Response: %s %s%n" , response.getStatus() , response.getStatusInfo());
                System.out.println(response.getLocation());
                String userId = CreatedResponseUtil.getCreatedId(response);

                System.out.printf("User created with userId: %s%n" , userId);

                // Define password credential
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(password);

                UserResource userResource = usersResource.get(userId);

                // Set password credential
                userResource.resetPassword(passwordCred);


//        // Get client
                ClientRepresentation app1Client = realmResource.clients().findByClientId(nome_client).get(0);
//
//        // Get client level role (requires view-clients role)
                RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()).roles().get(ruoloFarmacia).toRepresentation();
//
//        // Assign client level role to user
                userResource.roles().clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
                return true;
            }
            catch ( WebApplicationException e ){
                e.printStackTrace();
                return false;
            }

        }


    public boolean registraGestore( String nome, String email, String password ) {
        if(esisteGestore())
            return false;
        try {
            // Define user
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(nome);
            user.setEmail(email);

            user.setAttributes(Collections.singletonMap("origin" , Arrays.asList("demo")));

            // Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user (requires manage-users role)
            Response response = usersResource.create(user);
            System.out.printf("Response: %s %s%n" , response.getStatus() , response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);

            System.out.printf("User created with userId: %s%n" , userId);

            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);

            UserResource userResource = usersResource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);


//        // Get client
            ClientRepresentation app1Client = realmResource.clients().findByClientId(nome_client).get(0);
//
//        // Get client level role (requires view-clients role)
            RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()).roles().get(ruoloGestore).toRepresentation();
//
//        // Assign client level role to user
            userResource.roles().clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
            return true;
        }
        catch ( WebApplicationException e ){
            e.printStackTrace();
            return false;
        }
    }

    public boolean registraCliente( String nome, String cognome, String codiceFiscale, String password ) {
        try {
            // Define user
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(codiceFiscale);
            user.setFirstName(nome);
            user.setLastName(cognome);
            user.setEmail(codiceFiscale+"@sistemaFarmaceutico.com");

            user.setAttributes(Collections.singletonMap("origin" , Arrays.asList("demo")));

            // Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Create user (requires manage-users role)
            Response response = usersResource.create(user);
            System.out.printf("Response: %s %s%n" , response.getStatus() , response.getStatusInfo());
            System.out.println(response.getLocation());
            String userId = CreatedResponseUtil.getCreatedId(response);

            System.out.printf("User created with userId: %s%n" , userId);

            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);

            UserResource userResource = usersResource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);


//        // Get client
            ClientRepresentation app1Client = realmResource.clients().findByClientId(nome_client).get(0);
//
//        // Get client level role (requires view-clients role)
            RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()).roles().get(ruoloCliente).toRepresentation();
//
//        // Assign client level role to user
            userResource.roles().clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
            return true;
        }
        catch ( WebApplicationException e ){
            e.printStackTrace();
            return false;
        }

    }

    public boolean eliminaFarmacia(String partitaIva) {
        try{
            if(keycloak.realm(realm).users().delete(keycloak.realm(realm).users().search(partitaIva).get(0).getId()).getStatus()==204)
                return true;
            return false;
        } catch ( IndexOutOfBoundsException e ) {
            return false;
        }
    }

    public boolean eliminaGestore(String username) {
        if(!esisteGestorePerUsername(username))
            return false;
        try{

            if(keycloak.realm(realm).users().delete(keycloak.realm(realm).users().search(username).get(0).getId()).getStatus()==204) {
                return true;
            }
            return false;
        } catch ( IndexOutOfBoundsException e ) {
            return false;
        }
    }

    public boolean esisteGestore() {
        List<UserRepresentation> users = keycloak.realm(realm).users().list();

        for(UserRepresentation user: users) {
            UserResource userResource = keycloak.realm(realm).users().get(user.getId());
            ClientRepresentation clientRepresentation = keycloak.realm(realm).clients().findByClientId(clientId).get(0);
            List<RoleRepresentation> roles = userResource.roles().clientLevel(clientRepresentation.getId()).listAll();
            for(RoleRepresentation rr:roles) {
                if (rr.getName().equals("gestore"))
                    return true;
            }
        }
        return false;
    }

    public boolean esisteGestorePerUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(realm).users().list();

        for(UserRepresentation user: users) {
            UserResource userResource = keycloak.realm(realm).users().get(user.getId());
            ClientRepresentation clientRepresentation = keycloak.realm(realm).clients().findByClientId(clientId).get(0);
            List<RoleRepresentation> roles = userResource.roles().clientLevel(clientRepresentation.getId()).listAll();
            for(RoleRepresentation rr:roles) {
                if (rr.getName().equals("gestore") && user.getUsername().equals(username))
                    return true;
            }
        }
        return false;
    }

}
