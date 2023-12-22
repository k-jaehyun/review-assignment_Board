package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.common.dto.CommonResponseDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorDto;
import com.sparta.plusweekreviewassignment.exception.fieldError.FieldErrorException;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import com.sparta.plusweekreviewassignment.post.dto.PostListResponseDto;
import com.sparta.plusweekreviewassignment.post.dto.PostRequestDto;
import com.sparta.plusweekreviewassignment.post.dto.PostResponseDto;
import com.sparta.plusweekreviewassignment.post.dto.PostResponseWithCommentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok().body(
                new CommonResponseDto("게시되었습니다.", HttpStatus.CREATED.value())
        );
    }

    // 게시글 리스트 조회
    @GetMapping("")
    public ResponseEntity<List<PostListResponseDto>> getPostList() {
        List<PostListResponseDto> responseDtoList = postService.getPostList();
        return ResponseEntity.ok().body(new ArrayList<>(responseDtoList));
    }

    // 게시글 리스트를 Paging 조회
    @GetMapping("/page")
    public List<PostListResponseDto> getPostListPaging(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,  // title, createdAt 등등
            @RequestParam("isAsc") boolean isAsc) {
        return postService.getPostListPaging(page-1, size, sortBy, isAsc);
    }

    // 게시글 조회
    @GetMapping("{postId}")
    public ResponseEntity<PostResponseWithCommentDto> getPost(@PathVariable Long postId) {
        PostResponseWithCommentDto postResponseWithCommentDto = postService.getPost(postId);
        return ResponseEntity.ok().body(postResponseWithCommentDto);
    }

    // 게시글 수정
    @PatchMapping("{postId}")
    public ResponseEntity<PostResponseDto> modifyPost(@PathVariable Long postId,
                                                      @RequestBody PostRequestDto requestDto,
                                                      @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        PostResponseDto postResponseDto = postService.modifyPost(postId,requestDto,value);
        return ResponseEntity.ok().body(postResponseDto);
    }

    // 이미지 포함 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> modifyPostWithImage(
            @PathVariable Long postId,
            @RequestParam("image")MultipartFile imageFile,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {

        PostResponseDto postResponseDto = postService.modifyPostWithImage(postId,imageFile,title, content, value);
        return ResponseEntity.ok().body(postResponseDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> deletePost(@PathVariable Long postId,
                                                        @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        postService.deletePost(postId, value);
        return ResponseEntity.ok().body(new CommonResponseDto("게시글이 삭제되었습니다.",HttpStatus.OK.value()));
    }


}
