package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.CommonResponseDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorException;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 제목, 내용만 Json 형식으로 보내서 createPost
    @PostMapping("")
    public ResponseEntity<CommonResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
                                                        @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value,
                                                        BindingResult bindingResult) {
        // validation검증
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        if (!fieldErrorList.isEmpty()) {
            List<FieldErrorDto> fieldErrorDtoList = fieldErrorList.stream().map(FieldErrorDto::new).toList();
            throw new FieldErrorException("허용되지 않은 입력값입니다.",HttpStatus.BAD_REQUEST.value(),fieldErrorDtoList);
        }

        postService.createPost(postRequestDto, value);
        return ResponseEntity.ok().body(new CommonResponseDto("게시되었습니다.", HttpStatus.CREATED.value()));
    }

    // multipart/form-data 로 전송된 이미지와 제목, 내용 받아와서 createPost
    @PostMapping("/image")
    public ResponseEntity<CommonResponseDto> createPostWithImage(
            @RequestParam("image")MultipartFile imageFile,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {

        postService.createPostWithImage(imageFile,title, content, value);
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
