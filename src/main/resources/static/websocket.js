var stompClient = null;
var clientid = prompt("Please enter client id", "client1");

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/backToClient/'+clientid, function (transcript) {
            showResult(transcript.body);
        });
    });
}

function showResult(message) {
    $("#queryresult").append("<tr><td>" + message + "</td></tr>");
}

$( document ).ready(connect);

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#savedata" ).click(function() { submitdata(); });
    $( "#retrievedata" ).click(function() { retrievedata(); });
});

function showTranscript(message) {
    $("#queryresult").append("<tr><td>" + message + "</td></tr>");
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
    $.ajax({
        url:'/api/retrievedata',
        type:'post',
        data:$('#retrievedataform').serialize(),
        success:function(){
            
        }
    });
}