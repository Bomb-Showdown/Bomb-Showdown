var websocket = (function() {

    var stompClient =null;
    var room = null;
    var word = "";

    function connectAndSubscribe(code) {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.info('Connecting to WS...');
        var socket = new SockJS('/bs');
        stompClient = Stomp.over(socket);
        room = code;
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            var letter = document.getElementById("word-ws");
            stompClient.subscribe('/rooms/waiting-room/'+room, function (eventbody) {
                console.log('eventbody.body :>> ', JSON.parse(eventbody.body));
                addPlayers(JSON.parse(eventbody.body));
            });

            stompClient.subscribe('/rooms/text/'+room, function (eventbody) {
                console.log('eventbody.body :>> ', JSON.parse(eventbody.body));
                letter.textContent = JSON.parse(eventbody.body);
                refreshText(JSON.parse(eventbody.body));
            });
            
            stompClient.subscribe('/rooms/party/'+room, function (eventbody) {
                let msg = JSON.parse(eventbody.body);
                if (msg.correct === "") {
                    console.log("start");
                    start(msg);
                } else if (msg.correct === "boom") {
                    players[msg.candidate].lives -= 1;
                    boom();
                    rotateArrow(msg);
                    if (msg.winner) {
                        endGame();
                    }
                } else if (msg.correct) {
                    console.log('correct');
                    rotateArrow(msg);
                } else if (!msg.correct) {
                    console.log('incorrect');
                    wrongAnswer();
                } 
            });

            stompClient.subscribe('/rooms/bonus/'+room, function (eventbody) {
                console.log('bonus :>> ', JSON.parse(eventbody.body));
                let msg = JSON.parse(eventbody.body);
                if (msg.begin) {
                    rotateArrow(msg);
                    startBonus(msg);
                } else if (msg.correct) {
                    players[msg.candidate].lives += msg.won;
                    endBonus(msg);
                } else if (!msg.correct) {
                    console.log('bonus incorrect');
                    wrongAnswer(msg.candidate);
                }
                // habilitar inputs a todos
            });
        });
    };

    return {
        init: function() {
            var input = document.querySelector("#input");
            if (input) {
                input.addEventListener("keyup", function(event){
                    const key = event.key;
                    // Mira si la entrada es una letra
                    word = $("#input").val();
                    if (key === 'Enter') {
                        // check word
                        //console.log("Mirar validez de la palabra");
                    }
                    stompClient.send("/app/rooms/text/"+room+'/player/'+me, {}, JSON.stringify(word));
                    
                });
            }
        },
                
        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        },

        connection : function(code) {
            $.ajax({
                type: "POST",
                url: "/games/rooms/"+code,
                data: "data",
                contentType: "text/html"
            });
            roomCode = code;
            connectAndSubscribe(code);
        },

        joinRoom : function(player) {
            $.ajax({
                type: "PUT",
                url: "/games/rooms/"+room+"/players",
                data: player,
                contentType: "text/html"
            });
            me = player;
            $('.lobby-container').addClass('hidden');
        },

        checkWord : function(word) {
            $.ajax({
                type: "POST",
                url: "/games/rooms/"+room+"/word",
                data: word,
                contentType: "text/html"
            });
        },

        checkBonusWord : function(word) {
            $.ajax({
                type: "POST",
                url: "/games/rooms/"+room+"/players/"+me+"/bonus",
                data: word,
                contentType: "text/html"
            });
        },

        startGame: function() {
            if (players.length > 1) {
                $.ajax({
                    type: "POST",
                    url: "/games/rooms/"+room+"/start"
                });
            } else {
                alert('Deben haber mínimo 2 jugadores.')
            }
            
        }
    };

})();