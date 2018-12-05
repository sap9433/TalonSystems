var stompClient = null;
var clientid = prompt("Please enter client id", "client1");

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/backToClient', function (transcript) {
            showTranscript(transcript.body);
        });
    });
}

$( document ).ready(connect);

function showTranscript(message) {
    $("#queryresult").append("<tr><td>" + message + "</td></tr>");
}
