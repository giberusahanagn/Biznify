package com.biznify.partner.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String contactNumber;
    private String status;
    private String websiteUrl;

    // 🔥 No relation to Product entity — decoupled microservice
}
