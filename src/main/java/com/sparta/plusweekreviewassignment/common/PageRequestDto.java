package com.sparta.plusweekreviewassignment.common;

import lombok.Getter;

@Getter
public class PageRequestDto {

    private Integer page;
    private Integer size;
    private String sortBy;
    private Boolean isAsc;
}
