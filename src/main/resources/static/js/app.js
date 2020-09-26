$(function () {
    const $firstOptionButton = $('.first-option-button');
    const $secondOptionButton = $('.second-option-button');

    $firstOptionButton.click(function () {
        $(this).parent('.first-option').hide();
        $(this).parent('.first-option').siblings('.second-option').show();
        $(this).siblings('select').val('');
    });

    $secondOptionButton.click(function () {
        $(this).parent('.second-option').hide();
        $(this).parent('.second-option').siblings('.first-option').show();

    })
});