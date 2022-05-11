var stompClient = null;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        //TODO It should subscribe with game id for multiple games
        stompClient.subscribe('/topic/game', function (message) {
        	drawGameBoard(JSON.parse(message.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function start() {
    stompClient.send("/app/start", {}, {});
}

function move(pileId) {
    stompClient.send("/app/move", {}, pileId);
}

function drawGameBoard(message) {
    $("#gameStatus").text(message.gameStatus);
    $("#winner").text(message.winner);
    pits = message.pits;
    for (i=0; i<14; i++) {
    	$("#"+i).text(pits.numPebbles[i]);
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#start" ).click(function() { start(); });
});