
var me = "Andrés";
var syllable = "AUD"

$(document).ready(function() {
    $('#id').text('me: ' + me + ' - syllable: ' + syllable);
});

var player1 = {"name": "Andrés",
                "lives": 3,
                "div": null};
var player2 = {"name": "Juanpa",
                "lives": 2};
var player3 = {"name": "Julián",
                "lives": 3};
var player4 = {"name": "Vladimir",
                "lives": 0};
var player5 = {"name": "Bob",
                "lives": 3};
var player6 = {"name": "Hector",
                "lives": 0};
var player7 = {"name": "Vicky",
                "lives": 4};
var player8 = {"name": "Manolo Johnson",
                "lives": 0};
                
var players = [];
var currentPlayer = 0;

var arrowAngle;
var arrow;
var degreeAngle;

var liveIcon = '💗';
var deadIcon = '💀';
var correctSfx = new Audio('./../sounds/correct.wav');

$(document).ready(function () {
    //addPlayer("Manolo");
});


var addPlayers = function(newPlayers) {

    players = newPlayers;
    
    $('.board-container').empty();
    $('.board-container').append("<div class=\"arrow\"></div>\n" +
    "        <div class=\"bomb\">\n" +
    "          <p class=\"syllable\">AUD</p>\n" +
    "        </div>");

    var nPlayers = players.length;

    var angle = (2 * Math.PI / nPlayers); // ángulo entre cada jugador
    var r = 175;
    var playerDim = 74;

    var centerX = $(".board-container").width() / 2;
    var centerY = $(".board-container").width() / 2;

    for (let i = 0; i < nPlayers; i++) {
        let x = centerX + (r * Math.cos(i * angle)); // x = x_0 + r cos(t)
        let y = centerY + (r * Math.sin(i * angle)); // y = y_0 + r sen(t)
        let player = $("<div>")./*text(players[i].name).*/addClass("player").css({
            left: (x - (playerDim/2)) + "px",
            top: (y - (playerDim/2)) + "px"
        });
        player.append('<p class="player-name">' + players[i].name + '</p>');
        player.append('<p class="player-lives">'+ `${liveIcon.repeat(players[i].lives)}` +'</p>');
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
    updateInputState();
}

/**
 * Hace rotar la flecha central hacia el siguiente jugador
 */
var rotateArrow = function(info) {
    //animateCSS(players[currentPlayer].div, 'headShake');
    animateCSS(players[currentPlayer].div, 'rotateCenter');
    
    arrowAngle += degreeAngle + degreeAngle*nextAlive();
    console.log('currentPlayer :>> ', currentPlayer);

    arrow.css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + arrowAngle + 'deg)'
    });

    syllable = info.syllable;
    //correctSfx.play();

    updateBombState();
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
    players[currentPlayer].div.css("box-shadow", "0px 0px 10px 1px rgba(0, 0, 0 , .20)");
    // players[currentPlayer].div.removeClass("player-selected");
    while (!scape) {
        if (players[i].lives <= 0) {
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
            players[i].div.find('p').eq(0).addClass('dead');
            players[i].div.find('p').eq(1).text(deadIcon);
        } else if (players[i].lives >= 0) {
            players[i].div.find('p').eq(1).text(`${liveIcon.repeat(players[i].lives)}`);
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

var updateBombState = function() {
    console.log('syllable :>> ', syllable);
    $('.syllable').text(syllable.toUpperCase());
}

function upperCaseF(a){
    setTimeout(function(){
        a.value = a.value.toUpperCase();
    }, 1);
}

var wrongAnswer = function () {
    animateCSS(players[currentPlayer].div, 'headShake');
}

/**
 * Anima un elemento dado el nombre de la animación (Adaptado de: https://animate.style/#javascript)
 * @param {*} div elemento a animar
 * @param {*} animation nombre de la animación
 * @param {*} prefix prefijo
 * @returns 
 */
const animateCSS = (div, animation, prefix = 'animate__') =>
  // We create a Promise and return it
  new Promise((resolve, reject) => {
    const animationName = `${prefix}${animation}`;

    div.addClass(`${prefix}animated` + ' ' + animationName);

    // When the animation ends, we clean the classes and resolve the Promise
    function handleAnimationEnd(event) {
      event.stopPropagation();
      div.removeClass(`${prefix}animated` + ' ' + animationName);
      resolve('Animation ended');
    }

    div.one('animationend', handleAnimationEnd);
  });

var input = document.querySelector(".input-text");

// Execute a function when the user presses a key on the keyboard
input.addEventListener("keypress", function(event) {
  // If the user presses the "Enter" key on the keyboard
  if (event.key === "Enter") {
    // Cancel the default action, if needed
    event.preventDefault();
    websocket.checkWord($(".input-text").val(), rotateArrow);
  }
});