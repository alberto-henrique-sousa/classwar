package br.com.classwar.test;

import br.com.classwar.bean.EngineBean;
import br.com.classwar.bean.GameBean;
import br.com.classwar.util.Console;

public class EasyTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EngineBean engineBean = new EngineBean();		
		engineBean.init();
		engineBean.getSettings().setActionsRound(4);
		engineBean.getSettings().setTotalPlayers(1);
		engineBean.getSettings().setGold(6000);
		engineBean.setNameTeam1("Equipe A");
		engineBean.setNameTeam2("Equipe B");
		engineBean.open();
		
		GameBean gameBean1 = new GameBean();
		gameBean1.setPlayerName("Player 1");
		gameBean1.setEngineBean(engineBean);
		gameBean1.setNrPlayer(1);
		gameBean1.setTeam(1);
		gameBean1.iReady();
		
		GameBean gameBean2 = new GameBean();
		gameBean2.setPlayerName("Player 2");
		gameBean2.setEngineBean(engineBean);
		gameBean2.setNrPlayer(2);
		gameBean2.setTeam(2);
		gameBean2.iReady();
		
		Easy easyPlayer1 = new Easy(engineBean.getTeam1().getPlayers()[0], 1);
		Easy easyPlayer2 = new Easy(engineBean.getTeam2().getPlayers()[0], 2);
						
		// Primeira Rodada
		
		easyPlayer1.run(); 
		easyPlayer2.run();
		engineBean.getTeam1().getPlayers()[0].setCountActions(0);
		engineBean.getTeam2().getPlayers()[0].setCountActions(0);

		//easyPlayer1.run(); 
		//easyPlayer2.run(); 
		
		// Rodadas...
		int round = 0; //&& (engineBean.getTeam1().totalUnits() > 0 && engineBean.getTeam2().totalUnits() > 0)
		while (round < 200 && (engineBean.getTeam1().totalUnits() > 0 && engineBean.getTeam2().totalUnits() > 0)) {
			easyPlayer1.run(); 
			easyPlayer2.run();
			engineBean.getTeam1().getPlayers()[0].setCountActions(0);
			engineBean.getTeam2().getPlayers()[0].setCountActions(0);
			round ++;
		}
		System.out.println("---###--- RODADA: " + round);
		
		// Console...
		for (Console console : engineBean.getListConsole()) {
			System.out.println("["+console.getId() + "] " + console.getPlayer().getName() + ": "+console.getAction() + " => " + console.getMsg());
		}
		System.out.println("Unidades Equipe A: " + engineBean.getTeam1().totalUnits());
		System.out.println("Unidades Equipe B: " + engineBean.getTeam2().totalUnits());
		
	}

}
