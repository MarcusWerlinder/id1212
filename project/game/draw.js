var boll;
var paddle;

var leftScore = 0;
var rightScore = 0;

function setup() {
    createCanvas(1000, 600);
    boll = new Ball();
    paddleL = new Paddle(50, height/2);
    paddleR = new Paddle(width - 100, height/2);
    boll.construct();
}

function draw() {
    background(0);
    textSize(64);
    text(leftScore, 10, 80);
    text(rightScore, width-44, 80);
    boll.show();
    boll.update();
    boll.Edges(leftScore, rightScore);
    paddleL.show();
    paddleR.show();
    paddleR.update();
    boll.checkRPaddle(paddleR);
    boll.checkLPaddle(paddleL);
}