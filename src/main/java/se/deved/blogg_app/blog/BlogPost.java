package se.deved.blogg_app.blog;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.deved.blogg_app.comment.Comment;
import se.deved.blogg_app.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class BlogPost {

    @Id
    private UUID id;

    private String content;

    private int likes, dislikes;
    private boolean commentsDisabled;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blogPost")
    private List<Comment> comments;

    public BlogPost(String content, boolean commentsDisabled, User user) {
        this.id = UUID.randomUUID();
        this.likes = 0;
        this.dislikes = 0;
        this.commentsDisabled = commentsDisabled;
        this.content = content;
        this.user = user;
        this.comments = new ArrayList<>();
    }
}
