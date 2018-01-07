var paddleId;
var ball;
var score;
var paddles = [];

var leftScore = 0;
var rightScore = 0;

function setup() {
    //Connect a new player to get if we are player 1 or 2
    socket = io.connect('http://localhost:3000');
    socket.emit('newPlayer');
    socket.on('startCord', function(data) {
       paddleId = data.id;
    });

    socket.on('paddleBeat', function(data) {
        paddles = data;
    });
    socket.on('ballBeat', function(data) {
       ball = data;
    });
    socket.on('scoreBeat', function(data) {
        score = data;
    });

    createCanvas(1000, 600);
}

function draw() {

    background(0);
    textSize(64);
    if(score != null){
        leftScore = score.score1;
        rightScore = score.score2;
    }
    text(leftScore, 10, 80);
    text(rightScore, width-84, 80);

    //We are drawing the paddles
    for (var i = 0; i < paddles.length; i++){
        fill(255);
        rect(paddles[i].x, paddles[i].y, 10, 200);
    }
    if(ball != null){
        fill(255);
        ellipse(ball.x, ball.y, ball.r, ball.r);
    }

    if(keyIsDown(UP_ARROW) || keyIsDown(DOWN_ARROW)) {
        var add = 0;
        if(keyIsDown(UP_ARROW)){
            add = -5;
        } else if(keyIsDown(DOWN_ARROW)){
            add = 5;
        }
        var data = {
            add: add,
            id: paddleId
        }
        socket.emit('update', data);
    }
}