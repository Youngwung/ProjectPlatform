package com.ppp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
public class PageRequestDTO {
	private int page;

	private int size;

	public PageRequestDTO() {
		this.page = 1;
		this.size = 10;
	}
}
