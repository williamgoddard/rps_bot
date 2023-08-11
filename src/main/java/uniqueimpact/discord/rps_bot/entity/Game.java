package uniqueimpact.discord.rps_bot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniqueimpact.discord.rps_bot.util.GameStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player1_id")
    private Player player1;

    @OneToOne
    @JoinColumn(name = "player2_id")
    private Player player2;

    @Column(nullable = false)
    private String player1Channel;

    @Column(nullable = false)
    private String player2Channel;

    @Column(nullable = false)
    private Long timestamp = System.currentTimeMillis();

    @Column(nullable = false)
    private Integer firstTo = 1;

    @Column(nullable = false)
    private Integer player1Score = 0;

    @Column(nullable = false)
    private Integer player2Score = 0;

    @Column(nullable = false)
    private GameStatus status = GameStatus.PENDING;

}
