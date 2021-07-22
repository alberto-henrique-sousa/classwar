package br.com.classwar.bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.classwar.units.Unit;
import br.com.classwar.util.Animation;
import br.com.classwar.util.Codification;
import br.com.classwar.util.Console;
import br.com.classwar.util.Player;
import br.com.classwar.util.Settings;
import br.com.classwar.util.Team;

@ManagedBean
@ApplicationScoped
public class EngineBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5241106609622266733L;
	private Unit[][] boardGame;
	private String nameTeam1;
	private String nameTeam2;
	private Team team1;
	private Team team2;
	private boolean starded;
	private String path;
	private List<Console> listConsole;
	private List<Codification> listCodification;
	private String startTime;
	private Long timeStart;
	private boolean gameOver;
	private String playerVictor = "";
	private String callErrorEditor = "";
	private String cellLastAttack;
	private String cellLastMove;
	private boolean open;
	private Settings settings;
	private List<Player> listRun;
	private int idAnimation; 
	private List<Animation> listAnimation;
	private SimpleDateFormat dateFormat;
	
	@PostConstruct
	public void init() {
		if (FacesContext.getCurrentInstance() != null)
			path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "compilation/";
		else {
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			path = s + "\\WebContent\\compilation\\";
		}			
		this.nameTeam1 = "";
		this.nameTeam2 = "";
		this.settings = new Settings();
		this.listRun = new ArrayList<Player>();
		this.listAnimation = new ArrayList<Animation>();
		this.idAnimation = 1;
		this.listConsole = new ArrayList<Console>();
		this.listCodification = new ArrayList<Codification>();
		this.dateFormat = new SimpleDateFormat("HH:mm:ss");
		readSettigns();
		reset();
	}
	
	private void readSettigns() {
		try {
			String path = "";
			if (FacesContext.getCurrentInstance() != null)
				path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "WEB-INF/";
			else {
				Path currentRelativePath = Paths.get("");
				String s = currentRelativePath.toAbsolutePath().toString();
				path = s + "\\WebContent\\WEB-INF\\";
			}			
			File fileSettings = new File(path + "settings.json");
			if (fileSettings.exists()) {
				Gson gson = new Gson();
				Type listType = new TypeToken<List>() {}.getType();				
		    	List<Unit> listUnits = gson.fromJson(FileUtils.readFileToString(fileSettings), listType);
		    	this.settings.setListUnits(listUnits);
		    }				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void open() {
		if (isOpen())
			close();
		else {
			reset();
			this.open = true;
			timeStart = System.currentTimeMillis();
			this.team1 = new Team(1, this.nameTeam1, this.settings.getTotalPlayers(), this);
			this.team2 = new Team(2, this.nameTeam2, this.settings.getTotalPlayers(), this);
			for (int i = 0; i < this.settings.getTotalPlayers(); i++) {
				this.team1.addPlayer();
				this.team2.addPlayer();			
			}
		}
	}
	
	public void close() {
		this.nameTeam1 = "";
		this.nameTeam2 = "";
		this.settings.setTotalPlayers(1);
		reset();
	}
	
	public String statusRoom() {
		return this.isOpen() ? "Aberta" : "Fechada";
	}
	
	private void reset() {
		timeStart = null;
		this.open = false;
		this.gameOver = false;
		this.playerVictor = "";
		this.listConsole.clear();
		this.team1 = null;
		this.team2 = null;
		this.boardGame = new Unit[4][12];
		this.cellLastAttack = "";
		this.cellLastMove = "";
		this.listRun.clear();
		this.listAnimation.clear();
		this.idAnimation = 1;
		this.listCodification.clear();
	}
		
	public Unit[][] getBoardGame() {
		return boardGame;
	}

	public boolean isStarded() {
		if (this.getTeam1() == null || this.getTeam2() == null)
			this.starded = false;
		else {
			this.starded = true;
			for (int i = 0; i < this.getTeam1().getPlayers().length && this.starded; i++) {
				this.starded = this.getTeam1().getPlayers()[i] == null ? false : this.getTeam1().getPlayers()[i].isInUse();
			}
			for (int i = 0; i < this.getTeam2().getPlayers().length && this.starded; i++) {
				this.starded = this.getTeam2().getPlayers()[i] == null ? false : this.getTeam2().getPlayers()[i].isInUse();
			}
		}	
		return starded;
	}
	
	private String createJava(String name, String code) {
		String java = "import java.io.Serializable;\n"
				+ "import br.com.classwar.util.EnginePlayer;\n"
				+ "import br.com.classwar.util.Player;\n"
				+ "import java.util.Arrays;\n"
				+ "import java.util.ArrayList;\n"
				+ "import java.util.List;\n"
				+ "import java.util.HashMap;\n"
				+ "import java.util.Map;\n"				
				+ "import java.util.Map.Entry;\n"				
				+ "\n"
				+ "public class "+name+" extends EnginePlayer implements Serializable {\n"
				+ "\n"
				+ "	private static final long serialVersionUID = 1L;\n"
				+ "\n"
				+ code
				+ "\n"
				+ "	public "+name+"(Player player, Integer team) {\n"
				+ "		super(player, team);\n"
				+ "	}\n"
				+ "}\n";
		return java;
	}

	public boolean compiler(int team, int nrPlayer, String code) {
		boolean ret = false;
		try {			
			this.callErrorEditor = "";
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			
			File fileFolder = new File(path);
			File fileJava = new File(path + "EnginePlayer" + team + "_" + nrPlayer + ".java");
			if (fileJava.exists())
				fileJava.delete();
			FileUtils.writeStringToFile(fileJava, createJava("EnginePlayer" + team + "_" + nrPlayer, code), "UTF-8");
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(fileFolder));
			File fileLibs = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "WEB-INF/classes");
			fileManager.setLocation(StandardLocation.CLASS_PATH, Arrays.asList(fileLibs));
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			if (!compiler.getTask(null,
		               fileManager,
		               diagnostics,
		               null,
		               null,
		               fileManager.getJavaFileObjectsFromFiles(Arrays.asList(fileJava)))
		            .call()) {
				for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
					String msg[] = diagnostic.getMessage(null).split("\n") ;
					addMsgConsole(getPlayer(team, nrPlayer), "error", msg);
					String msgJS = "Error: ("+(diagnostic.getLineNumber()-12) + "," + diagnostic.getColumnNumber() + ") " + diagnostic.getMessage(null).replace("'", "\\x27").replace("\n", "\\r\\n");
					callErrorEditor += "updateErrors("+(diagnostic.getLineNumber()-12)+", '"+msgJS+"');";
			    }				
			}
		    fileManager.close();
			String time = dateFormat.format(Calendar.getInstance().getTime());
			long id = this.listCodification.size()+1;
			Codification codification = new Codification(id, getPlayer(team, nrPlayer), time, code, callErrorEditor);
			this.listCodification.add(codification);		    
		    if (!callErrorEditor.isEmpty()) {
		    	callErrorEditor = "clearErrors();" + callErrorEditor + ";";
		    } else {
		    	callErrorEditor = "clearErrors();";
		    	ret = true;
		    }
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				String msg[] = e.getMessage().split("\n");
				addMsgConsole(getPlayer(team, nrPlayer), "error", msg);
			}	
		}
		return ret;
	}

	public void addMsgConsole(Player player, String action, String[] msg) {
		String time = dateFormat.format(Calendar.getInstance().getTime());
		long id = this.listConsole.size()+1;
		Console console = new Console(id, player, time, action, msg[0]);
		this.listConsole.add(console);		
		for (int i = 1; i < msg.length; i++) {
			id = this.listConsole.size()+1;
			Console errorCompilerC = new Console(id, player, time, action, msg[i]);
			this.listConsole.add(errorCompilerC);		
		}
	}
	
	@SuppressWarnings("resource")
	public Object loadClass(String name, Player player, int team, String folder, ClassLoader classLoader) {
		Object obj = null;
		try {			
			File fileClass = new File(folder);
			@SuppressWarnings("deprecation")
			URL url = fileClass.toURL();
			URL[] urls = new URL[]{url};
			ClassLoader cl = new URLClassLoader(urls, classLoader);
			Method method = cl.loadClass(name).getMethod("run", null);
			obj = cl.loadClass(name).getConstructor(Player.class, Integer.class).newInstance(player, team);
			method.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			Animation animation = new Animation(null);
			animation.setFailed(true);
			animation.setSound(Animation.SOUND_FAIL);
			animation.setType("code");
			animation.setNamePlayer(player.getName());
			animation.setTeam(player.getTeam());
			animation.setCellEffect("team_"+player.getTeam()+"_d");
			player.getEngineBean().getListAnimation().add(animation);
			String trace = player.getEngineBean().stackTrace(e);
			String msg[] = null;
			if (trace != null)
				msg = trace.split("\n");
			addMsgConsole(player, "error", msg);
		}					
		return obj;
	}

	public final String stackTrace(Throwable e) {
		String foo = null;
		try {
			ByteArrayOutputStream ostr = new ByteArrayOutputStream();
			e.printStackTrace( new PrintWriter(ostr,true) );
			foo = ostr.toString();
		} catch (Exception f) {
		}
		return foo;
	}
		
	public List<Console> getListConsole() {
		return listConsole;
	}

	public void setListConsole(List<Console> listConsole) {
		this.listConsole = listConsole;
	}

	public String getStartTime() {
		if (!gameOver && timeStart != null && timeStart instanceof Long) {
			long uptimeStartValue = timeStart;
			long uptime = System.currentTimeMillis() - uptimeStartValue;
			//long uptimeDays = uptime / (1000 * 60 * 60 * 24);

			uptime = uptime % (1000 * 60 * 60 * 24);
			long uptimeHours = uptime / (1000 * 60 * 60);

			uptime = uptime % (1000 * 60 * 60);
			long uptimeMins = uptime / (1000 * 60);
			
			startTime = (uptimeHours < 10 ? "0" : "") + uptimeHours + ":" + (uptimeMins < 10 ? "0" : "") + uptimeMins;
		}					
		return startTime;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public String getCallErrorEditor() {
		return callErrorEditor;
	}

	public void setCallErrorEditor(String callErrorEditor) {
		this.callErrorEditor = callErrorEditor;
	}

	public String getPlayerVictor() {
		return playerVictor;
	}

	public void setPlayerVictor(String playerVictor) {
		this.playerVictor = playerVictor;
	}

	public String getCellLastAttack() {
		return cellLastAttack;
	}

	public void setCellLastAttack(String cellLastAttack) {
		this.cellLastAttack = cellLastAttack;
	}

	public String getCellLastMove() {
		return cellLastMove;
	}

	public void setCellLastMove(String cellLastMove) {
		this.cellLastMove = cellLastMove;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getNameTeam1() {
		return nameTeam1;
	}

	public void setNameTeam1(String nameTeam1) {
		this.nameTeam1 = nameTeam1;
	}

	public String getNameTeam2() {
		return nameTeam2;
	}

	public void setNameTeam2(String nameTeam2) {
		this.nameTeam2 = nameTeam2;
	}

	public List<Player> getListRun() {
		return listRun;
	}

	public void setListRun(List<Player> listRun) {
		this.listRun = listRun;
	}

	public static int convertColCell(String cell) {
		int col = -1;
		if (cell.length() > 1) {
			try {
				col = Integer.valueOf(cell.substring(1))-1;						
			} catch (Exception e) {
			}
		}	
		return col;
	}
	
	public static int convertRowCell(String cell) {
		int row = -1;
		if (cell.length() > 1) {
			String cellRow = cell.substring(0, 1);
			if (cellRow.equalsIgnoreCase("a")) row = 0;
			if (cellRow.equalsIgnoreCase("b")) row = 1;
			if (cellRow.equalsIgnoreCase("c")) row = 2;
			if (cellRow.equalsIgnoreCase("d")) row = 3;
		}	
		return row;
	}
	
	public String convertIndexLetter(int index) {
		String ret = "";
		switch (index) {
		case 1:
			ret = "a";
			break;
		case 2:
			ret = "b";
			break;
		case 3:
			ret = "c";
			break;
		case 4:
			ret = "d";
			break;
		}
		return ret;
	}

	public List<Animation> getListAnimation() {
		return listAnimation;
	}

	public void setListAnimation(List<Animation> listAnimation) {
		this.listAnimation = listAnimation;
	}

	public Player getPlayer(int team, int nrPlayer) {
		Player player = null;
		if (team == 1 && getTeam1() != null && getTeam1().getPlayers() != null && getTeam1().getPlayers().length >= nrPlayer)
			player = getTeam1().getPlayers()[nrPlayer];
		else if (team == 2 && getTeam2() != null && getTeam2().getPlayers() != null && getTeam2().getPlayers().length >= nrPlayer)
			player = getTeam2().getPlayers()[nrPlayer];
		return player;
	}

	public List<Codification> getListCodification() {
		return listCodification;
	}

	public void setListCodification(List<Codification> listCodification) {
		this.listCodification = listCodification;
	}

	public int getIdAnimation() {
		return idAnimation;
	}

	public void setIdAnimation(int idAnimation) {
		this.idAnimation = idAnimation;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public String titleBtnOpen() {
		return isOpen() ? "close" : "open";
	}
}
