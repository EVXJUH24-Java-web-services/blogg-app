package se.deved.blogg_app.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.deved.blogg_app.comment.CommentController;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserController;
import se.deved.blogg_app.utility.ErrorResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("blog-post")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody CreateBlogPostDTO createBlogPost,
            @AuthenticationPrincipal User user
    ) {
        try {
            BlogPost post = blogPostService.createPost(createBlogPost.content, createBlogPost.commentsDisabled, user);

            EntityModel<BlogPostResponseDTO> entityModel = EntityModel.of(BlogPostResponseDTO.fromModel(post));

            Link userLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserController.class).getUserInfo(post.getUser().getId())
            ).withRel("user");

            entityModel.add(userLink);

            return ResponseEntity.ok(entityModel);
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
    }

    @AllArgsConstructor
    @Getter
    public static class BlogPostResponseDTO extends RepresentationModel<BlogPostResponseDTO> {
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
