var clientid = prompt("Please enter client id", "client1");
var retriveapi = '/api/retrievedata?clientid='+clientid;
var searchkey = null;

window.setTimeout(function selectElement() {
     let element = document.getElementById("retrieveveid");
     element.value = this.clientid;
 }, 500);

function showResult(message) {
    $("#queryresult").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#savedata" ).click(function() { submitdata(); });
    $( "#retrievedata" ).click(function() { retrievedata(); });
});

function showTranscript(message) {
    $("#queryresult").html("<tr><td>" + message + "</td></tr>");
}

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

    this.source = null;

    this.start = function () {

        this.source = new EventSource(retriveapi+"&key="+searchkey);

        //before just message
        this.source.addEventListener('/topic/backToClient/'+clientid, function (event) {
           showResult(event.data);
        });

        this.source.onmessage = function (message) {
                   showResult(message);
         };

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

