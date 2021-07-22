var element = null;
function moveDiv() {
	var elementOrig = "#div-b2";
	var elementDest = "#div-a0";	
	if (element != null)
		$(element).remove();
	element = $('<div style="background-color: red; position: absolute; z-index: 1;">move</div>');
	$(element).width($(elementOrig).width());
	$(element).height($(elementOrig).height());
	$(element).css('top', $(elementOrig).position().top);
	$(element).css('left', $(elementOrig).position().left);
	$('.field').append(element);	
	
	$(element)
	  .transition({ y: ($(elementDest).position().top - $(elementOrig).position().top)})
	  .transition({ x: $(elementDest).position().left - $(elementOrig).position().left});
		
}