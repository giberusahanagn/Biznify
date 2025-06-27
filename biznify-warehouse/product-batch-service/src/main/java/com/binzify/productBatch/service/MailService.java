package com.binzify.productBatch.service;

import com.binzify.productBatch.dto.PartnerDTO;
import com.binzify.productBatch.entity.ProductBatch;

public interface MailService {

    void sendPartnerBatchStoredEmail(ProductBatch batch, PartnerDTO partner);

}
