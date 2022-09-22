package com.biit.usermanager.core.converters;


import com.biit.usermanager.core.controller.models.OrganizationDTO;
import com.biit.usermanager.core.converters.models.ConverterRequest;
import com.biit.usermanager.core.converters.models.OrganizationConverterRequest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ElementConverter<F, T, R extends ConverterRequest<F>> implements IElementConverter<F, T, R> {

    protected abstract T convertElement(R from);

    public T convert(R from) {
        if (from == null || !from.hasEntity()) {
            return null;
        }
        return convertElement(from);
    }

    @Override
    public List<T> convertAll(Collection<R> from) {
        return from.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<F> reverseAll(Collection<T> to) {
        return to.stream().map(this::reverse).collect(Collectors.toList());
    }
}
