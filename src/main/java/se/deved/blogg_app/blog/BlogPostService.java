package se.deved.blogg_app.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.deved.blogg_app.user.User;
import se.deved.blogg_app.user.UserRepository;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    public BlogPost createPost(String content, boolean commentsDisabled, User user) {
        //User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        BlogPost post = new BlogPost(content, commentsDisabled, user);

        return blogPostRepository.save(post);
    }

    public Collection<BlogPost> getAllPosts(int page) {
        return blogPostRepository.findAll(PageRequest.of(page, 5)).toList();
    }

    public void likePost(UUID blogPostId) {
        BlogPost post = blogPostRepository.findById(blogPostId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setLikes(post.getLikes() + 1);
        blogPostRepository.save(post);
    }

    public void dislikePost(UUID blogPostId) {
        BlogPost post = blogPostRepository.findById(blogPostId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setDislikes(post.getDislikes() + 1);
        blogPostRepository.save(post);
    }
}
