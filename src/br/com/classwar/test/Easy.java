package br.com.classwar.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.classwar.util.EnginePlayer;
import br.com.classwar.util.Player;				

public class Easy extends EnginePlayer {

	public Easy(Player player, int team) {
		super(player, team);
	}
	
	private String KEY_LASTUNIT = "0";
	private Integer team = 0;
	private String[] Letters = {"a", "b", "c", "d"};
	private String FORWARD = "0"; 
	private String UP = "0";
	private String BACK = "1";
	private String  DOWN = "1";

	@Override
	public void run() {
		try {
			team = (Integer)(game(GAME_PLAYER_TEAM)); // Identifica o time do jogador
			initUnits(); // Cria novas unidades
			actions(0); // Realiza ações de ataque
			actions(1); // Realiza ações de movimentações
		} catch (Exception e) {
			e.printStackTrace();
			console(e);
		}
	}
	
	/*
	 * Cria novas unidades se existir
	 * recursos (gold) e espaço no mapa
	*/	
	private void initUnits() {
		boolean create = true;
		while (create && (Integer)game(PLAYER_GOLD) >= 300) {
			create = createUnit();
		}		
	}
	
	/*
	 * Percorre o mapa conforme o lado da Equipe
	 * e cria uma unidade, alterando entre os tipos (warrior, knight, archer e guardian)
	*/	
	private boolean createUnit() {
		for (int i = 0; i < 4; i++) {				
			for (int j = 1; j < 5; j++) {
				String cell = this.Letters[i] + (this.team == 1 ? j : (13 - j));
				if (unit(cell, LIFE) == null || unit(cell, LIFE).equals(0)) {
					if (addUnit(nextType(), cell)) {
						memory().put(cell, (this.team == 1 ? FORWARD : BACK) + "," + DOWN); // Salva em memória do player em qual célula a unidade foi criada
						return true;						
					}	
				}
			}
		}
		return false;
	}
	
	/*
	 * Próximo tipos da unidade (warrior, knight, archer e guardian)
	 * usa a memória do player para recuperar e armazenar
	*/	
	private String nextType() {
		String type = memory().get(KEY_LASTUNIT);
		if (type == null) {
			type = WARRIOR;
		} else if (type.equals(WARRIOR))
			type = KNIGHT;		
		else if (type.equals(KNIGHT))
			type = ARCHER;
		else if (type.equals(ARCHER))
			type = GUARDIAN;
		else if (type.equals(GUARDIAN))
			type = WARRIOR;
		memory().put(KEY_LASTUNIT, type);
		return type;
	}
	
	private String nextRow(String row, boolean down) {
		String ret = "a";
		if (down) {
			if (row.equals("a"))
				ret = "b";
			else if (row.equals("b"))
				ret = "c";
			else if (row.equals("c"))
				ret = "d";
			else 
				ret = row;
		} else {
			if (row.equals("d"))
				ret = "c";
			else if (row.equals("c"))
				ret = "b";
			else if (row.equals("b"))
				ret = "a";
			else 
				ret = row;			
		}
		return ret;		
	}
	
	private void actions(int type) {
		// faz uma cópia da memória
		Map<String,String> memoryClone = new HashMap<String,String>();
		for (Entry<String, String> entry : memory().entrySet()) {
			memoryClone.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, String> entry : memoryClone.entrySet()) {
			if ((Integer)(game(PLAYER_TOTAL_ACTIONS)) >= (Integer)(game(GAME_ACTIONS_ROUND)))
				return;
		    String cell = entry.getKey();
		    if (cell.isEmpty() || cell == KEY_LASTUNIT || entry.getValue() == null || entry.getValue().isEmpty()) // Pula a chave se estiver sem valor ou ser for de controle dos tipos de unidades
		    	continue;
		    String[] cellValue = entry.getValue().split(",");
		    if (unit(cell, LIFE) != null && !unit(cell, LIFE).equals(0)) { // Se a unidade ainda estiver viva
				Integer square = (Integer)unit(cell, type == 0 ? ATTACK : AGILITY); // Alcance em casas
				String number = cell.replaceAll("[^0-9]", "");
				int col = Integer.valueOf(number);
				String row = cell.replace(number, "");
				String wayX = cellValue[0];
				String wayY = cellValue[1];
				if (type == 1) { // Movimentar
					if ((this.team == 1 && col == 12) || (this.team == 2 && col == 1)) { // está no limite do mapa direito ou esquerdo
						wayX = cellValue[0].equals(FORWARD) ? BACK : FORWARD;
						wayY = cellValue[1];
						if (row.equals("d"))
							wayY = UP;
						else if (row.equals("a"))
							wayY = DOWN;
						row = nextRow(row, wayY.equals(DOWN)); // mudar de linha
						/*if (col == 12)
							col = 12;
						else if (col == 1)
							col = 0;*/
					}
				}
				for (int i = 1; i <= square && (col+i < 15); i++) {
					int newCol = (wayX.equals(FORWARD) ? col+i : col-i);
					if (newCol < 1 || newCol > 12) {
						memory().put(cell, wayX + "," + wayY); // salva o novo direcionamento
						wayX = wayX.equals(FORWARD) ? BACK : FORWARD;
						newCol = (wayX.equals(FORWARD) ? col+i : col-i);
					}
					String cellDest = row + newCol;
					if (type == 0) { // Atacar 
						Object teamUnitAdv = unit(cellDest, TEAM);
						if (teamUnitAdv != null && !teamUnitAdv.equals(this.team)) {
							if (unit(cellDest, LIFE) != null && !unit(cellDest, LIFE).equals(0)) {
								if (attack(cell, cellDest)) {
									if ((Integer)(game(PLAYER_TOTAL_ACTIONS)) >= (Integer)(game(GAME_ACTIONS_ROUND)))
										return;
									else
										break;
								}
							}	
						}					
					} else if (type == 1) { // Movimentar
						if (unit(cellDest, LIFE) == null || unit(cellDest, LIFE).equals(0)) {
							if (move(cell, cellDest)) {
								memory().remove(cell); // Libera essa célula
								memory().put(cellDest, wayX + "," + wayY); // Atribui a nova célula
								if ((Integer)(game(PLAYER_TOTAL_ACTIONS)) >= (Integer)(game(GAME_ACTIONS_ROUND)))
									return;
								else
									break;
							}
						}					
					}
				}
		    } else {
		    	memory().remove(cell);
		    }
		}
	}
	
}
