<!DOCTYPE html>
<html data-bs-theme="light" lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Scoreboard</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="/assets/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic&display=swap">

    <style>
        /* Floating chat styles */
        .chat-container {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 300px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
            z-index: 9999;
        }

        .chat-header {
            background-color: #f5f5f5;
            padding: 10px;
            border-bottom: 1px solid #ccc;
            cursor: pointer;
        }

        .chat-body {
            padding: 10px;
            height: 300px;
            overflow-y: auto;
        }

        .chat-footer {
            padding: 10px;
            border-top: 1px solid #ccc;
        }

        .chat-footer input {
            width: 100%;
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }

        .chat-message {
            margin-bottom: 10px;
            padding: 5px;
            border-radius: 5px;
            max-width: 80%;
        }

        .chat-message.user {
            background-color: #e6f7ff;
            align-self: flex-end;
        }

        .chat-message.bot {
            background-color: #f5f5f5;
            align-self: flex-start;
        }

        .chat-message.typing {
            align-self: flex-start;
            animation: typing 1s infinite;
        }

        @keyframes typing {
            0% {
                background-color: #f5f5f5;
            }
            50% {
                background-color: #e0e0e0;
            }
            100% {
                background-color: #f5f5f5;
            }
        }
    </style>
</head>

<body>
<header class="text-center text-white masthead" style="background: url('/css/background.jpg')no-repeat center center;background-size: cover;height: 126.188px;">
    <div class="container">
        <div class="row">
            <div class="col-xl-9 mx-auto position-relative">
                <h1 class="mb-5">Contempt in Court</h1>
            </div>
        </div>
    </div>
</header>
<section class="showcase py-2">
    <div class="container-fluid p-0">
        <div class="row justify-content-center mb-5">
            <div class="col-md-8 col-xl-6">
                <br />
                <h1 class="text-center mt-1">Scores</h1>
                <br />
                <div class="container">
                    <div class="row">
                        <div class="col-md-6">
                            <h2>Miles Edgeworth</h2>
                            <div id="edgeworth-wins" class="alert alert-success"></div>
                            <div id="edgeworth-losses" class="alert alert-danger"></div>
                        </div>
                        <div class="col-md-6">
                            <h2>Phoenix Wright</h2>
                            <div id="wright-wins" class="alert alert-success"></div>
                            <div id="wright-losses" class="alert alert-danger"></div>
                        </div>
                    </div>
                </div>
                <div class="d-flex justify-content-left mb-1 mt-1">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#filterModal">
                        <i class="fa fa-filter"></i>
                    </button>
                </div>
                <div class="d-flex justify-content-center">
                    <table id="scoresTable" class="table table-striped table-bordered"></table>
                </div>
                <div class="modal fade" id="filterModal" tabindex="-1" aria-labelledby="filterModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="filterModalLabel">Filter Scores</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form id="filterForm" class="form-inline mt-2">
                                    <input id="playerNameFilter" class="form-control mr-2" type="text" placeholder="Filter by player name">
                                    <input id="dateFilter" class="form-control mr-2" type="date" placeholder="Filter by date">
                                    <br />
                                    <button type="submit" class="btn btn-primary pt-0" data-bs-dismiss="modal" aria-label="Close">Filter</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="chat-container">
        <div class="chat-header">
            <h5>Diana 2.0</h5>
        </div>
        <div class="chat-body">
            <p type="text">You're all caught up!</p>
        </div>
        <div class="chat-footer">
            <input type="text" id="chat-input" placeholder="Type your message...">
        </div>
    </div>
</section>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
</body>

<script>
    $('#filterForm').on('submit', function(e) {
        e.preventDefault();
        let playerName = $('#playerNameFilter').val();
        let date = $('#dateFilter').val();
        getScores(playerName, date);
    });

    function calculateWinsAndLosses(data) {
        let edgeworthWins = 0;
        let edgeworthLosses = 0;
        let wrightWins = 0;
        let wrightLosses = 0;

        data.forEach((element) => {
            if (element.playerName === 'Miles Edgeworth') {
                if (element.result === 'Won') {
                    edgeworthWins++;
                } else {
                    edgeworthLosses++;
                }
            } else if (element.playerName === 'Phoenix Wright') {
                if (element.result === 'Won') {
                    wrightWins++;
                } else {
                    wrightLosses++;
                }
            }
        });

        $('#edgeworth-wins').html(edgeworthWins + ' Wins');
        $('#edgeworth-losses').html(edgeworthLosses + ' Losses');
        $('#wright-wins').html(wrightWins + ' Wins');
        $('#wright-losses').html(wrightLosses + ' Losses');
    }

    function getScores(playerName, date) {
        $.ajax({
            url: 'http://127.0.0.1:6969/api/all',
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                console.log(data);
                calculateWinsAndLosses(data);
                if (playerName) {
                    data = data.filter(element => element.playerName === playerName);
                }
                if (date) {
                    data = data.filter(element => element.datePlayed.startsWith(date));
                }
                let table = $("#scoresTable");
                table.empty();
                table.append("<thead class='text-center'><tr><th>Player</th><th>Result</th><th>Date Played</th></tr></thead>");
                let tbody = $("<tbody></tbody>");
                data.forEach((element) => {
                    let datePlayed = new Date(element.datePlayed);
                    let formattedDate = datePlayed.toLocaleDateString();
                    let formattedTime = datePlayed.toLocaleTimeString();
                    tbody.append("<tr class='text-center'><td>" + element.playerName + "</td><td>" + element.result + "</td><td>" + formattedDate + " " + formattedTime + "</td></tr>");
                });
                table.append(tbody);
            }
        });
    }

    $(document).ready(function() {
        getScores();

        var chatBody = $(".chat-body");
        var chatInput = $("#chat-input");

        chatInput.on("keydown", function(e) {
            if (e.keyCode === 13) { // Enter key
                sendMessage(chatInput.val());
                chatInput.val("");
            }
        });

        function sendMessage(message) {
            var userMessage = $("<div>").addClass("chat-message user").text(message);
            chatBody.append(userMessage);
            chatBody.scrollTop(chatBody[0].scrollHeight);

            // Show typing animation
            var typingMessage = $("<div>").addClass("chat-message typing").text("...");
            chatBody.append(typingMessage);
            chatBody.scrollTop(chatBody[0].scrollHeight);

            // Call the API and replace the typing animation with the response
            $.ajax({
                url: "http://127.0.0.1:5000/api/query",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({ "query": message }),
                success: function(data) {
                    chatBody.find(".typing").remove();
                    var botMessage = $("<div>").addClass("chat-message bot").text(data.response);
                    chatBody.append(botMessage);
                    chatBody.scrollTop(chatBody[0].scrollHeight);
                },
                error: function() {
                    chatBody.find(".typing").remove();
                    var errorMessage = $("<div>").addClass("chat-message bot").text("An error occurred while processing the request.");
                    chatBody.append(errorMessage);
                    chatBody.scrollTop(chatBody[0].scrollHeight);
                }
            });
        }
    });

    function addScore() {
        let playerName = $("#playerName").val();
        let result = $("#result").val();
        let datePlayed = $("#datePlayed").val();
        $.ajax({
            url: 'http://localhost:6969/api/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                playerName: playerName,
                result: result,
                datePlayed: datePlayed
            }),
            success: function() {
                getScores();
            }
        });
    }

    function deleteScore() {
        let playerName = $("#playerName").val();
        $.ajax({
            url: 'http://localhost:6969/api/delete/' + playerName,
            type: 'DELETE',
            success: function() {
                getScores();
            }
        });
    }
    getScores();
</script>

</html>