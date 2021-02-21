$(document).ready(function(){
    $("#addTag").mouseleave(function(){
        hideSelectTag();
    });
});

function showSelectTag(){
    var addTag = $("#addTag");
    var tagDiv = $("#selectTag");
    tagDiv.addClass("show");
}

function hideSelectTag(){
    var tagDiv = $("#selectTag");
    tagDiv.removeClass("show");
}

function selectTag(e) {
    var inputTag = $("#tag");
    var previous = inputTag.val().replace('，',',');
    var tag = e.textContent.trim();
    if (previous){
        var tags = previous.split(',');
        if (!tags.includes(tag)){
            inputTag.val(previous+','+tag);
        }
    }else {
        inputTag.val(tag)
    }
}