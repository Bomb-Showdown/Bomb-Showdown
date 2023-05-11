$(document).ready(function () {
    $('#lobby-join-btn').on('click', function () {
        $('.lobby-join').removeClass('hidden');
        $('.lobby-join-code').removeClass('hidden');
        $('.lobby-create').addClass('hidden');
    });
    $('#lobby-join-code-btn').on('click', function () {
        $('.lobby-choise').addClass('hidden');
        $('.lobby-join-code').addClass('hidden');
        $('.lobby-join-name').removeClass('hidden');
    });

    $('#lobby-create-btn').on('click', function () {
        websocket.connection(generateCode());
        $('.lobby-choise').addClass('hidden');
        $('.lobby-create-name').removeClass('hidden');
        $('.lobby-create').removeClass('hidden');
    });
    $('#lobby-create-code-btn').on('click', function () {
        $('.lobby-choise').addClass('hidden');
        $('.lobby-create-code').addClass('hidden');
        
    });

    // $('.lobby-container').addClass('animate__animated animate__fadeInUp');
});