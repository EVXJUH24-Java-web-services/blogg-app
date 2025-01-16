package se.deved.blogg_app.blog;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.deved.blogg_app.comment.CommentController;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.utility.ErrorResponseDTO;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("blog-post")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreateBlogPostDTO createBlogPost) {
        try {
            BlogPost post = blogPostService.createPost(createBlogPost.content, createBlogPost.commentsDisabled, createBlogPost.userId);
            return ResponseEntity.ok(BlogPostResponseDTO.fromModel(post));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
        }
    }

    @GetMapping
    public Stream<BlogPostResponseDTO> getPosts(@RequestParam int page) {
        return blogPostService
                .getAllPosts(page)
                .stream()
                .map(BlogPostResponseDTO::fromModel);
    }

    @PutMapping("/like/{blogPostId}")
    public ResponseEntity<?> likePost(@PathVariable UUID blogPostId) {
        try {
            blogPostService.likePost(blogPostId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
        }
    }

    @PutMapping("/dislike/{blogPostId}")
    public ResponseEntity<?> dislikePost(@PathVariable UUID blogPostId) {
        try {
            blogPostService.dislikePost(blogPostId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
        }
    }

    public static class CreateBlogPostDTO {
        public String content;
        public boolean commentsDisabled;
        public UUID userId;
    }

    @AllArgsConstructor
    @Getter
    public static class BlogPostResponseDTO {
        private UUID id;
        private String content;
        private int likes, dislikes;
        private boolean commentsDisabled;
        private UUID userId;
        private String username;
        private List<CommentController.CommentResponseDTO> comments;

        public static BlogPostResponseDTO fromModel(BlogPost post) {
            return new BlogPostResponseDTO(
                    post.getId(),
                    post.getContent(),
                    post.getLikes(),
                    post.getDislikes(),
                    post.isCommentsDisabled(),
                    post.getUser().getId(),
                    post.getUser().getName(),
                    post.getComments().stream().map(CommentController.CommentResponseDTO::fromModel).toList()
            );
        }
    }
}
