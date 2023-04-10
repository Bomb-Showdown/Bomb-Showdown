var websocket = (function() {

    var stompClient =null;
    var room = null;
    var player1 = {"name": "Andrés",
                "lives": 3};
    var player2 = {"name": "Juanpa",
                "lives": 3};
    var player3 = {"name": "Julián",
                "lives": 3};
    var player4 = {"name": "Vladimir",
                "lives": 0};
    var player5 = {"name": "Bob",
                "lives": 3};
    var players = [player1, player2, player3, player4, player5];
    var word = "";
    var syllable = null;
    var currentPlayer = null;

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
                letter.textContent = eventbody.body;
                refreshText(eventbody.body);
            });
            
            stompClient.subscribe('/rooms/party/'+room, function (eventbody) {
                let msg = JSON.parse(eventbody.body);
                if (msg.correct === "") {
                    console.log("start");
                    start(msg);
                } else if (msg.correct) {
                    console.log('correct');
                    rotateArrow(msg);
                } else if (!msg.correct) {
                    console.log('incorrect');
                    wrongAnswer();
                }
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
                        console.log("Mirar validez de la palabra");
                    }
                    stompClient.send("/app/rooms/text/"+room, {}, JSON.stringify(word));
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
        },

        checkWord : function(word) {
            $.ajax({
                type: "POST",
                url: "/games/rooms/"+room+"/word",
                data: word,
                contentType: "text/html"
            });
        },

        startGame: function() {
            $.ajax({
                type: "POST",
                url: "/games/rooms/"+room+"/start"
            });
        }
    };

})();