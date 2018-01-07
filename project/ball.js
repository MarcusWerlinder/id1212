var methods = {
    checkPaddle: function(paddle, ball) {
        if(ball.y - ball.r < paddle.y + paddle.h
            && ball.y + ball.r > paddle.y
            && ball.x + ball.r > paddle.x
            && ball.x - ball.r < paddle.x + paddle.w
            && paddle.side == 1) {

            ball.xspeed *= -1;
        } else if(ball.y - ball.r < paddle.y + paddle.h
            && ball.y + ball.r > paddle.y
            && ball.x - ball.r < paddle.x + paddle.w
            && ball.x + ball.r > paddle.x
            && paddle.side == 2) {

            ball.xspeed *= -1;
        }
        return ball;
    },
    goal: function(ball) {
        var scored = 0;
        if(ball.x > 1000) {
            scored = 1;
        }
        if(ball.x < 0) {
            scored = 2;
        }
        return scored;
    },
    edges: function(ball) {
        if(ball.y < 0 || ball.y > 600) {
            ball.yspeed *= -1;
        }
        return ball;
    },
    reset: function(ball) {
        ball.x = 500;
        ball.y = 300;
        var angle = Math.random(-Math.PI/4, Math.PI/4);
        ball.xspeed = 5 * Math.cos(angle);
        ball.yspeed = 5 * Math.sin(angle);

        if(Math.random(1) < 0.5) {
            ball.xspeed *= -1;
        }
        return ball;
    },
    update: function(ball) {
        ball.x += ball.xspeed;
        ball.y += ball.yspeed;
        return ball;
    }
};

exports.data = methods;