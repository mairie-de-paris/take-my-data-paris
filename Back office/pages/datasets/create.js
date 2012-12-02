$(document).ready(function () {
    $('#addfield').click(function() {
        var base_node = $('.field').last();
        var new_node = base_node.clone();
        base_node.after(new_node);
    });
});
