package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class LinkTypeDto {
    private Long id;
    private String name;
}
