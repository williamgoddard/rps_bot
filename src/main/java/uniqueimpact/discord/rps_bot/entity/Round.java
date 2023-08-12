package uniqueimpact.discord.rps_bot.entity;

import jakarta.persistence.*;
import lombok.*;
import uniqueimpact.discord.rps_bot.util.RoundOutcome;
import uniqueimpact.discord.rps_bot.util.Selection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name="round_num", nullable = false)
    private Integer roundNum = 1;

    @Column(nullable = false)
    private Selection player1Choice = Selection.UNDECIDED;

    @Column(nullable = false)
    private Selection player2Choice = Selection.UNDECIDED;

    public RoundOutcome getOutcome() {
        if (player1Choice == Selection.UNDECIDED || player2Choice == Selection.UNDECIDED) {
            return RoundOutcome.UNDECIDED;
        }
        if (player1Choice == player2Choice) {
            return RoundOutcome.TIE;
        }
        if (player2Choice == Selection.FORFEIT) {
            return RoundOutcome.PLAYER_1_WIN;
        }
        if (player1Choice == Selection.ROCK && player2Choice == Selection.SCISSORS) {
            return RoundOutcome.PLAYER_1_WIN;
        }
        if (player1Choice == Selection.PAPER && player2Choice == Selection.ROCK) {
            return RoundOutcome.PLAYER_1_WIN;
        }
        if (player1Choice == Selection.SCISSORS && player2Choice == Selection.PAPER) {
            return RoundOutcome.PLAYER_1_WIN;
        }
        return RoundOutcome.PLAYER_2_WIN;
    }

}
