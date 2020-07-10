var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/allMessages', function (msg) {
            console.log('new public message')
            showNewMessage(JSON.parse(msg.body));
        });
        stompClient.subscribe('/topic/allLogins', function (event) {
            console.log('new login')
            handleUsersActivity(JSON.parse(event.body));
        });
        stompClient.subscribe('/user/topic/privateMessages', function (msg) {
            console.log('new private message')
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
    senderSpan.textContent = message.sender.username + ' ';
    senderSpan.style = 'color: ' + message.sender.colorCode + ';';

    let arrowSpan = null;
    let recipientSpan = null;
    if (message.recipient != null) {
        arrowSpan = document.createElement('span');
        arrowSpan.textContent = '➡';

        recipientSpan = document.createElement('span');
        recipientSpan.textContent = ' ' + message.recipient.username + ' ';
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

function handleUsersActivity(event) {
    let allActiveUsersDiv = $("#allActiveUsersDiv");
    if (event.type === 'LOGGED_IN') {
        console.log('should append');

        // th:onclick="'toggleDirectMessageUser(\'' + ${user.name} + '\')'"
        // toggleDirectMessageUser(this.getAttribute('data1'));

        let li = document.createElement('li');
        li.classList.add('no-active');
        li.setAttribute("onclick", 'toggleDirectMessageUser(\'' + event.username + '\');');

        let flexDiv = document.createElement('div');
        flexDiv.classList.add('d-flex');
        flexDiv.classList.add('bd-highlight');

        let imgDiv = document.createElement('div');
        imgDiv.classList.add('img_cont');

        let img = document.createElement('img');
        img.src = event.logoLink;
        img.classList.add('rounded-circle');
        img.classList.add('user_img');

        let iconSpan = document.createElement('span');
        iconSpan.classList.add('online_icon');

        let userInfoDiv = document.createElement('div');
        userInfoDiv.classList.add('user_info');

        let usernameSpan = document.createElement('span');
        usernameSpan.textContent = event.username;

        let p = document.createElement('p');
        p.textContent = 'is online';

        imgDiv.appendChild(img);
        imgDiv.appendChild(iconSpan);

        userInfoDiv.appendChild(usernameSpan);
        userInfoDiv.appendChild(p);

        flexDiv.appendChild(imgDiv);
        flexDiv.appendChild(userInfoDiv);

        li.appendChild(flexDiv);

        allActiveUsersDiv.append(li);

    } else if (event.type === "LOGGED_OUT") {
        let allChildren = allActiveUsersDiv[0].children;
        for (let i = 0; i < allChildren.length; i++) {
            let child = allChildren[i];
            if (child.textContent.trim() === event.username) {
                allActiveUsersDiv[0].removeChild(child);
            }
        }
    }
}

let directMessagesRecipient = null;

function toggleDirectMessageUser(username) {
    console.log("Clicked toggleDirectMessageUser for user ", username);
    let allActiveUsersDiv = document.getElementById('allActiveUsersDiv');
    let allChildren = allActiveUsersDiv.childNodes;
    let buttonClicked = null;
    for (let i = 0; i < allChildren.length; i++) {
        let child = allChildren[i];
        console.log('child text '+child.textContent.trim())
        if (child.textContent.startsWith(username)) {
            console.log('true child text '+child.textContent.trim())
            buttonClicked = child;
        }
    }

    if (directMessagesRecipient == null) {
        directMessagesRecipient = username;
        buttonClicked.className = "active";
    } else if (directMessagesRecipient === username) {
        directMessagesRecipient = null;
        buttonClicked.className = "no-active";
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