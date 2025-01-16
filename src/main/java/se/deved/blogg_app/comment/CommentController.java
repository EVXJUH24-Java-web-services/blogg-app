package se.deved.blogg_app.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.deved.blogg_app.utility.ErrorResponseDTO;

import java.util.UUID;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> commentOnPost(@RequestBody CreateCommentDTO createComment) {
        try {
            Comment comment = commentService.commentOnPost(createComment.content, createComment.blogPostId, createComment.userId);
            return ResponseEntity.ok(CommentResponseDTO.fromModel(comment));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
        }
    }

    public static class CreateCommentDTO {
        public String content;
        public UUID blogPostId;
        public UUID userId;
    }

    @Data
    @AllArgsConstructor
    public static class CommentResponseDTO {
        private String content;
        private String username;
        private UUID userId;
        private UUID blogPostId;

        public static CommentResponseDTO fromModel(Comment comment) {
            return new CommentResponseDTO(
                    comment.getContent(),
                    comment.getUser().getName(),
                    comment.getUser().getId(),
                    comment.getBlogPost().getId()
            );
        }
    }
}
