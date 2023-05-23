package com.example.progettocozzadelgaudio.authentication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;


@UtilityClass
@Log4j2
public class Utils {

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
        return  objectMapper.valueToTree(result);
    }


    private Map<String,Object> getTokenNode1()  {
        Jwt jwt = getPrincipal();
        return  jwt.getClaims();
    }
}
