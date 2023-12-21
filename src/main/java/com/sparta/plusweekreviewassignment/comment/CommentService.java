package com.sparta.plusweekreviewassignment.comment;

import com.sparta.plusweekreviewassignment.User.User;
import com.sparta.plusweekreviewassignment.User.UserRepository;
import com.sparta.plusweekreviewassignment.common.PageRequestDto;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import com.sparta.plusweekreviewassignment.post.Post;
import com.sparta.plusweekreviewassignment.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void addCommnet(CommentRequestDto requestDto, Long postId, String value) {
        User user = getUserFromToken(value);
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto,post,user);
        commentRepository.save(comment);
    }

    // Post의 댓글 목록 조회
    public List<CommentResponseDto> getCommentList(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return commentRepository.findByPost(post).stream().map(CommentResponseDto::new).toList();
    }

    // Paging으로 댓글 목록 조회
    public List<CommentResponseDto> getCommentListPaging(PageRequestDto pageRequestDto) {
        Sort.Direction direction = pageRequestDto.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, pageRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(pageRequestDto.getPage()-1, pageRequestDto.getSize(),sort);

        Page<Comment> comments = commentRepository.findAll(pageable);

        return comments.map(CommentResponseDto::new).getContent();

    }

    @Transactional
    public List<CommentResponseDto> modifyComment(CommentRequestDto requestDto, Long postId, Long commentId, String value) {
        User user = getUserFromToken(value);
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getUser().getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("댓글 작성자만 수정 할 수 있습니다.");
        }

        comment.modify(requestDto);
        return commentRepository.findByPost(post).stream().map(CommentResponseDto::new).toList();
    }

    // 댓글 삭제
    public void deleteComment(Long postId, Long commentId, String value) {
        User user = getUserFromToken(value);
        postRepository.findById(postId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        if (!comment.getUser().getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("댓글 작성자만 삭제 할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    // 토큰에서 user 가져오기
    private User getUserFromToken(String value) {
        // 쿠키에서 받아온 헤더의 Bearer토큰 substring
        String token = jwtUtil.substringBearerToken(value);
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        String nickname = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByNickname(nickname).orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저입니다."));
        return user;
    }
}
