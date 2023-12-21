package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.common.CommonResponseDto;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommonResponseDto> addComment(@RequestBody CommentRequestDto requestDto,
                                                        @PathVariable Long postId,
                                                        @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        commentService.addCommnet(requestDto,postId,value);
        return ResponseEntity.ok().body(new CommonResponseDto("댓글 작성 완료", HttpStatus.CREATED.value()));
    }

}
