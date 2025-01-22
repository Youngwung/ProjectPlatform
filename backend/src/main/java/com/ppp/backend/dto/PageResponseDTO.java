package com.ppp.backend.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Data;

@Data
public class PageResponseDTO<E> {
	
	// dto의 목록
	private List<E> dtoList;

	// 페이지 번호의 리스트
	// 보여줄 페이지 번호 리스트
	private List<Integer> pageNumList;
	
	private PageRequestDTO pageRequestDTO;

	// 이전페이지와 다음페이지가 존재하는 지 저장하기 위한 필드
	private boolean prev, next;

	// 이전 페이지가 몇인 지, 다음 페이지가 몇인 지 등을 저장하는 필드
	private int totalCount, prevPage, nextPage, totalPage, current;
	
	// 생성자
	public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {

		this.dtoList = dtoList;
		this.pageRequestDTO = pageRequestDTO;
		this.totalCount = (int)total;


		// ! 현재 페이지가 1~10에 속하는 지 11~20에 속하는 지 구하기 위한 알고리즘 작성
		// ! 여기서 1이 start, 10이 end
		// * last는 totalCount로 계산함

		// 끝 페이지 end가 몇 인 지 구하기.
		// 나누는 숫자가 실수여야 원하는 결과가 나옴.
		int end = (int) (Math.ceil(pageRequestDTO.getPage() / (pageRequestDTO.getSize() + 0.0))) * pageRequestDTO.getSize(); 
		// ceil: 올림 함수

		// 시작 페이지가 몇 인 지 구하기
		int start = end - (pageRequestDTO.getSize() - 1);

		// 완전 끝 페이지
		int last = (int) (Math.ceil(totalCount/(double) pageRequestDTO.getSize()));

		// 전체 페이지 개수가 7인데 end는 고정된 값인 10(바꾸지 않았다면)이라면
		// end 값은 7로 바뀌어야 함.
		end = end > last ? last : end;

		// 한 번이라도 다음 페이지를 누른 경우 prev = true
		this.prev = start > 1;

		// size = 10일 때 11페이지가 존재하면 next = true
		this.next = totalCount > end * pageRequestDTO.getSize();

		// 시작값과 끝 값으로 List<Integer>를 만들어주는 메서드
		this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

		// 이전 페이지가 존재하면 시작번호 -1을 저장
		this.prevPage = prev ? start - 1 : 0;

		// 다음 페이지가 존재하면 끝번호 +1을 저장
		this.nextPage = next ? end + 1 : 0;
		
		this.totalPage = this.pageNumList.size();
		this.current= pageRequestDTO.getPage();
		
	}
	
}