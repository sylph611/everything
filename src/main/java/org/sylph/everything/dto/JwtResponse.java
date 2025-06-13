package org.sylph.everything.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.sylph.everything.entity.User;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private User user;

    public JwtResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}