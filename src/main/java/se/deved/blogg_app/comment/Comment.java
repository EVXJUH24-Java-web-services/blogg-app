package se.deved.blogg_app.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.deved.blogg_app.blog.BlogPost;
import se.deved.blogg_app.user.User;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    private UUID id;

    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private BlogPost blogPost;

    public Comment(String content, User user, BlogPost blogPost) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.user = user;
        this.blogPost = blogPost;
    }
}
