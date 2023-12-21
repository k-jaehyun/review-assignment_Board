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
        jwtUtil.validateToken(value);
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto,post);
        commentRepository.save(comment);
    }


}
