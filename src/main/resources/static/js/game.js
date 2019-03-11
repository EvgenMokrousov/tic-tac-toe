$(document).ready(function () {
    viewGameState();
    var timerId = setTimeout(function tick() {
        viewGameState();
        timerId = setTimeout(tick, 2000);
    }, 2000);

    $("#dostep").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        dostep_submit();
    });


});

function dostep_submit() {
    var search = {}
    search['col'] = $("#col").val();
    search['row'] = $("#row").val();

    $("#btn-dostep").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/games/do_step",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 60000,
        headers: {
            'ACCESS-TOKEN': $('#accessToken').val()
        },
        success: function (data) {
            $('#feedback').text(data.message);

            console.log("SUCCESS : ", data);
            $("#btn-dostep").prop("disabled", false);
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);
            console.log("ERROR : ", e);
            $("#btn-dostep").prop("disabled", false);
        }
    });
};

function viewGameState() {
    $.ajax({
        type: "GET",
        //contentType: "application/json",
        url: "/games/state",
        dataType: 'json',
        cache: false,
        timeout: 60000,
        headers: {
            'ACCESS-TOKEN': $('#accessToken').val()
        },
        success: function (result) {
            $('#owner').html(result.ownerName);
            $('#opponent').html(result.opponentName);
            $('#feedback').text(result.message);
            if (result.winner == "") {
                if (result.whoIs == "owner") {
                    if (result.youTurn) {
                        $("#btn-dostep").prop("disabled", false);
                        if (!$('#owner').hasClass("youTurn")) {
                            $('#owner').addClass("youTurn");
                            $('#opponent').removeClass("youTurn");
                        }
                    }
                    else {
                        $("#btn-dostep").prop("disabled", true);
                        if (!$('#opponent').hasClass("youTurn")) {
                            $('#owner').removeClass("youTurn");
                            $('#opponent').addClass("youTurn");
                        }
                    }
                }
                if (result.whoIs == "opponent") {
                    if (result.youTurn) {
                        $("#btn-dostep").prop("disabled", false);
                        if (!$('#opponent').hasClass("youTurn")) {
                            $('#opponent').addClass("youTurn");
                            $('#owner').removeClass("youTurn");
                        }
                    }
                    else {
                        $("#btn-dostep").prop("disabled", true);
                        if (!$('#owner').hasClass("youTurn")) {
                            $('#opponent').removeClass("youTurn");
                            $('#owner').addClass("youTurn");
                        }
                    }
                }
            } else {
                $('#owner').removeClass("youTurn");
                $('#opponent').removeClass("youTurn");
                if (result.winner == $('#owner').text()) $('#owner').addClass("winner");
                if (result.winner == $('#opponent').text()) $('#opponent').addClass("winner");
            }
            $('#game_space').html(stringToTable(result.field));
            $("#param input[name='whoIs'").val(result.whoIs);
            $("#time").text(formatTime(Math.round(result.gameDuration/1000)));

            if ($("#data-char").val().length > 0) {
                $("#game_space tr:eq(" + $("#row").val() + ") td:eq(" + $("#col").val() +")" ).text($("#data-char").val());
            }
            console.log(result);
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            console.log(e.responseText);
            $('#feedback').html(json);
        }
    });
};

function stringToTable(s) {
    var step = Math.sqrt(s.length)
    var content = "<table>"
    for(i = 0; i < s.length; i++) {
        if (i % step == 0) content += '<tr>';
        content += '<td row=' + Math.floor(i/step).toString() + ' col=' + (i%step).toString() + '>' + s[i] + '</td>';
        if (i % step == step-1) content += '</tr>';
    }
    content += "</table>"

    return content;
};

$("#game_space").click(function (event) {
    var targ = event.target;
    var val = $(targ).text();

    var curRow = $(targ).attr('row');
    var curCol = $(targ).attr('col');
    var prevRow = $("#row").val();
    var prevCol = $("#col").val();

    var whoIs = $("#param input[name='whoIs'").val();
    if (whoIs == 'viewer') return;
    var charX0 = (whoIs == 'owner') ? 'X' :
                 (whoIs == 'opponent') ? '0' :
                 '';
    if (val == charX0) {
        if (curRow == prevRow && curCol == prevCol) {
            $(targ).text("?");

            $("#data-char").val("");
            $("#col").val($(targ).attr(""));
            $("#row").val($(targ).attr(""));
            $("#btn-dostep").prop("disabled", true);
        }
    }
    if (val == '?') {
        if ($("#data-char").val() != "" ) {
            $("#game_space tr:eq(" + prevRow + ") td:eq(" + prevCol +")" ).text("?");
        }
        $(targ).text(charX0);

        $("#data-char").val(charX0);
        $("#col").val(curCol);
        $("#row").val(curRow);
        $("#btn-dostep").prop("disabled", false);
    }
    console.log($("#data-char").val());
});
