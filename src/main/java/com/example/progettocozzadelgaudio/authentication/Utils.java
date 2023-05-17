package com.example.progettocozzadelgaudio.authentication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.writer.JsonReader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;


@UtilityClass
@Log4j2
public class Utils {

    /* CODICE PROFESSORE
    public Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getAuthServerId() {
        return getTokenNode().get("subject").asText();
    }

    public String getName() {
        return getTokenNode().get("claims").get("name").asText();
    }

    public String getEmail() {
        return getTokenNode().get("claims").get("preferred_username").asText();
    }

    private JsonNode getTokenNode() {
        Jwt jwt = getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtAsString;
        JsonNode jsonNode;
        try {
            jwtAsString = objectMapper.writeValueAsString(jwt);
            jsonNode = objectMapper.readTree(jwtAsString);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Unable to retrieve user's info!");
        }
        return jsonNode;
    }

    public static boolean isAdmin() {
       return getTokenNode().get("claims").get("resource_access").get("biglietteria-client").findValue("roles").get(0).asText().equals("admin");

    }

    */

     public Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getAuthServerId(){
        return getPrincipal().getClaims().get("sid").toString();
    }

    public String getName() {
        return getPrincipal().getClaims().get("preferred_username").toString();
    }

    public String getCognome(){return getTokenNode1().get("family_name").toString();}

    public String getEmail() {
        return getTokenNode().get("email").asText();
    }

    private JsonNode getTokenNode()  {
        Jwt jwt = getPrincipal();
        Map<String, Object> result = new HashMap<>();
        for( Map.Entry<String, Object> riga : jwt.getClaims().entrySet()) {
            if(! (riga.getKey().equals("exp") || riga.getKey().equals("iat")))
                result.put(riga.getKey(),riga.getValue());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        //System.out.println("TOKEN VALUE= "+result.toString());
        return  objectMapper.valueToTree(result);
    }


    private Map<String,Object> getTokenNode1()  {
        Jwt jwt = getPrincipal();
        return  jwt.getClaims();
    }
}
