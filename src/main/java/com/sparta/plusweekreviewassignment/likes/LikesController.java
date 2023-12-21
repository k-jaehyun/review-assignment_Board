package com.sparta.plusweekreviewassignment.likes;

import com.sparta.plusweekreviewassignment.common.CommonResponseDto;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/like")
    public ResponseEntity<CommonResponseDto> postLikeOrNot(@PathVariable Long postId,
                                                           @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String value) {
        String trueOrFalse = likesService.postLikeOrNot(postId,value);
        return ResponseEntity.ok().body(new CommonResponseDto(trueOrFalse+" 성공", HttpStatus.OK.value()));
    }
}
