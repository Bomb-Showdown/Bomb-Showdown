/**
 * Bomb Showdown visual scripts
 * @author Andrés Ariza
 */

var me = "Andrés";
var syllable = ""
var roomCode = "";
                
var players = [/*player1, player2, player3, player4*/];
var currentPlayer = 0;

var arrowAngle;
var arrow;
var degreeAngle;

var liveIcon = '💗';
var deadIcon = '💀';
var sfx = {'correct': new Audio('./../sounds/correct.wav'),
           'incorrect': new Audio('./../sounds/Error.wav'),
            'explosion': new Audio('./../sounds/boom.wav'),
            'start': new Audio('./../sounds/start.wav'),
            'end': new Audio('./../sounds/end.wav')};

var entranceAnimations = ['fadeInDown', 'jackInTheBox', 'rollIn', 'zoomIn'];

sfx.correct.volume = 0.3;
sfx.incorrect.volume = 0.08;
sfx.explosion.volume = 0.5;
sfx.start.volume = 0.65;
sfx.end.volume = 0.7;


/**
 * Inicia el juego
 * @param {*} info objeto con información sobre el estado actual del juego
 */
var start = function (info) {
    $('.bomb').addClass('scale-up-center');
    syllable = info.syllable != "" ? info.syllable : syllable;
    $('.arrow').removeClass('hidden');
    $('.arrow').css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + (arrowAngle) + 'deg)'
    });
    activateInput();


    $('.start-btn').addClass('hidden');
    $('.text-container').removeClass('hidden');

    sfx.start.play();
    updateBombState();
    updatePlayersState();
    updateInputState();
}


/**
 * Actualiza la vista para que muestre los elementos del juego
 * @param {Array[Object]} newPlayers jugadores de la sala
 */
var addPlayers = function(newPlayers) {
    $('.room-code').text(roomCode);
    $('.room-info').removeClass('hidden');


    players = newPlayers;
    
    $('.board-container').empty();
    $('.board-container').append(
        "<div class=\"arrow\"></div>\n" +
        "<div class=\"boom hidden\"></div>\n" +
    "        <div class=\"bomb\">\n" +
    "          <p class=\"syllable\"></p>\n" +
    "        </div>");

    var nPlayers = players.length;

    var angle = (2 * Math.PI / nPlayers); // ángulo entre cada jugador
    var r = 215;
    var playerDim = 74;

    var centerX = $(".board-container").width() / 2;
    var centerY = $(".board-container").width() / 2;

    for (let i = 0; i < nPlayers; i++) {
        let entrance = entranceAnimations[Math.floor(Math.random()*entranceAnimations.length)];
        let x = centerX + (r * Math.cos(i * angle)); // x = x_0 + r cos(t)
        let y = centerY + (r * Math.sin(i * angle)); // y = y_0 + r sen(t)
        let player = $("<div>").addClass("player animate__animated animate__"+entrance).css({
            left: (x - (playerDim/2)) + "px",
            top: (y - (playerDim/2)) + "px"
        });
        player.append('<p class="player-name">' + players[i].name + '</p>');
        player.append('<p class="player-lives">'+ `${liveIcon.repeat(players[i].lives)}` +'</p>');
        player.append('<p class="player-text" id="player' + i + '"></p>');
        $(".board-container").append(player);
        animateCSS(player, entrance);
        players[i].div = player;
        //players[i].div.removeClass("animate__animated "+entrance);
    }

    arrow = $('.arrow');

    arrowAngle = (Math.atan2(parseInt(players[0].div.css('left').replace(".px", "")), parseInt(players[0].div.css('top').replace(".px", ""))))-90;
    degreeAngle = angle*(180/Math.PI);

    

    //updateBombState();
    updatePlayersState();
    updateInputState();
    
    console.log( players[0].name == me);
    if (players[0].name == me) {
        $('.start-btn').removeClass('hidden');
        //$('.start-btn').addClass('animate__animated animate__fadeIn');
    }
}


/**
 * Hace rotar la flecha central hacia el siguiente jugador
 * @param {*} info objeto con información sobre el estado actual del juego
 */
var rotateArrow = function(info, who = currentPlayer) {
    //animateCSS(players[currentPlayer].div, 'headShake');
    if (info.correct !== "boom") {
        animateCSS(players[who].div, 'rotateCenter');
        sfx.correct.play();
    } else if (info.correct === "boom") {
        animateCSS(players[who].div, 'headShake');
    }
    
    
    arrowAngle += degreeAngle + degreeAngle*nextAlive(info.player);
    console.log('currentPlayer :>> ', currentPlayer);

    arrow.css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + arrowAngle + 'deg)'
    });

    console.log('info.syllable :>> ', info.syllable);
    syllable = info.syllable != "" ? info.syllable : syllable;

    
    updateLives(info.lives);
    updateBombState();
    updatePlayersState();
    updateInputState();
}


/**
 * Calcula el número de saltos desde el jugador actual hasta el siguiente jugador vivo
 * @returns número de saltos
 */
var nextAlive = function(nextPlayer) {
    let scape = false;
    let i = (currentPlayer + 1) % players.length;
    let jumps = 0;
    players[currentPlayer].div.css("outline", "0px solid white");
    players[currentPlayer].div.css("box-shadow", "0px 0px 10px 1px rgba(0, 0, 0 , .20)");
    while (!scape) {
        if (players[i].name != nextPlayer) {
            jumps++;
        } else {
            scape = true;
        }
        i = (i + 1) % players.length;
        currentPlayer = (currentPlayer + 1) % players.length;
    }
    players[currentPlayer].div.css("outline", "2px solid white");
    players[currentPlayer].div.css("box-shadow", "0px 0px 10px 3px rgba(0, 0, 0 , .50)");
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


var refreshText = function (info) {
    let text = info.word;
    let who = info.player;
    console.log('info :>> ', info);
    $('#player'+who).text(text.replace('\"', '').replace('\"', ''));
    let ocurrences = text.toUpperCase().indexOf(syllable.toUpperCase());
    console.log('ocurrences :>> ', ocurrences);
    if (ocurrences !== -1) {
        $('#player'+who).html((text.substring(0, ocurrences)+'<b>' + syllable.toUpperCase() + '</b>' + text.substring(ocurrences+syllable.length)
        ).replace('"', '').replace('"', ''));
    }
}


var updateLives = function(lives) {
    for (let i = 0; i < lives.length; i++) {
        players[i].lives = lives[i];
    }
    console.log('players :>> ', players);
}


/**
 * Actualiza la visualización de la bomba y la sílaba actual
 */
var updateBombState = function() {
    console.log('syllable :>> ', syllable);
    $('.syllable').text(syllable.toUpperCase());
}


/**
 * Anima al jugador en caso de introducir una palabra erronea
 */
var wrongAnswer = function (who = currentPlayer) {
    sfx.incorrect.play();
    animateCSS(players[who].div, 'headShake');
    $('.input-text').val('');
}


/**
 * 
 */
var startBonus = function (info) {
    $('.arrow').addClass('bonus-arrow');
    $('body').addClass('bonus-body');
    $('.input-text').css('display', 'block');
    $('.input-text').val('');
    $('.current-player').css('display', 'none');
    syllable = info.syllable != "" ? info.syllable : syllable;
    updateBombState();
    var input = document.querySelector(".input-text");
    input.removeEventListener('keypress', pressEnter);
    input.addEventListener('keypress', pressEnterBonus);
}


/**
 * 
 */
var endBonus = function (info) {
    $('.arrow').removeClass('bonus-arrow');
    $('body').removeClass('bonus-body');
    var input = document.querySelector(".input-text");
    input.removeEventListener('keypress', pressEnterBonus);
    input.addEventListener('keypress', pressEnter);
    rotateArrow(info, info.candidate);
}

/**
 * 
 */
var boom = function () {
    sfx.explosion.play();
    $('.boom').removeClass('hidden');
    setTimeout(function() {
        $('.boom').addClass('hidden');
      }, 1000);
}

/**
 * 
 */
var endGame = function () {
    $('.input-text').css('display', 'none');
    $('.current-player').text('Gana el jugador ' + players[currentPlayer].name + '!');
    $('.current-player').css('display', 'block');
    $('.bomb').addClass('hidden');
    $('.arrow').addClass('hidden');
    sfx.end.play();
}

var generateCode = function () {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

    for (let i = 0; i < 5; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
    }

    return result;
}


/**
 * Anima un elemento dado el nombre de la animación (Adaptado de: https://animate.style/#javascript)
 * @param {Object} div elemento a animar
 * @param {String} animation nombre de la animación
 * @param {String} prefix prefijo
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
      console.log('removed :>> ', `${prefix}animated`, animationName);
      resolve('Animation ended');
    }

    div.one('animationend', handleAnimationEnd);
  });

function activateInput() {
    var input = document.querySelector(".input-text");
    input.addEventListener("keypress", pressEnter);
}

function pressEnter(event) {
    // If the user presses the "Enter" key on the keyboard
    if (event.key === "Enter") {
      // Cancel the default action, if needed
      event.preventDefault();
      websocket.checkWord($(".input-text").val());
    }
}


function pressEnterBonus(event) {
    // If the user presses the "Enter" key on the keyboard
    if (event.key === "Enter") {
      // Cancel the default action, if needed
      event.preventDefault();
      websocket.checkBonusWord($(".input-text").val());
    }
}


function upperCaseF(a){
    setTimeout(function(){
        a.value = a.value.toUpperCase();
    }, 1);
}
