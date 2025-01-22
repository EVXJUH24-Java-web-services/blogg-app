package se.deved.blogg_app.user;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.deved.blogg_app.blog.BlogPost;
import se.deved.blogg_app.comment.Comment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "blog_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    private UUID id;

    private String name;
    private String password;
    private boolean admin = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<BlogPost> blogPosts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    public User(String name, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.password = password;
        this.blogPosts = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admin) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return List.of();
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
