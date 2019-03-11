$(document).ready(function () {
    $("#newgame").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        newgame_submit();
    });

    $("#viewgame").click(function (event) {
        //stop click on link, we will do it manually.
        event.preventDefault();
        viewgames_click();
    });

    $("#gamelist_container").click(function (event) {
        event.preventDefault();
        join2game_click();
    });
});

function newgame_submit() {
    var search = {}
    search['userName'] = $("#username").val();
    search['size'] = $("#size").val();

    $("#btn-newgame").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/games/new",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 60000,
        success: function (data) {
            var json = "<h4>Ajax Response</h4><pre>"
                + JSON.stringify(data.message, null, 4) + "</pre>";
            $('#feedback').html(json);
            $('#accessToken').prop("value", data.accessToken)

            console.log("SUCCESS : ", data);
            $("#btn-newgame").prop("disabled", false);

            $(location).attr("href", "/games?accessToken="+data.accessToken)
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
                $('#feedback').html(json);
            console.log("ERROR : ", e);
            $("#btn-newgame").prop("disabled", false);
        }
    });
}

function viewgames_click() {
    $.ajax({
        type: "GET",
        //contentType: "application/json",
        url: "/games/list",
        dataType: 'json',
        cache: false,
        timeout: 60000,
        success: function (result) {
            jPut.games.data = JSON.stringify(result.games);
            $(".ready .state-name").text("ready");
            $(".playing .state-name").text("playing ..");
            $(".done .state-name").text("game over");

            console.log(result.games)
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            console.log(e.responseText)
            $('#feedback').html(json);
        }
    });
}

function join2game_click() {
    var search = {}
    search['userName'] = $("#username").val();
    var targ = event.target;
    search['gameToken'] = $(targ).closest('.data-state').find("input[name='token']").val();
    console.log("search : ", search)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/games/join",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 60000,
        headers: {
            'ACCESS-TOKEN': $('#accessToken').val()
        },
        success: function (data) {
            var json = "<h4>Ajax Response</h4><pre>"
                + JSON.stringify(data, null, 4) + "</pre>";
            $('#feedback').html(json);
            console.log("SUCCESS : ", data);
            $(location).attr("href", "/games?accessToken="+data.accessToken)
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
                $('#feedback').html(json);
            console.log("ERROR : ", e);
        }
    });
}
