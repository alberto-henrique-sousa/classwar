// CodeMirror, copyright (c) by Marijn Haverbeke and others
// Distributed under an MIT license: http://codemirror.net/LICENSE

(function(mod) {
  if (typeof exports == "object" && typeof module == "object") // CommonJS
    mod(require("../../lib/codemirror"));
  else if (typeof define == "function" && define.amd) // AMD
    define(["../../lib/codemirror"], mod);
  else // Plain browser env
    mod(CodeMirror);
})(function(CodeMirror) {
  "use strict";

  var WORD = /[\w$]+/, RANGE = 500;

  CodeMirror.registerHelper("hint", "classwar", function(editor, options) {
    var word = options && options.word || WORD;
    var range = options && options.range || RANGE;
    var cur = editor.getCursor(), curLine = editor.getLine(cur.line);
    var end = cur.ch, start = end;
    while (start && word.test(curLine.charAt(start - 1))) --start;
    var curWord = start != end && curLine.slice(start, end);

    var list = options && options.list || [], seen = {};
    
    list.push('addUnit(ARCHER, "a1"); // boolean');
    list.push('addUnit(GUARDIAN, "a2"); // boolean');
    list.push('addUnit(KNIGHT, "a3"); // boolean');
    list.push('addUnit(WARRIOR, "a4"); // boolean');
    list.push('-');
    list.push('addUnits(new String[]{WARRIOR, KNIGHT, ARCHER, GUARDIAN}, 4, "a"); // void');
    list.push('-');
    list.push('championUnit("cell"); // boolean');
    list.push('-');
    list.push('move("cellOrig", "cellDest"); // boolean');
    list.push('-');
    list.push('attack("cellOrig", "cellDest"); // boolean');
    list.push('-');
	list.push('game(GAME_ACTIONS_ROUND); // Integer');
	list.push('game(GAME_GOLD_PLAYER); // Integer');
	list.push('game(GAME_PLAYER_TEAM); // Integer (1 or 2)');
	list.push('game(PLAYER_NAME); // String|null');
	list.push('game(PLAYER_GOLD); // Integer|null');
	list.push('game(PLAYER_TOTAL_ACTIONS); // Integer|null');
	list.push('unit("cell", TITLE); // String|null');
	list.push('unit("cell", PLAYER_NAME); // String|null');
	list.push('unit("cell", TEAM); // Integer|null (1 or 2)');
	list.push('unit("cell", TEAM_GOLD); // Integer|null');
	list.push('unit("cell", LIFE); // Integer|null');
	list.push('unit("cell", FORCE); // Integer|null');
	list.push('unit("cell", AGILITY); // Integer|null');
	list.push('unit("cell", ATTACK); // Integer|null');
	list.push('unit("cell", DEFENSE); // Integer|null');
	list.push('unit("cell", COST); // Integer|null');
	list.push('unit("cell", COST_CHAMPION); // Integer|null');
	list.push('unit("cell", POWER_CHAMPION); // Integer|null');
	list.push('unit("cell", BONUS); // Integer|null');
    list.push('-');
    list.push('memory(); // Map<String,String>');
    list.push('-');
    list.push('cellLastMove(); // String');
    list.push('cellLastAttack(); // String');
    
    return {list: list, from: CodeMirror.Pos(cur.line, start), to: CodeMirror.Pos(cur.line, end)};
  });
});
