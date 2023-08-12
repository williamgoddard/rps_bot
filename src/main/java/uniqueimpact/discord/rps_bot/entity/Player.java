package uniqueimpact.discord.rps_bot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Player {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    private String nickname;

}
