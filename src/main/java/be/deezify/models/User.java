package be.deezify.models;

import be.deezify.models.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements UserDTO, Indexable {

    public static final User GUEST_USER = new User("Guest", true);

    private int id = 0;
    @NonNull
    private String username;
    private transient final boolean guest;

    public User(String username) {
        this(username, false);
    }

}
