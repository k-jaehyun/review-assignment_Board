package com.sparta.plusweekreviewassignment.likes;

import com.sparta.plusweekreviewassignment.User.User;
import com.sparta.plusweekreviewassignment.User.UserRepository;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import com.sparta.plusweekreviewassignment.post.Post;
import com.sparta.plusweekreviewassignment.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public String postLikeOrNot(Long postId, String value) {
        String nickname = getNicknameFromToken(value);
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저닉네임"));

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 해당 유저가 post에 좋아요가 없다면 새로운 likes 생성
        var likes = likesRepository.findByPostAndUser(post,user).orElse(null);
        if (likes == null) {
            Likes newLikes = new Likes(user, post);
            likesRepository.save(newLikes);
            return newLikes.getIslike().toString();
        } else {
            // 좋아요가 있다면 true/false 변경
            likes.likeOrNot(!likes.getIslike());
            return likes.getIslike().toString();
        }
    }

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
