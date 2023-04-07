
var me = "Andrés";
var syllable = "AUD"

$(document).ready(function() {
    $('#id').text('me: ' + me + ' - syllable: ' + syllable);
});

var player1 = {"name": "Andrés",
                "lives": 3,
                "div": null};
var player2 = {"name": "Juanpa",
                "lives": 3};
var player3 = {"name": "Julián",
                "lives": 3};
var player4 = {"name": "Vladimir",
                "lives": 0};
var player5 = {"name": "Bob",
                "lives": 3};
var player6 = {"name": "Hector",
                "lives": 0};
var players = [player1, player2, player3, player4, player5];
var currentPlayer = 0;

var arrowAngle;
var arrow;
var degreeAngle;

$(document).ready(function () {
    
    var nPlayers = players.length; 

    var angle = (2 * Math.PI / nPlayers); // ángulo entre cada jugador
    var r = 175;
    var playerDim = 74;
    
    var centerX = $(".board-container").width() / 2;
    var centerY = $(".board-container").width() / 2;

    for (let i = 0; i < nPlayers; i++) {
        let x = centerX + (r * Math.cos(i * angle)); // x = x_0 + r cos(t)
        let y = centerY + (r * Math.sin(i * angle)); // y = y_0 + r sen(t)
        let player = $("<div>").text(players[i].name).addClass("player").css({
            left: (x - (playerDim/2)) + "px",
            top: (y - (playerDim/2)) + "px"
        });
        $(".board-container").append(player);
        players[i].div = player;
    }

    var button = $('#button');
    arrow = $('.arrow');

    arrowAngle = (Math.atan2(parseInt(players[0].div.css('left').replace(".px", "")), parseInt(players[0].div.css('top').replace(".px", ""))))-90;
    degreeAngle = angle*(180/Math.PI);

    arrow.css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + (arrowAngle) + 'deg)'
    });

    updatePlayersState();

    // NEXT PLAYER
    button.on('click', rotateArrow);
});

/**
 * Hace rotar la flecha central hacia el siguiente jugador
 */
var rotateArrow = function() {
    arrowAngle += degreeAngle + degreeAngle*nextAlive();

    console.log('currentPlayer :>> ', currentPlayer);

    arrow.css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + arrowAngle + 'deg)'
    });

    updatePlayersState();
    updateInputState();
}


/**
 * Calcula el número de saltos desde el jugador actual hasta el siguiente jugador vivo
 * @returns número de saltos
 */
var nextAlive = function() {
    let scape = false;
    let i = (currentPlayer + 1) % players.length;
    let jumps = 0;
    players[currentPlayer].div.css("outline", "0px solid white");
    players[currentPlayer].div.css("box-shadow", "0px 0px 10px 3px rgba(0, 0, 0 , .20)");
    // players[currentPlayer].div.removeClass("player-selected");
    while (!scape) {
        if (players[i].lives == 0) {
            jumps++;
        } else {
            scape = true;
        }
        i = (i + 1) % players.length;
        currentPlayer = (currentPlayer + 1) % players.length;
    }
    players[currentPlayer].div.css("outline", "2px solid white");
    players[currentPlayer].div.css("box-shadow", "0px 0px 10px 3px rgba(0, 0, 0 , .50)");
    // players[currentPlayer].div.addClass("player-selected");
    return jumps;
}

/**
 * Actualiza la visualización de los jugadores según su estado
 */
var updatePlayersState = function () {
    for (let i = 0; i < players.length; i++) {
        if (players[i].lives == 0) {
            players[i].div.css({
                'background-color': 'red',
                color: 'black'
            });
        }
    }
}

/**
 * Actualiza la visualización del input de texto para el jugador
 */
var updateInputState = function() {
    if (players[currentPlayer].name != me) {
        $('.input-text').css('display', 'none');
        $('.current-player').text('Turno de ' + players[currentPlayer].name + '.');
        $('.current-player').css('display', 'block');
    } else {
        $('.input-text').css('display', 'block');
        $('.input-text').val('');
        $('.current-player').css('display', 'none');
    }
}

function upperCaseF(a){
    setTimeout(function(){
        a.value = a.value.toUpperCase();
    }, 1);
}

$(selector).val();