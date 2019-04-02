var slider = document.getElementById("myRange");
var output = document.getElementById("sValue");
output.innerHTML = slider.value;

// Update the current slider value when moved
slider.oninput = function() {
  output.innerHTML = this.value;
}