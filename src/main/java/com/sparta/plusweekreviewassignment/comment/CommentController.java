package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.comment.dto.CommentRequestDto;
import com.sparta.plusweekreviewassignment.comment.dto.CommentResponseDto;
import com.sparta.plusweekreviewassignment.common.dto.CommonResponseDto;
import com.sparta.plusweekreviewassignment.common.dto.PageRequestDto;
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

    // 댓글 목록 페이징 조회
    @GetMapping("/page")
    public List<CommentResponseDto> getCommentListPaging(@RequestBody PageRequestDto pageRequestDto) {
        return commentService.getCommentListPaging(pageRequestDto);
    }

    // 댓글 수정
    @PatchMapping("{commentId}")
    public ResponseEntity<List<CommentResponseDto>> modifyComment(@RequestBody CommentRequestDto requestDto,
                                                                  @PathVariable Long postId,
                                                                  @PathVariable Long commentId,
                                                                  @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        List<CommentResponseDto> commentResponseDtoList = commentService.modifyComment(requestDto,postId,commentId,value);
        return ResponseEntity.ok().body(commentResponseDtoList);
    }

    // 댓글 삭제
    @DeleteMapping("{commentId}")
    public ResponseEntity<CommonResponseDto> deleteComment(@PathVariable Long postId,
                                                           @PathVariable Long commentId,
                                                           @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        commentService.deleteComment(postId,commentId,value);
        return ResponseEntity.ok().body(new CommonResponseDto("삭제되었습니다.",HttpStatus.OK.value()));
    }
}
