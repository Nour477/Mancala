var stompClient = null;
var messageGameId =null; 
var playerId =null;
$(document).ready(function() {
	connect();
});
function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/game', function (message) {
        var player2Dummy =JSON.parse(message.body).player2.dummy;
        if (player2Dummy)
        {
        	playerId=JSON.parse(message.body).player1.id; 
        }else if (!playerId)
        {
        	playerId=JSON.parse(message.body).player2.id; 
        }
    	var gameId =JSON.parse(message.body).gameId;
    	if (!messageGameId ||  gameId==messageGameId )
    	{messageGameId = gameId;
    	 drawGameBoard(JSON.parse(message.body));
    	}
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
	var playerName =document.getElementById('playerName').value;
	var param = pileId+","+gameId +","+playerId; 
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