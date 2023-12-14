package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<CommonResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        postService.createPost(postRequestDto);
        return ResponseEntity.ok().body(new CommonResponseDto("게시되었습니다.", HttpStatus.CREATED.value()));
    }

    @GetMapping("")
    public ResponseEntity<List<PostListResponseDto>> getPostList() {
        List<PostListResponseDto> responseDtoList = postService.getPostList();
        return ResponseEntity.ok().body(new ArrayList<>(responseDtoList));
    }

    @GetMapping("/page")
    public Page<PostListResponseDto> getPostListPaging(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,  // title, createdAt 등등
            @RequestParam("isAsc") boolean isAsc) {
        return postService.getPostListPaging(page-1, size, sortBy, isAsc);
    }



}
