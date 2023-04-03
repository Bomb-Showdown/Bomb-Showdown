// const button = document.getElementById('button');
// const objeto = document.getElementById('object');
// let angle = 0;

// button.addEventListener('click', () => {
//     objeto.classList.add('rotate');
//     angle += 120;
//     objeto.style.transform = `rotate(${angle}deg)`;
// });

// objeto.addEventListener('transitionend', () => {
//     objeto.classList.remove('rotate');
// });

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
var players = [player1, player2, player3, player4, player5, player6];
var currentPlayer = 0;

var arrowAngle;
var arrow;
var degreeAngle;

$(document).ready(function () {
    
    var nPlayers = players.length; 

    var angle = (2 * Math.PI / nPlayers); // ángulo entre cada jugador
    var r = 120;
    var playerDim = 50;
    
    var centerX = $(".game-container").width() / 2;
    var centerY = $(".game-container").width() / 2;

    for (let i = 0; i < nPlayers; i++) {
        let x = centerX + (r * Math.cos(i * angle)); // x = x_0 + r cos(t)
        let y = centerY + (r * Math.sin(i * angle)); // y = y_0 + r sen(t)
        let player = $("<div>").text(players[i].name).addClass("player").css({
            left: (x - (playerDim/2)) + "px",
            top: (y - (playerDim/2)) + "px"
        });
        $(".game-container").append(player);
        players[i].div = player;
    }

    var button = $('#button');
    arrow = $('.arrow');

    arrowAngle = (Math.atan2(parseInt(players[0].div.css('left').replace(".px", "")), parseInt(players[0].div.css('top').replace(".px", ""))))-90;
    degreeAngle = angle*(180/Math.PI);

    arrow.css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + (arrowAngle) + 'deg)'
    });

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
}


/**
 * Calcula el número de saltos desde el jugador actual hasta el siguiente jugador vivo
 * @returns número de saltos
 */
var nextAlive = function() {
    let scape = false;
    let i = currentPlayer+1;
    let jumps = 0;
    while (!scape) {
        if (players[i].lives == 0) {
            jumps++;
        } else {
            scape = true;
        }
        i = (i + 1) % players.length;
        currentPlayer = (currentPlayer + 1) % players.length;
    }
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