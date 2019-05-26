/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import static java.lang.Math.sqrt;

/**
 *
 * @author Alexander
 */
public class MovingPart implements EntityPart {
    private double dx, dy;
    private float speed, radians;
    private boolean left, right, up, down, moving;
    private boolean ai;
    private float[] destination;

    public void setDestination(float[] destination) {
        this.destination = destination;
    }

    public MovingPart(float Speed) {
        this.speed = Speed;
        this.ai = false;
    }
    public MovingPart(float Speed, float radians) {
        this.speed = Speed;
        this.radians = radians;
    }

    public MovingPart(float speed, boolean ai) {
        this.speed = speed;
        this.ai = ai;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
        PositionPart positionPart = entity.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();
        float dt = gameData.getDelta();

        if (ai) {
//            float targetRadians = (float) Math.atan2(y - this.destination[1], x - this.destination[0]);
            float targetRadians = (float) Math.atan2(this.destination[1] - y, this.destination[0] - x);

            dx = speed * Math.cos(targetRadians);
            dy = speed * Math.sin(targetRadians);
        }
        if (left) {
            dy = 0;
            dx = -speed;
        }

        if (right) {
            dy = 0;
            dx = speed;
        }

        // accelerating            
        if (up) {
            dy = speed;
            dx = 0;
        }

        if (down) {
            dy = -speed;
            dx = 0;
        }

        if (up && left) {
            dy = speed;
            dx = -speed;
        }

        if (up && right) {
            dy = speed;
            dx = speed;
        }

        if (down && left) {
            dy = -speed;
            dx = -speed;
        }

        if (down && right) {
            dy = -speed;
            dx = speed;
        }

        if (up && down) {
            dy = 0;
            dx = 0;
        }

        if (right && left) {
            dy = 0;
            dx = 0;
        }

        // deccelerating
        float vec = (float) sqrt(dx * dx + dy * dy);
        if (!isDown() && !isUp() && !ai) {
            dy = 0;
        }
        if (!isLeft() && !isRight() && !ai) {
            dx = 0;
        }
        if (vec > speed) {
            dx = (dx / vec) * speed;
            dy = (dy / vec) * speed;
        }

        // set position
        x += dx * dt;
        if (x > gameData.getDisplayWidth()) {
            x = 0;
        } else if (x < 0) {
            x = gameData.getDisplayWidth();
        }

        y += dy * dt;
        if (y > gameData.getDisplayHeight()) {
            y = 0;
        } else if (y < 0) {
            y = gameData.getDisplayHeight();
        }

        if (ai) {
            System.out.println("dx " + dx + " dy " + dy);

        }
        positionPart.setX(x);
        positionPart.setY(y);

        positionPart.setRadians(radians);
    }

}
