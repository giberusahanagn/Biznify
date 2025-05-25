package com.biznify.warehouse.service;

import java.util.List;
import com.biznify.warehouse.entity.Partner;

public interface PartnerService {

    Partner createPartner(Partner partner);

    Partner updatePartner(Long id, Partner partner);

    void deletePartner(Long id);

    Partner getPartnerById(Long id);

    List<Partner> getAllPartners();
}
