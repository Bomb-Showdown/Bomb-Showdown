package edu.eci.arsw.bombshowdown;

import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class BombShowdownApplicationTests {

	private BombShPersistence bombShowdownGame;

	public BombShowdownApplicationTests() {
		this.bombShowdownGame = new BombShPersistenceImpl();
	}

	@Test
	public void shouldAddPlayers() {
		try {
			for (int i = 0; i < 2; i++){
				bombShowdownGame.addPlayer("Player " + i);
			}
			Assert.assertEquals(2, bombShowdownGame.getPlayers().size());
		} catch (Exception e) {
			System.out.println("Error al añadir jugadores");
		}
	}

	@Test
	public void shouldCheckWord() throws IOException {
		try {
			bombShowdownGame.addPlayer("Player 2");
			bombShowdownGame.addPlayer("Player 3");
			Assert.assertFalse(bombShowdownGame.checkWord("audio") && bombShowdownGame.checkWord("fdgfghdf"));
		} catch (Exception e) {
			System.out.println("Error al revisar palabra");
		}
	}

	@Test
	public void shouldReduceLive() {
		try {
			bombShowdownGame.addPlayer("Player 4");
			bombShowdownGame.bombExplodes();
			Assert.assertEquals(1, bombShowdownGame.find("Player 4").getLives());
		} catch (Exception e) {
			System.out.println("Error al quitar vidas de un jugador");
		}
	}

	@Test
	public void shouldNextPlayer() {
		try {
			bombShowdownGame.addPlayer("Player 5");
			bombShowdownGame.addPlayer("Player 6");
			bombShowdownGame.nextPlayer();
			Assert.assertEquals(1, bombShowdownGame.getCurrentPlayer().getId());
		} catch (Exception e) {
			System.out.println("Error al cambiar el jugador actual");
		}
	}

	@Test
	public void shouldKillPlayer() {
		try {
			bombShowdownGame.addPlayer("Player 7");
			bombShowdownGame.addPlayer("Player 8");
			bombShowdownGame.bombExplodes();
			bombShowdownGame.killPlayer();
			Assert.assertEquals(1, bombShowdownGame.getDeadCount());
		} catch (Exception e) {
			System.out.println("Error al cambiar el jugador actual");
		}
	}

	@Test
	public void shouldCheckBonusWord() {
		try {
			bombShowdownGame.addPlayer("Player 9");
			bombShowdownGame.addQueue("Player 9", "audio");
			Assert.assertTrue(bombShowdownGame.checkBonusWord());
		} catch (Exception e) {
			System.out.println("Error al revisar palabra en ronda bonus");
		}
	}


}
