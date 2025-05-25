package com.biznify.warehouse.dto;

import lombok.Data;
import java.util.List;

@Data
public class AisleDTO {
    private Long id;
    private String aisleCode;
    private String name;

    private List<RackDTO> racks;
}
