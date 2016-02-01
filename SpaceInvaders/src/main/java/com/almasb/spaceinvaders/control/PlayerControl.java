/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2016 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.spaceinvaders.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.ents.component.Required;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.entity.component.BoundingBoxComponent;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.spaceinvaders.EntityFactory;
import com.almasb.spaceinvaders.component.InvincibleComponent;
import javafx.util.Duration;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
@Required(PositionComponent.class)
@Required(BoundingBoxComponent.class)
@Required(InvincibleComponent.class)
public class PlayerControl extends AbstractControl {

    private PositionComponent position;
    private BoundingBoxComponent bbox;
    private double tpf = 0.017;

    private InvincibleComponent invicibility;

    private boolean canShoot = true;

    private double delay = 0.5;

    @Override
    public void onAdded(Entity entity) {
        position = entity.getComponentUnsafe(PositionComponent.class);
        bbox = entity.getComponentUnsafe(BoundingBoxComponent.class);
        invicibility = entity.getComponentUnsafe(InvincibleComponent.class);

        GameApplication.getService(ServiceType.MASTER_TIMER)
                .runAtInterval(() -> canShoot = true, Duration.seconds(delay));
    }

    @Override
    public void onUpdate(Entity entity, double tpf) {


        this.tpf = tpf;
    }

    public void left() {
        if (position.getX() >= 5)
            position.translateX(-5 * 60 * tpf);
    }

    public void right() {
        if (position.getX() + bbox.getWidth() <= 650 - 5)
            position.translateX(5 * 60 * tpf);
    }

    public void shoot() {
        if (!canShoot)
            return;

        Entity bullet = EntityFactory.newBullet(getEntity());

        getEntity().getWorld().addEntity(bullet);

        GameApplication.getService(ServiceType.AUDIO_PLAYER)
                .playSound("shoot" + (int)(Math.random() * 4 + 1) + ".wav");

        canShoot = false;
    }

    public void enableInvincibility() {
        invicibility.setValue(true);
    }

    public void disableInvincibility() {
        invicibility.setValue(false);
    }

    public boolean isInvincible() {
        return invicibility.getValue();
    }
}