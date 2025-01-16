package se.deved.blogg_app.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.deved.blogg_app.blog.BlogPost;
import se.deved.blogg_app.comment.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "blog_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID id;

    private String name;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<BlogPost> blogPosts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User(String name, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.password = password;
        this.blogPosts = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}
