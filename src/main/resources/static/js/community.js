/**
 * 提交回复
 */
function postComment() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    comment2target(questionId,1,commentContent);
}

function postSubComment(e){
    var id = e.getAttribute("data-id");
    var content = $('#input-'+id).val();
    comment2target(id, 2, content);
}

function comment2target(targetId, type, content){
    if (!content){
        alert('可是，可是你不能发送不存在的东西');
        return
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=6a312e03edabef8a3323&redirect_uri=http://localhost:8080/callback&scope=user&state=666")
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType: "json",
        contentType: "application/json"
    });
}

/**
 * 展开二级评论
 */
function collapseComments(e){
    var id = e.getAttribute("data-id");
    var comment = $('#comment-'+id);
    if (comment.hasClass("in")){
        //折叠
        comment.removeClass("in");
        e.classList.remove("icon-active");
    }else {
        //展开
        $.getJSON( "/comment/"+id, function( data ) {  //data即为请求获取到的值
            var commentBody = $("#comment-body-"+id);
            // var items = [];
            // $.each(data.data, function (comment) {
            //     var c = $("<div/>", {
            //         "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
            //         html: comment.content
            //     });
            //     items.push(c);
            // });
            //
            // commentBody.append($("<div/>", {
            //     "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
            //     "id": "comment-" + id,
            //     html: items.join("")
            // }));
            comment.addClass("in");
            e.classList.add("icon-active");
        });
    }
}