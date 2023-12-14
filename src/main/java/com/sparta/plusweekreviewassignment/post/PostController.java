package com.sparta.plusweekreviewassignment.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("")
    public ResponseEntity<List<PostResponseDto>> getPostList() {
        List<PostResponseDto> responseDtoList = postService.getPostList();
        return ResponseEntity.ok().body(new ArrayList<>(responseDtoList));
    }
}
