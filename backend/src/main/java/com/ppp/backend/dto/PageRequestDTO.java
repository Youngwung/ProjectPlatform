package com.ppp.backend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class PageRequestDTO {
	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 10;
}
