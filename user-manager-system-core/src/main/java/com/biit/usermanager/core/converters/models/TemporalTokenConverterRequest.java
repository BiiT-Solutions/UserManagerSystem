package com.biit.usermanager.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.usermanager.persistence.entities.TemporalToken;

public class TemporalTokenConverterRequest extends ConverterRequest<TemporalToken> {

    public TemporalTokenConverterRequest(TemporalToken entity) {
        super(entity);
    }
}
