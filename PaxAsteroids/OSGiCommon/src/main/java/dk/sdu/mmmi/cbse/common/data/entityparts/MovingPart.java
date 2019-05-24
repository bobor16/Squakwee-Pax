/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private boolean left, right, up, down, moving, mouse;

    public MovingPart(float Speed) {
        this.speed = Speed;
    }

    public MovingPart(float Speed, float radians) {
        this.speed = Speed;
        this.radians = radians;
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

    public void setMouse(boolean mouse) {
        this.mouse = mouse;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isMouse() {
        return mouse;
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
        if (mouse) {
            float angle = positionPart.getMouseRadians(); 
            dx = speed * Math.cos(angle);
            dy = speed * Math.sin(angle);
//            System.out.println("dy: " + dy + " dx " + dx + " angle: " + angle);
        }

        // deccelerating
        float vec = (float) sqrt(dx * dx + dy * dy);
        if (!isDown() && !isUp()) {
            dy = 0;
        }
        if (!isLeft() && !isRight()) {
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

        positionPart.setX(x);
        positionPart.setY(y);

        positionPart.setRadians(radians);
    }

}
