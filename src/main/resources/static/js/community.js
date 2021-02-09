function postComment() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": questionId,
            "content": commentContent,
            "type": 1
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