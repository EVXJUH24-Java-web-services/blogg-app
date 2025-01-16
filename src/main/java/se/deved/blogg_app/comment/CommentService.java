package se.deved.blogg_app.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.deved.blogg_app.blog.BlogPost;
import se.deved.blogg_app.blog.BlogPostRepository;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    public Comment commentOnPost(String content, UUID blogPostId, UUID userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        BlogPost post = blogPostRepository
                .findById(blogPostId)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found"));

        if (post.isCommentsDisabled()) {
            throw new IllegalArgumentException("Comments are disabled.");
        }

        Comment comment = new Comment(content, user, post);
        return commentRepository.save(comment);
    }
}
