var websocket = (function() {

    var stompClient =null;
    var room = null;

    function connectAndSubscribe(code) {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.info('Connecting to WS...');
        var socket = new SockJS('/bs');
        stompClient = Stomp.over(socket);
        room = code;
        stompClient.connect({}, function (frame) {
            //setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/code.'+room, function (eventbody) {
                //alert(eventbody.body);
                console.log(JSON.parse(eventbody.body).content);
                $("#res").append("<tr><td>" + JSON.parse(eventbody.body).content + "</td></tr>");
            });
        });
    };

    return {
        send: function() {
            stompClient.send("/app/code."+room, {}, JSON.stringify({'room':room}));
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