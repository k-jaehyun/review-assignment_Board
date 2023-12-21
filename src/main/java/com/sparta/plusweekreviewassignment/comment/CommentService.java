package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import com.sparta.plusweekreviewassignment.post.Post;
import com.sparta.plusweekreviewassignment.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;

    public void addCommnet(CommentRequestDto requestDto, Long postId, String value) {
        getNicknameFromToken(value);
        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto,post);
        commentRepository.save(comment);
    }


    // 토큰에서 nickname 가져오기
    private String getNicknameFromToken(String value) {
        // 쿠키에서 받아온 헤더의 Bearer토큰 substring
        String token = jwtUtil.substringBearerToken(value);
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        String nickname = jwtUtil.getUsernameFromToken(token);
        return nickname;
    }
}
