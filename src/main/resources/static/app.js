var stompClient = null;
$(document).ready(function() {
	connect();
});
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
        stompClient.subscribe('/topic/game', function (message) {
        	drawGameBoard(JSON.parse(message.body));
        });
    });
}

function start() {
	var playerName =document.getElementById('playerName').value;
    stompClient.send("/app/start", {}, playerName);
}

function connectToGame() {
	var playerName =document.getElementById('playerName').value;
	var gameId =document.getElementById('gameId').value;
	var param = playerName+","+gameId; 
    stompClient.send("/app/connectToGame", {}, param);
}

function move(pileId) {
	var gameId =document.getElementById('gameId').value;
	var param = pileId+","+gameId; 
    stompClient.send("/app/move", {},param);
}

function drawGameBoard(message) {
      var inputGameId = document.getElementById("gameId");
      if (inputGameId.value != null ){
      inputGameId.value=message.gameId;
     }
    $("#gameId").text(inputGameId.value); 
    $("#gameStatus").text(message.status);
    $("#winner").text(message.winner);
    $("#player1").text(message.player1.name);
    $("#player2").text(message.player2.name);
    pits = message.pits;
    for (i=0; i<14; i++) {
    	$("#"+i).text(pits[i].stonesCount);
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
    $( "#connectToGame" ).click(function() { connectToGame(); });
});