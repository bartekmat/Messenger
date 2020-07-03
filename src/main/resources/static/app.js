var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/allMessages', function (msg) {
            showNewMessage(JSON.parse(msg.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage() {
    let content = $("#newMessageInput").val();
    let username = $("#usernameInput").val();

    console.log(content);
    console.log("username"+username);

    stompClient.send("/app/publishMessage", {}, JSON.stringify(
        {
            'content': content,
            'username': username
        }
        ));
}

function showNewMessage(message) {
    let div = document.createElement('div');
    let timestampSpan = document.createElement('span');
    timestampSpan.textContent = message.createdAt + ' ';

    let userSpan = document.createElement('span');
    userSpan.textContent = message.user.username + ' ';
    userSpan.style = 'color: ' + message.user.colorCode + ';';

    let textSpan = document.createElement('span');
    textSpan.textContent = message.content;

    div.appendChild(timestampSpan);
    div.appendChild(userSpan);
    div.appendChild(textSpan);

    let allMessagesDiv = $("#allMessagesDiv");
    allMessagesDiv.append(div);

    allMessagesDiv.stop().animate({
        scrollTop: allMessagesDiv[0].scrollHeight
    }, 500);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
    // $( "#connect" ).click(function() { connect(); });
    // $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendMessage" ).click(function() {
        sendMessage();
    });
});