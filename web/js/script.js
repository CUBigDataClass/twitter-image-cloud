var slider = document.getElementById("myRange");
var output = document.getElementById("sValue");
output.innerHTML = slider.value;

var today = new Date();
today.setDate(today.getDate());
//options for localeToString
var options = { month: 'numeric', day: 'numeric', year: 'numeric' };
date.innerHTML = today.toLocaleString('en-US', options);
// Update the current slider value when moved
slider.oninput = function () {
    output.innerHTML = this.value;
    var otherDay = new Date();
    otherDay.setDate(today.getDate() - this.value);
    date.innerHTML = (otherDay.toLocaleString('en-US', options));
};


var words = [];
var words = [
    {"text":"Press","count":"400"},
    {"text":"word","count":"200"},
    {"text":"number","count":"200"},
    {"text":"hello","count":"500"}
];
//zingChart
var myConfig = {
    "graphset": [ 
    {
        "type": "wordcloud",
  
         "options": {
          "style": {
            "tooltip": {
              visible: true,
              text: '%text: %hits'
            },
            fontFamily: 'Roboto'
          },
            words
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