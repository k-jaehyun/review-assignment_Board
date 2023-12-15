package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.User.User;
import com.sparta.plusweekreviewassignment.User.UserRepository;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public void createPost(PostRequestDto postRequestDto, String tokenValue) {
        // 쿠키에서 받아온 헤더의 Bearer토큰 substring
        String token = jwtUtil.substringBearerToken(tokenValue);
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        // 토큰에서 nickname 가져오기
        String nickname = jwtUtil.getUsernameFromToken(token);

        // username 검증
        User user = userRepository.findByNickname(nickname).orElseThrow(()-> new IllegalArgumentException("토큰의 nickname은 존재하지 않는 유저입니다."));

        // Post 만들기
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
    }

    public List<PostListResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostListResponseDto::new).toList();
    }

    public Page<PostListResponseDto> getPostListPaging(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page,size,sort);

        Page<Post> postList = postRepository.findAll(pageable);

        return postList.map(PostListResponseDto::new);
    }
}
