var clientid = prompt("Please enter client id", "client1");
var retriveapi = '/api/retrievedata?clientid='+clientid;
var searchkey = null;

window.setTimeout(function selectElement() {
     let element = document.getElementById("retrieveveid");
     element.value = this.clientid;
 }, 500);

function showResult(message) {
    $("#resultspace").text(message);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#savedata" ).click(function() { submitdata(); });
    $("#retrievedata" ).click(function() { retrievedata(); });
});


function submitdata (){
    $.ajax({
        url:'/api/savedata',
        type:'post',
        data:$('#savedataform').serialize(),
        success:function(){
            alert("Saved");
        }
    });

}

function retrievedata (){
    searchkey = $('#searchkey').val();
    var rstream = new responseStream();
    rstream.start();
}

//### New ###

function responseStream () {

    try{
        this.source.stop();
    }catch(e){}

    this.start = function () {

        this.source = new EventSource(retriveapi+"&key="+searchkey);

        //before just message
        this.source.addEventListener('/topic/backToClient/'+clientid+searchkey, function (event) {
           showResult(event.data);
        });

        this.source.onerror = function () {
            this.close();
        };
    };

    this.stop = function() {
        this.source.close();
    }

}

window.onbeforeunload = function() {
        rstream.stop();
    }

