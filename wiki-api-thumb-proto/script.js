console.log("js linked"); 

 $(document).ready(function() {

 $('#search').click(getThumbnail);
    
});



function getThumbnail() {
  var title = $("#title").val();
  var url = 'https://en.wikipedia.org/w/api.php?action=query&titles=' + title + '&prop=pageimages&format=json&pithumbsize=300';
// jQuery preflight request
$.ajax({
    type: "GET",
    headers: {"Access-Control-Allow-Origin": "*"},
    crossDomain: true,
    dataType: 'jsonp',
    url: url
})
.done(showThumbnail)
.fail(function(request, reason) {
  console.log("failed");
  console.log(reason);
});
}

function showThumbnail(response) {
    console.log(response);

    var src = response.query.pages;
    var pageId = String(Object.keys(src));

    var string = JSON.stringify(response);
    var newString = string.replace(pageId, "id");
    var json = JSON.parse(newString);

    var imgSrc = json.query.pages.id.thumbnail.source;

    console.log(imgSrc);

    $("#thumbnail").attr("src",imgSrc)

}