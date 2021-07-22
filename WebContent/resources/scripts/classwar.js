var editorPlayer0 = null;
var volume = 100;
var soundAction = null;
var pathSounds = "/classwar/resources/sounds/";
var soundMusic = new buzz.sound(pathSounds + "a storm is coming.ogg");
var grid = false;
var arrayAnimation;
var countAnimation = 0;
var totalAnimation = 0;
var topDifAdd = 50;
var leftDifAdd = 0;
var timeRunX = 500;
var timeRunY = 500;
var gridInverted = false;
var newDiv = null;

buzz.all().setVolume(volume);

function playSoundBackground() {
	soundMusic.play().fadeIn().loop();
}

function init() {
    CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.classwar});
    }	
	editorPlayer0 = updateCodeMirror("editorPlayer0", "text/x-java", "textEditor0", "", "", true, 180, true);
	editorPlayer0.setCursor(4, 8);
	editorPlayer0.focus();		

	autoSendCode();
}

function autoSendCode() {
	var buttonImg = document.getElementById("formGame:btnAutoSendCode").src;
	if (buttonImg.indexOf('button-on.png') > 0 && countAnimation == 0 && totalAnimation == 0) {
		PrimeFaces.ab({s:"formGame:btnSendCode",u:"formGame:btnSendCode"});		
	}	
	//var x = Math.floor((Math.random() * 180000) + 30000); // Intervalo entre 30 segundos e 3 minutos
	var x = Math.floor((Math.random() * 30000) + 5000);
	window.setTimeout('autoSendCode();', x);	
}

function convertId(id) {
	return id.replace(/:/g, "\\\\:");
}

function updateCodeMirror(idEditor, modeL, idTextArea1, callBtnSave, callUpdateSave, editable, height, change) {
	document.getElementById(idEditor).value = "";
	var data = "";
	if (PF(idTextArea1) != null)
		data = PF(idTextArea1).jq.val();
	else
		data = $("#"+idTextArea1).text();
	$("#" + idEditor).text('');
	var editor = CodeMirror(document.getElementById(idEditor), {
	    value: data, 	
	    lineNumbers: true,
        mode: modeL,
        autoCloseTags: true,
	    keyMap: "sublime",
	    theme: "monokai",
	    autoCloseBrackets: true,
	    matchBrackets: true,
	    showCursorWhenSelecting: true,
	    tabSize: 4,
	    indentUnit: 4,
	    readOnly: !editable,
	    gutters: ["CodeMirror-linenumbers", "breakpoints"],
	    extraKeys: {
	    	"Ctrl-Space": "autocomplete",
	        "Ctrl-S": function(cm) {
	        	//PF('statusDialog').show();
        		PrimeFaces.ab({s:"formGame:btnSendCode",u:"formGame:btnSendCode"});
	        }	    	
	      }	    
	});	
	if (change) {
		editor.on("change", function() {
			PF(idTextArea1).jq.val(editor.getValue());
		});
	}	
	editor.setSize(null, height);
	return editor;
}

function clearErrors() {
	editorPlayer0.clearGutter("breakpoints");
}

function updateErrors(line, msg) {
	var marker = document.createElement("img");
	marker.src = 'resources/images/error.png'; 
	marker.title = msg;
	editorPlayer0.setGutterMarker(line, "breakpoints", marker);
}

function updateCode() {
	PF('statusDialog').hide();
	editorPlayer0.focus();
}

function updateCodeRefresh() {
	PrimeFaces.ab({s:"formGame:pollGame", f:"formGame", u:"formGame:panelBoard formGame:btnSendCode formGame:startTime formGame:panelToolbarTeam1 formGame:panelToolbarTeam2 formGame:panelToolbarPlayer"});	
}

function updateMonitorRefresh() {
	PrimeFaces.ab({s:"formGame:pollGame", f:"formGame", u:"formGame:panelBoard formGame:startTime formGame:panelToolbarTeam1 formGame:panelToolbarTeam2"});	
}

function divMirror(parent, className, position, team, type){	
	var element = $('<div class="divUnitGeneral" style="position: absolute; z-index: 90;width: '+$('.col').css('width')+'"><div class="'+className+'" /></div>');	
	$(element).css('top', position.top + (type == 'add' ? topDifAdd : 0));
	if (team == 1)
		$(element).css('left', position.left - (type == 'add' ? leftDifAdd : 0));
	else
		$(element).css('left', position.left + (type == 'add' ? leftDifAdd : 0));
	$(parent).append(element);		
	return element;
}

function playSound(unit, sound, type, failed) {	
	if (buzz.isOGGSupported() && sound.length > 0) {
		if (soundAction != null) {
			soundAction.stop();
		}	
		soundAction = new buzz.sound(pathSounds + (unit.length > 0 ? unit+"-" : "") + sound + ".ogg");
		if (soundAction != null) {
			if (type == 'dead' || failed)
				soundAction.play().fadeIn();
			else
				soundAction.play().fadeIn().loop();
		}	
	}
}

function endAnimation() {
	if (soundAction != null)
		soundAction.stop();	
	if (countAnimation >= totalAnimation) {
		countAnimation = 0;
		totalAnimation = 0;
		updateCodeRefresh();
	}	
}

function callAnimation() {
	if (countAnimation < totalAnimation) {
		eval(arrayAnimation[countAnimation]);
		countAnimation++;
	} else {
		endAnimation();
	}
}

function updateAnimation(id, newClass, oldClass, unit, sound, idOrig, team, teamView, type, failed, player, rotate) {
	playSound(unit, sound, type, failed);
	if (failed) {
		var positionOrig = $("#unit_" + idOrig).position();
		if (id.length > 0)
			positionOrig = $("#unit_" + id).parent().position();
		newDiv = divMirror("#divBoardgame", "", positionOrig, team, "");
		$(newDiv).css('left', positionOrig.left + 40);
		$( newDiv ).css("width", type == 'gameover' ? "68px" : "57px");
		$( newDiv ).css("height", "70px");
		$( newDiv ).css("background-repeat", "no-repeat");
		var nameImg = (type == 'gameover' ? "trophy.png" : "button-"+type+"-failed.png");
		$( newDiv ).css("background-image", "url(/classwar/resources/images/buttons/"+nameImg+")");
	    $( newDiv ).removeClass().addClass('bounce animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
		      $( newDiv ).fadeTo( "slow" , 0.9, function() {
		    	    if (type != 'gameover') {
			    		$(newDiv).remove();
			    		callAnimation();
		    	    } else {
		    	    	PrimeFaces.ab({s:"formGame:pollGame", f:"formGame", u:"formGame:btnSendCode formGame:startTime formGame:panelToolbarTeam1 formGame:panelToolbarTeam2 formGame:panelToolbarPlayer"});			    	    	
		    	    }	
			  });
		});
	} else {
		if (idOrig.length > 0) {		
			var positionOrig = $("#unit_" + idOrig).parent().position();
			var positionDest = $("#unit_" + id).parent().position();
			newDiv = divMirror("#divBoardgame", newClass, positionOrig, team, type);
			$("#unit_" + idOrig).attr('class', "divUnit");
	
			var left = positionOrig.left;
			if (type == 'add') {
				if (team == 1)
					left -= leftDifAdd;
				else
					left += leftDifAdd;
			}
			var pLeft = positionDest.left - left;
			var pTop = positionDest.top - positionOrig.top - (type == 'add' ? topDifAdd : 0);
			if ((teamView == 1 && team == 1 && positionDest.left < left) || 
				(teamView == 2 && team == 2 && positionDest.left < left) ||
				(teamView == 1 && team == 2 && positionDest.left > left) ||
				(teamView == 2 && team == 1 && positionDest.left > left)) {
				$(newDiv).transition({
					  rotateY: '180deg'
					});			
				pLeft = left - positionDest.left;
			}	
			if (pLeft != 0 && pTop != 0) {
				$(newDiv)
				  .transition({ x: pLeft}, timeRunX, 'linear')
				  .transition({ y: pTop}, timeRunY, 'linear', 
						  function() {
					  		$("#unit_" + id).attr('class', oldClass);
					  		$(newDiv).remove();
					  		callAnimation();
					  		});
			} else {
				$(newDiv)
				  .transition({ x: pLeft}, timeRunX, 'linear', 
						  function() {
				  		$("#unit_" + id).attr('class', oldClass);
				  		$(newDiv).remove();
				  		callAnimation();
				  		})
				  .transition({ y: pTop}, timeRunY, 'linear', 
						  function() {
					  		$("#unit_" + id).attr('class', oldClass);
					  		$(newDiv).remove();
					  		callAnimation();
					  		});			
			}	
		} else {
			var changeClass = (oldClass.length > 0 ? "$('#unit_"+id+"').attr('class', '"+oldClass+"');" : "");
			$('#unit_' + id).attr('class', newClass);
			if (rotate) {
				$('#unit_' + id).transition({
					  rotateY: ((teamView == 2 && team == 1) || (teamView == 1 && team == 2) ? '360deg' : '180deg')
					});							
			}
			window.setTimeout(changeClass + 'callAnimation();', 1100);
		}		
	}
}

function resetAnimation() {
	window.setTimeout('PrimeFaces.ab({s:"formGame:pollGame", f:"formGame", u:"formGame:panelBoard"});', 1100);	
}

function showGrid() {
	grid = !grid;
	var border = (grid ? '1': '0') + 'px solid #dde0e8';
	$(".col").css('border', border);
}

function fullEditor() {
	var buttonImg = document.getElementById("formGame:buttonFullEditor").src;
	var newHeight = 180;
	if (buttonImg.indexOf('button-up.png') > 0) {
		newHeight = $( window ).height() - $( "#divEditor" ).height() + 50;
		buttonImg = buttonImg.replace("button-up.png", "button-down.png"); 
	} else {
		buttonImg = buttonImg.replace("button-down.png", "button-up.png"); 		
    	editorPlayer0.setSize(null, newHeight);
	}	
    $("#divMap").slideToggle("slow", function() {
    	if (newHeight > 180)
    		editorPlayer0.setSize(null, newHeight);
    	document.getElementById("formGame:buttonFullEditor").src = buttonImg;
    });
}