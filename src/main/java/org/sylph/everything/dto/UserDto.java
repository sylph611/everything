package org.sylph.everything.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String picture;
    private String provider;
    private String providerId;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 