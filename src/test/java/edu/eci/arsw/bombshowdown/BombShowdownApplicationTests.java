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
			bombShowdownGame.addQueue("Player 9", "aupo");
			Assert.assertFalse(bombShowdownGame.checkBonusWord());
			bombShowdownGame.addPlayer("Player 10");
			bombShowdownGame.addQueue("Player 10", "auditorio");
			Assert.assertTrue(bombShowdownGame.checkBonusWord());
		} catch (Exception e) {
			System.out.println("Error al revisar palabra en ronda bonus");
		}
	}

	@Test
	public void shouldGetSyllable() {
		try {
			Assert.assertEquals("au", bombShowdownGame.getSyllable());
		} catch (Exception e) {
			System.out.println("Error al revisar silaba");
		}
	}

	@Test
	public void shouldFindPlayer() {
		try {
			Assert.assertNull(bombShowdownGame.find("Player 11"));
			bombShowdownGame.addPlayer("Player 11");
			Assert.assertEquals("Player 11", bombShowdownGame.find("Player 11").getName());
		} catch (Exception e) {
			System.out.println("Error al encontrar jugador");
		}
	}

	@Test
	public void shouldUpdateLives() {
		try {
			bombShowdownGame.updateLives("Player 12");
			bombShowdownGame.addPlayer("Player 12");
			bombShowdownGame.updateLives("Player 12");
			Assert.assertEquals(2, bombShowdownGame.find("Player 12").getLives());
		} catch (Exception e) {
			System.out.println("Error al actualizar vidas");
		}
	}

	@Test
	public void shouldSetTime() {
		try {
			bombShowdownGame.setTimeSinceLastTurn(123445);
			Assert.assertEquals(123445, bombShowdownGame.getTimeSinceLastTurn());
		} catch (Exception e) {
			System.out.println("Error al configurar tiempo");
		}
	}

	@Test
	public void shouldToJson() {
		try {
			bombShowdownGame.addPlayer("Player 13");
			Assert.assertEquals("{\"players\":[{\"lives\":2,\"name\":\"Player 13\",\"id\":0}],\"currentPlayer\":0,\"currentSyllable\":\"au\",\"started\":false,\"bonusWinner\":false,\"timeSinceLastTurn\":0,\"deadCount\":0}", bombShowdownGame.toJsonElement());
		} catch (Exception e) {
			System.out.println("Error al convertir a JSON");
		}
	}

	@Test
	public void shouldToString() {
		try {
			bombShowdownGame.addPlayer("Player 14");
			Assert.assertEquals("BombShPersistenceImpl{players=[{\"lives\":2, \"name\":\"Player 14\", \"id\":0}], currentPlayer=0, currentSyllable='au', started=false, bonusWinner=false, timeSinceLastTurn=0, deadCount=0}", bombShowdownGame.toString());
		} catch (Exception e) {
			System.out.println("Error al convertir a JSON");
		}
	}

}
