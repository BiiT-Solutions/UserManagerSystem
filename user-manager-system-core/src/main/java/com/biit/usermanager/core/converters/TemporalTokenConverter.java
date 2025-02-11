package com.biit.usermanager.core.converters;

import com.biit.server.controller.converters.ElementConverter;
import com.biit.server.converters.ConverterUtils;
import com.biit.usermanager.core.converters.models.TemporalTokenConverterRequest;
import com.biit.usermanager.dto.TemporalTokenDTO;
import com.biit.usermanager.persistence.entities.TemporalToken;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TemporalTokenConverter extends ElementConverter<TemporalToken, TemporalTokenDTO, TemporalTokenConverterRequest> {


    @Override
    protected TemporalTokenDTO convertElement(TemporalTokenConverterRequest from) {
        final TemporalTokenDTO temporalTokenDTO = new TemporalTokenDTO();
        BeanUtils.copyProperties(from.getEntity(), temporalTokenDTO, ConverterUtils.getNullPropertyNames(from.getEntity()));
        temporalTokenDTO.setUserId(from.getEntity().getUser().getId());
        return temporalTokenDTO;
    }


    @Override
    public TemporalToken reverse(TemporalTokenDTO to) {
        throw new UnsupportedOperationException();
    }
}
