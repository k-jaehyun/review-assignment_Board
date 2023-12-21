package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.common.CommonResponseDto;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 추가
    @PostMapping("")
    public ResponseEntity<CommonResponseDto> addComment(@RequestBody CommentRequestDto requestDto,
                                                        @PathVariable Long postId,
                                                        @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        commentService.addCommnet(requestDto,postId,value);
        return ResponseEntity.ok().body(new CommonResponseDto("댓글 작성 완료", HttpStatus.CREATED.value()));
    }

    // 댓글 목록 조회
    @GetMapping("")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable Long postId) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCommentList(postId);
        return ResponseEntity.ok().body(commentResponseDtoList);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<List<CommentResponseDto>> modifyComment(@RequestBody CommentRequestDto requestDto,
                                                                  @PathVariable Long postId,
                                                                  @PathVariable Long commentId,
                                                                  @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        List<CommentResponseDto> commentResponseDtoList = commentService.modifyComment(requestDto,postId,commentId,value);
        return ResponseEntity.ok().body(commentResponseDtoList);
    }
}
