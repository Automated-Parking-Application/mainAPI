package com.capstone.parking.service;

import com.capstone.parking.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;


public class TokenAuthenticationService {
    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "QRPARKINGAPPLICATIONSECRETKEYFORMYCAPSTONEPROJECTQRPARKINGAPPLICATIONSECRETKEYFORMYCAPSTONEPROJECTQRPARKINGAPPLICATIONSECRETKEYFORMYCAPSTONEPROJECTQRPARKINGAPPLICATIONSECRETKEYFORMYCAPSTONEPROJECT";
    static final String TOKEN_PREFIX = "Bearer";
    static public final String HEADER_STRING = "Authorization";

    public static String createToken(UserEntity userEntity) {
        ObjectMapper mapper = new ObjectMapper();
        UserEntity userAuthenInfo = new UserEntity();
        userAuthenInfo.setId(userEntity.getId());
        userAuthenInfo.setPhoneNumber(userEntity.getPhoneNumber());
        userAuthenInfo.setRoleByRoleId(userEntity.getRoleByRoleId());
        String json;
        try {
            json = mapper.writeValueAsString(userAuthenInfo);
        } catch (JsonProcessingException e) {
            json = userAuthenInfo.getId() + " " + userAuthenInfo.getPhoneNumber();
        }
        String token = Jwts.builder()
                .setSubject(json)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + token;
    }

    public static boolean checkToken(String username, String token) {
        boolean res = false;
        try {
            String user = getInfoFromToken(token);
            res = user.equals(username);
        } catch (Exception e) {
        }
        return res;
    }

    public static String getInfoFromToken(String token) {
        String res = null;
        try {
            res = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
        }
        return res;
    }

    public static UserEntity getUserFromToken(String token) {
        UserEntity userEntity = null;

        String res = getInfoFromToken(token);
        if (res == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            userEntity = mapper.readValue(res, UserEntity.class);
        } catch (IOException e) {
            StringTokenizer stringTokenizer = new StringTokenizer(res, " ");
            userEntity = new UserEntity();
            try {
                String id = stringTokenizer.nextToken();
                userEntity.setId(Integer.parseInt(id));
                userEntity.setPhoneNumber(stringTokenizer.nextToken());
            } catch (Exception ex) {
                userEntity = null;
            }
        }
        return userEntity;
    }

}
