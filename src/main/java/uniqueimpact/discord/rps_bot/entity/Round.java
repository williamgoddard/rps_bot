package uniqueimpact.discord.rps_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniqueimpact.discord.rps_bot.util.GameStatus;
import uniqueimpact.discord.rps_bot.util.Selection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToOne
    private Game game;

    @Column(nullable = false)
    private Selection user1Choice = Selection.UNDECIDED;

    @Column(nullable = false)
    private Selection user2Choice = Selection.UNDECIDED;

    @Column(nullable = false)
    private GameStatus outcome = GameStatus.PENDING;

}
