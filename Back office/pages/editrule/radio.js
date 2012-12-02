$(document).ready(function () {
    
    delete_act = function() {
        $(this).parent().remove();
    }
    
    $('.delete').click(delete_act);
    
    $('#addfield').click(function() {
        var base_node = $('.field').last();
        var new_node = base_node.clone();
        $(base_node).find('.delete').click(delete_act);
        base_node.after(new_node);
    });
});
