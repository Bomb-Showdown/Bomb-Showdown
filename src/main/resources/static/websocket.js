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

    function connectAndSubscribe(code) {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.info('Connecting to WS...');
        var socket = new SockJS('/bs');
        stompClient = Stomp.over(socket);
        room = code;
        console.log('---ME CONECTÉ AL ROOM---');
        console.log(room);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            var letter = document.getElementById("test");
            stompClient.subscribe('/topic/room.'+room, function (eventbody) {
                console.log(letter);
                letter.textContent = eventbody.body;
            });
        });
    };

    return {
        init: function() {
            var log = document.getElementById("result");
            var input = document.querySelector("#sample");
            if (input) {
                console.log('Entra');
                input.addEventListener("keyup",  function(event){
                    const key = event.key;
                    if (/^[a-zA-ZñÑ]$/.test(key)) {
                        log.textContent = `${event.key}`;
                        //word += `${event.key}`;
                        word = $("#sample").val();
                        console.log("+ /// " + word);
                    } else if (key === 'Backspace') {
                        //word = word.slice(0, -1);
                        word = $("#sample").val();
                        console.log("- /// " + word);                   
                    } else {
                        event.preventDefault();
                        console.log("nada /// " + word);  
                    }
                    stompClient.send("/app/room."+room, {}, JSON.stringify(word));
                });
            }
            console.log('No Entra');
        },

        send: function(code) {
            //stompClient.send("/app/room."+room, {}, JSON.stringify({'room':code}));
            stompClient.send("/app/room."+room, {}, JSON.stringify(word));
        },

        wordFunction: function(word) {
            input.addEventListener("keypress", logKey);
        },
                
        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        },

        connection : function(code) {
            connectAndSubscribe(code);
        }
    };

})();