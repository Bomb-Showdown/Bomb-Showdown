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


$(document).ready(function() {
    var button = $('#button');
    var object = $('#object');

    var angle = 0;

    button.on('click', function () {
        angle += 120;

        object.css({
            'transform': 'rotate(' + angle + 'deg)'
        });
    });
});

$(document).ready(function () {
    var nPlayers = 4; 

    var angle = (2 * Math.PI / nPlayers); // ángulo entre cada jugador
    var r = 120; 
    
    var centerX = $(".game-container").width() / 2;
    var centerY = $(".game-container").height() / 2;

    for (var i = 0; i < nPlayers; i++) {
        var x = centerX + (r * Math.cos(i * angle)); // x = x_0 + r cos(t)
        var y = centerY + (r * Math.sin(i * angle)); // y = y_0 + r sen(t)
        var player = $("<div>").addClass("player").css({
            left: (x-25) + "px",
            top: (y-25) + "px"
        });
        $(".game-container").append(player);
    }


    var button = $('#button');
    var arrow = $('.arrow');

    var angulo = -0;
    var degreeAngle = angle*(180/Math.PI);

    angulo += degreeAngle;

    arrow.css({
        'transform': 'translate(-50%, -50%)' + 'rotate(' + (angulo) + 'deg)'
    });

    button.on('click', function () {
        angulo += degreeAngle;

        arrow.css({
            'transform': 'translate(-50%, -50%)' + 'rotate(' + angulo + 'deg)'
        });
    });

});