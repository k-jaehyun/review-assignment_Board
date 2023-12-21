package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import com.sparta.plusweekreviewassignment.post.Post;
import com.sparta.plusweekreviewassignment.post.PostRepository;
import com.sparta.plusweekreviewassignment.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;

    public void addCommnet(CommentRequestDto requestDto, Long postId, String value) {
        String token = jwtUtil.substringBearerToken(value);
        jwtUtil.validateToken(token);
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto,post);
        commentRepository.save(comment);
    }

    // Post의 댓글 목록 조회
    public List<CommentResponseDto> getCommentList(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return commentRepository.findByPost(post).stream().map(CommentResponseDto::new).toList();
    }
}
