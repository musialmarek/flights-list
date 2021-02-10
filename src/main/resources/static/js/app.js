$(function () {
    const $firstOptionButton = $('.first-option-button');
    const $secondOptionButton = $('.second-option-button');
    const $startMethod = $('.start-method');
    const $towSection = $('.tow-section');
    const $atto = $('#ATTO');

    $startMethod.change(function () {
        if ($atto.is(':selected')) {
            $towSection.show();
        } else {
            $towSection.hide();
        }
    });

    $firstOptionButton.click(function () {
        $(this).parent('.first-option').hide();
        $(this).parent('.first-option').siblings('.second-option').show();
        $(this).siblings('select').val('');
    });

    $secondOptionButton.click(function () {
        $(this).parent('.second-option').hide();
        $(this).parent('.second-option').siblings('.first-option').show();

    });

    const $confirmingPassword = $('#confirming-password');
    const $password = $('#password');
    $password.val('');
    const $wrongConfirming = $("<p class='text-danger' id='wrong-confirming'>Hasła nie są jednakowe!</p>")
    $confirmingPassword.keyup(function () {
        if ($confirmingPassword.val() !== $password.val()) {
            $confirmingPassword.before($wrongConfirming)
        } else {
            $wrongConfirming.remove();
        }
    });
});