package br.com.classwar.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.com.classwar.units.Unit;
import br.com.classwar.util.Animation;
import br.com.classwar.util.Player;
import br.com.classwar.util.Team;

@ManagedBean
@SessionScoped
public class GameBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7432571406473598811L;
	@ManagedProperty(value="#{engineBean}")
	protected EngineBean engineBean;
	private int team;
	private String playerName;
	private boolean ready;
	private int nrPlayer;
	private String code;
	private Player currentPlayer;
	private boolean turn = true;
	private boolean callUpdateJs;
	private int idAnimation;
	private boolean autoSendCode;
	
	@PostConstruct
	public void init() {
		this.team = 1;
		this.nrPlayer = -1;
		this.idAnimation = -1;
	}	
	
	public void start() {
		if (this.engineBean.isStarded()) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml");							
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void iReady() {
		this.nrPlayer = -1;
		this.playerName = this.playerName.replace("'", "");
		if (this.team == 1) {
			for (int i = 0; i < this.engineBean.getTeam1().getPlayers().length && nrPlayer < 0; i++) {
				if (!this.engineBean.getTeam1().getPlayers()[i].isInUse()) {
					this.engineBean.getTeam1().getPlayers()[i].setName(this.playerName);
					this.engineBean.getTeam1().getPlayers()[i].setInUse(true);
					this.nrPlayer = i;
				}
			}			
		} else if (this.team == 2) {
			for (int i = 0; i < this.engineBean.getTeam2().getPlayers().length && nrPlayer < 0; i++) {
				if (!this.engineBean.getTeam2().getPlayers()[i].isInUse()) {
					this.engineBean.getTeam2().getPlayers()[i].setName(this.playerName);
					this.engineBean.getTeam2().getPlayers()[i].setInUse(true);
					this.nrPlayer = i;
				}
			}						
		}
		this.ready = this.nrPlayer > -1;
	}
	
	public String waitPlayers() {
		String ret = "";
		if (isReady() && !this.getEngineBean().isStarded())
			ret = "Aguardando jogador(es)...";
		return ret;
	}
	
	public void pollIndex() {
		if (!this.engineBean.isOpen() && this.ready) {
			this.nrPlayer = -1;
			this.ready = false;
			this.playerName = "";
			this.currentPlayer = null;
		}	
	}
	
	public void showGrid() {
		RequestContext context = RequestContext.getCurrentInstance();		
		context.execute("showGrid();");
	}
	
	public void fullEditor() {
		RequestContext context = RequestContext.getCurrentInstance();		
		context.execute("fullEditor();");
	}
	
	public void pollGame(boolean monitor) {
		if (!this.engineBean.isOpen()) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");							
			} catch (Exception e) {
				e.printStackTrace();
			}						
		}
		if (this.getCurrentPlayer() == null || (!this.engineBean.getTeam1().allReady() && !this.engineBean.getTeam2().allReady() && this.callUpdateJs)) {
			RequestContext context = RequestContext.getCurrentInstance();
			String jsAnimation = "arrayAnimation = new Array(";
			int totalAnimation = 0;
			if (this.getCurrentPlayer() != null) {
				this.callUpdateJs = false;
				this.engineBean.getTeam1().allWait(false);
				this.engineBean.getTeam2().allWait(false);
			}	
			
			if (this.getEngineBean().getTeam1() != null && this.getEngineBean().getTeam2() != null) {
				if (this.idAnimation != this.engineBean.getIdAnimation()) {
					for (Animation animation : this.getEngineBean().getListAnimation()) {
						String oldClass = "";
						String id = "";
						String newClass = "";
						String unitTitle = "";
						int unitTeam = 0;
						if (animation.getUnit() != null) {
							id = animation.getCell() != null && !animation.getCell().isEmpty() ? animation.getCell() : engineBean.convertIndexLetter(animation.getUnit().getRow()+1)+(animation.getUnit().getCol()+1);
							newClass = "animation-"+(!animation.getSpriter().equals("dead") ? animation.getUnit().getTitle()+"-" : "")+(animation.getUnit().getPlayer().getTeam()==1 ? "green" : "purple")+"-" +animation.getSpriter();
							String color = (animation.getUnit().getPlayer().getTeam()==1 ? "green" : "purple");
							if (animation.getUnit().getType() == Unit.CHAMPION)
								newClass += " champion-" + color;
							if (((this.team == 1 && animation.getUnit().getPlayer().getTeam() == 2) || 
								 (this.team == 2 && animation.getUnit().getPlayer().getTeam() == 1)))
								newClass += " itemFlipH";
							if (animation.getSpriter().equals("dead"))
								oldClass = "divUnit";
							else {
								oldClass = "animation-"+animation.getUnit().getTitle()+"-"+color+"-idle";
								if (animation.getUnit().getType() == Unit.CHAMPION)
									oldClass += " champion-" + color;
								if ((this.team == 1 && animation.getUnit().getPlayer().getTeam() == 2) || 
									(this.team == 2 && animation.getUnit().getPlayer().getTeam() == 1))
									oldClass += " itemFlipH";
							}	
							unitTitle = animation.getUnit().getTitle();
							unitTeam = animation.getUnit().getPlayer().getTeam();
						} else {
							unitTeam = animation.getTeam();
						}
						jsAnimation += String.format("\"updateAnimation('%s', '%s', '%s', '%s', '%s', '%s', %s, %s, '%s', %s, '%s', %s)\",",
								id,
								newClass,
								oldClass,
								unitTitle,
								animation.getSound(),
								animation.getCellEffect(),
								unitTeam,
								this.team,
								animation.getType(),
								(animation.isFailed() ? "true" : "false"),
								animation.getNamePlayer(),
								(animation.isRotate() ? "true" : "false")
								);
						totalAnimation++;
					}
					this.idAnimation = this.engineBean.getIdAnimation();
				}
				jsAnimation = "countAnimation = 0;totalAnimation = " + totalAnimation + ";" + jsAnimation + "\"\");";
				jsAnimation += monitor ? "updateMonitorRefresh()" : (totalAnimation == 0 ? "updateCodeRefresh()" : "callAnimation()");
				context.execute(jsAnimation);
			}
		}
	}	
	
	public void autoSendCode() {
		this.autoSendCode = !this.autoSendCode;
	}
	
	@SuppressWarnings("unused")
	public void sendCode() {
    	RequestContext context = RequestContext.getCurrentInstance();
		if (currentPlayer != null && !currentPlayer.isWait()) {
			Player playerConsole = this.getCurrentPlayer();
			try {
				this.callUpdateJs = true;
				this.currentPlayer.setCode(this.code);
				this.currentPlayer.setWait(true);
				this.currentPlayer.setReady(true);
				this.currentPlayer.setBad(!this.engineBean.compiler(this.team, this.nrPlayer, this.currentPlayer.getCode()));
				this.getEngineBean().getListRun().add(this.currentPlayer);
				if (this.engineBean.getTeam1().allReady() && this.engineBean.getTeam2().allReady()) {
					this.engineBean.getListAnimation().clear();
					this.engineBean.setIdAnimation(this.engineBean.getIdAnimation()+1);
					this.engineBean.getTeam1().allReady(false);
					this.engineBean.getTeam2().allReady(false);
					String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "compilation/";
					for (Player player : this.getEngineBean().getListRun()) {
						player.setCountActions(0);
						playerConsole = player;
						if (!player.isBad()) {
							Object enginePlayer1 = this.engineBean.loadClass("EnginePlayer" + player.getTeam() + "_" + player.getId(), player, this.team, path, this.getClass().getClassLoader());
							enginePlayer1 = null;						
						} else {
							Animation animation = new Animation(null);
							animation.setFailed(true);
							animation.setSound(Animation.SOUND_FAIL);
							animation.setType("code");
							animation.setNamePlayer(player.getName());
							animation.setTeam(player.getTeam());
							animation.setCellEffect("team_"+player.getTeam()+"_d");
							player.getEngineBean().getListAnimation().add(animation);
						}
					}
					this.getEngineBean().getListRun().clear();
					if (this.getEngineBean().getTeam1().totalUnits() == 0) {
						this.engineBean.setGameOver(true);
						this.engineBean.setPlayerVictor("Vencedor: " + this.engineBean.getTeam2().getName());
						Animation animation = new Animation(null);
						animation.setFailed(true);
						animation.setSound(Animation.SOUND_GAMEOVER);
						animation.setCellEffect("team_2_d");
						animation.setType("gameover");
						animation.setTeam(2);
						this.currentPlayer.getEngineBean().getListAnimation().add(animation);								
					} else if (this.getEngineBean().getTeam2().totalUnits() == 0) {
						this.engineBean.setGameOver(true);
						this.engineBean.setPlayerVictor("Vencedor: " + this.engineBean.getTeam1().getName());					
						Animation animation = new Animation(null);
						animation.setFailed(true);
						animation.setSound(Animation.SOUND_GAMEOVER);
						animation.setCellEffect("team_1_d");
						animation.setType("gameover");
						animation.setTeam(1);
						this.currentPlayer.getEngineBean().getListAnimation().add(animation);								
					}				
				}
			} catch (Exception e) {
				e.printStackTrace();
				Animation animation = new Animation(null);
				animation.setFailed(true);
				animation.setSound(Animation.SOUND_FAIL);
				animation.setType("code");
				animation.setNamePlayer(playerConsole.getName());
				animation.setTeam(playerConsole.getTeam());
				animation.setCellEffect("team_"+playerConsole.getTeam()+"_d");
				playerConsole.getEngineBean().getListAnimation().add(animation);
				if (e.getMessage() != null) {
					String msg[] = e.getMessage().split("\n");
					this.engineBean.addMsgConsole(playerConsole, "error", msg);
				}	
			}
	    	context.execute(this.engineBean.getCallErrorEditor() + ";updateCode()");
		} else {
	    	context.execute("updateCode()");			
		}
	}
	
	public Player getCurrentPlayer() {
		this.currentPlayer = null;
		if (this.nrPlayer > -1) {
			if (this.team == 1 && this.engineBean.getTeam1() != null && this.engineBean.getTeam1().getPlayers() != null && this.engineBean.getTeam1().getPlayers().length >= nrPlayer)
				this.currentPlayer = this.engineBean.getTeam1().getPlayers()[nrPlayer];
			else if (this.team == 2 && this.engineBean.getTeam2() != null && this.engineBean.getTeam2().getPlayers() != null && this.engineBean.getTeam2().getPlayers().length >= nrPlayer)
				this.currentPlayer =  this.engineBean.getTeam2().getPlayers()[nrPlayer];
		}
		return this.currentPlayer;
	}

	private Unit selectUnit(int row, int col) {
		Unit unit = row >= 0 && row < 4 && col >=0 && col <12 ? this.engineBean.getBoardGame()[row][col] : null;
		return unit;
	}

	public String namePleryUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = unit.getPlayer().getName();
		return ret;
	}	

	public String cssUnit(int row, int col) {
		String ret = "divUnit";
		Unit unit = selectUnit(row, col);
		if (unit != null) {
			String color = (unit.getPlayer().getTeam()==1 ? "green" : "purple");
			ret = "animation-"+unit.getTitle()+"-"+color+"-idle";
			if (unit.getType() == Unit.CHAMPION)
				ret += " champion-" + color;
			if ((this.team == 1 && unit.getPlayer().getTeam() == 2) || 
				(this.team == 2 && unit.getPlayer().getTeam() == 1))
				ret += " itemFlipH";
		}	
		return ret;
	}	
	
	public String titleUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = unit.getTitle() + (unit.getType() == Unit.CHAMPION ? " (champion)" : "");
		return ret;
	}
	
	public String lifeUnit(int row, int col) {
		String ret = "0";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = String.valueOf(unit.getLife());
		return ret;
	}
	
	public String agilityUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "agilidade: " + unit.getAgility() + " casa(s)";
		return ret;
	}		
	
	public String forceUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "força: " + unit.getForce();
		return ret;
	}	
	
	public String attackUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "ataque: " + unit.getAttack() + " casa(s)";
		return ret;
	}
		
	public String defenseUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "defesa: " + unit.getDefense();
		return ret;
	}
	
	public String costUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "custo: $ " + unit.getCost();
		return ret;
	}
	
	public String costChampionUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "champion: $ " + unit.getCostChampion() + " (+"+unit.getPowerChampion()+"% força/defesa)";
		return ret;
	}		
		
	public String bonusUnit(int row, int col) {
		String ret = "";
		Unit unit = selectUnit(row, col);
		if (unit != null)
			ret = "bônus: -"+unit.getBonus()+"% defesa (ataque na retaguarda)";
		return ret;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public int getNrPlayer() {
		return nrPlayer;
	}

	public void setNrPlayer(int nrPlayer) {
		this.nrPlayer = nrPlayer;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public EngineBean getEngineBean() {
		return engineBean;
	}

	public void setEngineBean(EngineBean engineBean) {
		this.engineBean = engineBean;
	}

	public String getCode() {
		if (getCurrentPlayer() != null && (code == null || code.trim().isEmpty()))
			return getCurrentPlayer().getCode();
		else
			return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String imageBtnSend() {
		return this.currentPlayer != null && this.currentPlayer.isWait() && !this.engineBean.isGameOver() ? "wait" : "send";
	}

	public String imageAutoBtnSend() {
		return this.autoSendCode ? "on" : "off";
	}	
	
	public String waitText() {
		String ret = this.currentPlayer != null && this.currentPlayer.isWait() && !this.engineBean.isGameOver() ? "Aguardando a ações..." : "";
		return ret;
	}
	
	public String badText() {
		return this.currentPlayer != null && this.currentPlayer.isBad() ? "Codificação falhou!" : "";
	}	

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	public String cssDisplayNone() {
		return this.currentPlayer == null ? ";display: none;" : "";
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
	
	public boolean openSession() {
		return nrPlayer > -1;
	}
	
	public int indexBoard(int x) {
		return this.team == 1 ? x : 13-x;
	}

	public Team currentTeam() {
		if (this.team == 1)
			return this.getEngineBean().getTeam1();
		else
			return this.getEngineBean().getTeam2();
	}

	public Team advTeam() {
		if (this.team == 1)
			return this.getEngineBean().getTeam2();
		else
			return this.getEngineBean().getTeam1();
	}

	public boolean isAutoSendCode() {
		return autoSendCode;
	}

	public void setAutoSendCode(boolean autoSendCode) {
		this.autoSendCode = autoSendCode;
	}

}
