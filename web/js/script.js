var slider = document.getElementById("myRange");
var output = document.getElementById("sValue");
output.innerHTML = slider.value;

// Update the current slider value when moved
slider.oninput = function() {
  output.innerHTML = this.value;
}
//zingChart
var myConfig = {
"graphset":[
{
"type":"wordcloud",
"options":{
  "style":{
    "tooltip":{
      visible: true,
      text: '%text: %hits'
    },
	fontFamily: 'Roboto'
  },
"words":[
{
"text":"Press",
"count":"400"
},
{
"text":"USA",
"count":"300"
},
{
"text":"Waffles",
"count":"200"
},
{
"text":"hello",
"count":"150"
},
{
"text":"morning",
"count":"100"
},
{
"text":"fox",
"count":"20"
}
]
}
}
]
};
 
zingchart.render({ 
	id: 'myChart', 
	data: myConfig, 
	height: '100%', 
	width: '100%' 
});