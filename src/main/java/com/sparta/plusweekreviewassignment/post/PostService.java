package com.sparta.plusweekreviewassignment.post;

import com.sparta.plusweekreviewassignment.User.User;
import com.sparta.plusweekreviewassignment.User.UserRepository;
import com.sparta.plusweekreviewassignment.jwt.JwtUtil;
import com.sparta.plusweekreviewassignment.post.dto.PostListResponseDto;
import com.sparta.plusweekreviewassignment.post.dto.PostRequestDto;
import com.sparta.plusweekreviewassignment.post.dto.PostResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 이미지 없이 포스팅
    public void createPost(PostRequestDto postRequestDto, String value) {
        String nickname = getNicknameFromToken(value);

        // username으로 user 받아옴
        User user = userRepository.findByNickname(nickname).orElseThrow(
                () -> new IllegalArgumentException("토큰의 nickname은 존재하지 않는 유저입니다.")
        );

        // Post 만들기
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
    }

    // 이미지와 함께 포스팅
    public void createPostWithImage(MultipartFile imageFile, String title, String content, String value) {
        String nickname = getNicknameFromToken(value);

        // username으로 user 받아옴
        User user = userRepository.findByNickname(nickname).orElseThrow(
                () -> new IllegalArgumentException("토큰의 nickname은 존재하지 않는 유저입니다.")
        );

        byte[] imageByte = imageToByteArray(imageFile);
        Post post = new Post(title, content, imageByte, user);

        postRepository.save(post);
    }

    public List<PostListResponseDto> getPostList() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostListResponseDto::new).toList();
    }

    public List<PostListResponseDto> getPostListPaging(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> postList = postRepository.findAll(pageable);

        return postList.map(PostListResponseDto::new).getContent();
    }

    // 게시글 조회
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시물이 존재하지 않습니다."));

        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto modifyPost(Long postId, PostRequestDto requestDto, String value) {
        String nickname = getNicknameFromToken(value);

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        validatePostUser(post, nickname);

        Post modifiedPost = post.modify(requestDto);

        return new PostResponseDto(modifiedPost);
    }

    // 이미지 포함 게시글 수정
    @Transactional
    public PostResponseDto modifyPostWithImage(Long postId, MultipartFile imageFile, String title, String content, String value) {
        String nickname = getNicknameFromToken(value);
        
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        validatePostUser(post, nickname);

        byte[] imageByte = imageToByteArray(imageFile);

        post.modifyWithImage(imageByte,title,content);

        return new PostResponseDto(post,imageByte);
    }

    // 게시글 삭제
    public void deletePost(Long postId, String value) {
        String nickname = getNicknameFromToken(value);

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        validatePostUser(post, nickname);

        postRepository.delete(post);
    }





    // 해당 사용자가 작성한 게시글인지 확인
    private void validatePostUser(Post post, String nickname) {
        if (!post.getUser().getNickname().equals(nickname)) {
            throw new IllegalArgumentException("해당 사용자가 작성한 게시글이 아닙니다.");
        }
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

    // 이미지 받아오는 메서드
    private byte[] imageToByteArray(MultipartFile imageFile) {
        try {
            return imageFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("이미지 변환 실패.");
        }
    }


    // 수정된지 90일이 지난 데이터는 자동으로 지우는 스케줄러 기능
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 00시에 작동
    public void cleanupPost() {
        LocalDateTime before90Days = LocalDate.now().minusDays(90).atStartOfDay();  // 매일 자정을 기준으로 90일이 지났으면 삭제
        postRepository.deleteByModifiedAtBefore(before90Days);
    }

}
