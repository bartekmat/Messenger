var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/allMessages', function (msg) {
            showNewMessage(JSON.parse(msg.body));
        });
        stompClient.subscribe('/topic/allLogins', function (msg) {
            handleUsersActivity(JSON.parse(msg.body));
        });
        stompClient.subscribe('/user/topic/privateMessages', function (msg) {
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
    let newMessageInput = $("#newMessageInput");
    if (directMessagesRecipient == null) {
        stompClient.send("/app/publishPublicMessage", {}, JSON.stringify(
            {
                'content': newMessageInput.val(),
            }
        ));
    } else {
        stompClient.send("/app/publishPrivateMessage", {}, JSON.stringify(
            {
                'content': newMessageInput.val(),
                'recipient': directMessagesRecipient
            }
        ));
    }
    newMessageInput.val('');
}

function showNewMessage(message) {
    let div = document.createElement('div');
    let timestampSpan = document.createElement('span');
    timestampSpan.textContent = message.createdAt + ' ';

    let senderSpan = document.createElement('span');
    senderSpan.textContent = message.sender.name + ' ';
    senderSpan.style = 'color: ' + message.sender.colorCode + ';';

    let arrowSpan = null;
    let recipientSpan = null;
    if (message.recipient != null) {
        arrowSpan = document.createElement('span');
        arrowSpan.textContent = '➡';

        recipientSpan = document.createElement('span');
        recipientSpan.textContent = ' ' + message.recipient.name + ' ';
        recipientSpan.style = 'color: ' + message.recipient.colorCode + ';';
    }
    let textSpan = null;
    let video = null;
    if (message.type === "TEXT") {
        textSpan = document.createElement('span');
        textSpan.textContent = message.content;
    } else if (message.type === "GIF") {
        video = document.createElement('video');
        video.autoplay = true;
        video.loop = true;
        video.muted = true;
        let source = document.createElement('source');
        source.src = message.content;
        source.type = "video/mp4";
        video.appendChild(source);
    }

    div.appendChild(timestampSpan);
    div.appendChild(senderSpan);
    if (recipientSpan != null) {
        div.appendChild(arrowSpan);
        div.appendChild(recipientSpan);
    }
    if (textSpan != null) {
        div.appendChild(textSpan);
    }
    if (video != null) {
        div.appendChild(video);
    }

    let allMessagesDiv = $("#allMessagesDiv");
    allMessagesDiv.append(div);

    allMessagesDiv.stop().animate({
        scrollTop: allMessagesDiv[0].scrollHeight
    }, 500);
}

function handleUsersActivity(message) {
    let allActiveUsersDiv = $("#allActiveUsersDiv");
    if (message.type === "USER_LOGGED_IN") {
        let userButton = document.createElement('button');
        userButton.textContent = message.username + ' ';
        userButton.style = 'color: ' + message.colorCode + ';';
        userButton.setAttribute("onclick", 'toggleDirectMessageUser(\'' + message.username + '\');');
        // th:onclick="'toggleDirectMessageUser(\'' + ${user.name} + '\')'"
        // toggleDirectMessageUser(this.getAttribute('data1'));

        allActiveUsersDiv.append(userButton);
    } else if (message.type === "USER_LOGGED_OUT") {
        let allChildren = allActiveUsersDiv[0].children;
        for (let i = 0; i < allChildren.length; i++) {
            let child = allChildren[i];
            if (child.textContent.trim() === message.username) {
                allActiveUsersDiv[0].removeChild(child);
            }
        }
    }
}

let directMessagesRecipient = null;

function toggleDirectMessageUser(username) {
    console.log("Clicked toggleDirectMessageUser for user ", username);
    let allActiveUsersDiv = $("#allActiveUsersDiv");
    let allChildren = allActiveUsersDiv[0].children;
    let buttonClicked = null;
    for (let i = 0; i < allChildren.length; i++) {
        let child = allChildren[i];
        if (child.textContent.trim() === username) {
            buttonClicked = child;
        }
    }

    if (directMessagesRecipient == null) {
        directMessagesRecipient = username;
        buttonClicked.style.backgroundColor = 'red';
        buttonClicked.style.textDecoration = 'underline';
    } else if (directMessagesRecipient === username) {
        directMessagesRecipient = null;
        buttonClicked.style.backgroundColor = '';
        buttonClicked.style.textDecoration = '';
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
    // $( "#connect" ).click(function() { connect(); });
    // $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendMessage" ).click(function() { sendMessage(); });
});