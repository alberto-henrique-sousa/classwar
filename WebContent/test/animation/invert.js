var inverted = false;

function invertY() {
	var value = inverted ? "0" : "180";
	inverted = !inverted;
	$('.battleField').transition({
		  perspective: '100px',
		  rotateY: value + 'deg'
		});
	$('.col').transition({
		  perspective: '100px',
		  rotateY: value + 'deg'
		});
}