package ru.javawebinar.topjava.util.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class StringToLocalDateConverter implements Converter<String, LocalDate>{
    @Override
    public LocalDate convert(String source) {
        return StringUtils.hasLength(source) ? LocalDate.parse(source) : null;
    }
}
