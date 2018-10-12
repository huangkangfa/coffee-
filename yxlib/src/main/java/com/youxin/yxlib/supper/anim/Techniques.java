
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 daimajia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.youxin.yxlib.supper.anim;


import com.youxin.yxlib.supper.anim.attention.BounceAnimator;
import com.youxin.yxlib.supper.anim.attention.FlashAnimator;
import com.youxin.yxlib.supper.anim.attention.PulseAnimator;
import com.youxin.yxlib.supper.anim.attention.RubberBandAnimator;
import com.youxin.yxlib.supper.anim.attention.ShakeAnimator;
import com.youxin.yxlib.supper.anim.attention.StandUpAnimator;
import com.youxin.yxlib.supper.anim.attention.SwingAnimator;
import com.youxin.yxlib.supper.anim.attention.TadaAnimator;
import com.youxin.yxlib.supper.anim.attention.WaveAnimator;
import com.youxin.yxlib.supper.anim.attention.WobbleAnimator;
import com.youxin.yxlib.supper.anim.bouncing_entrances.BounceInAnimator;
import com.youxin.yxlib.supper.anim.bouncing_entrances.BounceInDownAnimator;
import com.youxin.yxlib.supper.anim.bouncing_entrances.BounceInLeftAnimator;
import com.youxin.yxlib.supper.anim.bouncing_entrances.BounceInRightAnimator;
import com.youxin.yxlib.supper.anim.bouncing_entrances.BounceInUpAnimator;
import com.youxin.yxlib.supper.anim.fading_entrances.FadeInAnimator;
import com.youxin.yxlib.supper.anim.fading_entrances.FadeInDownAnimator;
import com.youxin.yxlib.supper.anim.fading_entrances.FadeInLeftAnimator;
import com.youxin.yxlib.supper.anim.fading_entrances.FadeInRightAnimator;
import com.youxin.yxlib.supper.anim.fading_entrances.FadeInUpAnimator;
import com.youxin.yxlib.supper.anim.fading_exits.FadeOutAnimator;
import com.youxin.yxlib.supper.anim.fading_exits.FadeOutDownAnimator;
import com.youxin.yxlib.supper.anim.fading_exits.FadeOutLeftAnimator;
import com.youxin.yxlib.supper.anim.fading_exits.FadeOutRightAnimator;
import com.youxin.yxlib.supper.anim.fading_exits.FadeOutUpAnimator;
import com.youxin.yxlib.supper.anim.flippers.FlipInXAnimator;
import com.youxin.yxlib.supper.anim.flippers.FlipInYAnimator;
import com.youxin.yxlib.supper.anim.flippers.FlipOutXAnimator;
import com.youxin.yxlib.supper.anim.flippers.FlipOutYAnimator;
import com.youxin.yxlib.supper.anim.rotating_entrances.RotateInAnimator;
import com.youxin.yxlib.supper.anim.rotating_entrances.RotateInDownLeftAnimator;
import com.youxin.yxlib.supper.anim.rotating_entrances.RotateInDownRightAnimator;
import com.youxin.yxlib.supper.anim.rotating_entrances.RotateInUpLeftAnimator;
import com.youxin.yxlib.supper.anim.rotating_entrances.RotateInUpRightAnimator;
import com.youxin.yxlib.supper.anim.rotating_exits.RotateOutAnimator;
import com.youxin.yxlib.supper.anim.rotating_exits.RotateOutDownLeftAnimator;
import com.youxin.yxlib.supper.anim.rotating_exits.RotateOutDownRightAnimator;
import com.youxin.yxlib.supper.anim.rotating_exits.RotateOutUpLeftAnimator;
import com.youxin.yxlib.supper.anim.rotating_exits.RotateOutUpRightAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideInDownAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideInLeftAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideInRightAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideInUpAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideOutDownAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideOutLeftAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideOutRightAnimator;
import com.youxin.yxlib.supper.anim.sliders.SlideOutUpAnimator;
import com.youxin.yxlib.supper.anim.specials.RollInAnimator;
import com.youxin.yxlib.supper.anim.specials.RollOutAnimator;
import com.youxin.yxlib.supper.anim.zooming_entrances.ZoomInAnimator;
import com.youxin.yxlib.supper.anim.zooming_entrances.ZoomInDownAnimator;
import com.youxin.yxlib.supper.anim.zooming_entrances.ZoomInLeftAnimator;
import com.youxin.yxlib.supper.anim.zooming_entrances.ZoomInRightAnimator;
import com.youxin.yxlib.supper.anim.zooming_entrances.ZoomInUpAnimator;
import com.youxin.yxlib.supper.anim.zooming_exits.ZoomOutAnimator;
import com.youxin.yxlib.supper.anim.zooming_exits.ZoomOutDownAnimator;
import com.youxin.yxlib.supper.anim.zooming_exits.ZoomOutLeftAnimator;
import com.youxin.yxlib.supper.anim.zooming_exits.ZoomOutRightAnimator;
import com.youxin.yxlib.supper.anim.zooming_exits.ZoomOutUpAnimator;

public enum Techniques {


    Flash(FlashAnimator.class),
    Pulse(PulseAnimator.class),
    RubberBand(RubberBandAnimator.class),
    Shake(ShakeAnimator.class),
    Swing(SwingAnimator.class),
    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
    StandUp(StandUpAnimator.class),
    Wave(WaveAnimator.class),

    RollIn(RollInAnimator.class),
    RollOut(RollOutAnimator.class),

    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),

    FadeIn(FadeInAnimator.class),
    FadeInUp(FadeInUpAnimator.class),
    FadeInDown(FadeInDownAnimator.class),
    FadeInLeft(FadeInLeftAnimator.class),
    FadeInRight(FadeInRightAnimator.class),

    FadeOut(FadeOutAnimator.class),
    FadeOutDown(FadeOutDownAnimator.class),
    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    FadeOutUp(FadeOutUpAnimator.class),

    FlipInX(FlipInXAnimator.class),
    FlipOutX(FlipOutXAnimator.class),
    FlipInY(FlipInYAnimator.class),
    FlipOutY(FlipOutYAnimator.class),
    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
    RotateInDownRight(RotateInDownRightAnimator.class),
    RotateInUpLeft(RotateInUpLeftAnimator.class),
    RotateInUpRight(RotateInUpRightAnimator.class),

    RotateOut(RotateOutAnimator.class),
    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
    RotateOutDownRight(RotateOutDownRightAnimator.class),
    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),

    SlideInLeft(SlideInLeftAnimator.class),
    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
    SlideInDown(SlideInDownAnimator.class),

    SlideOutLeft(SlideOutLeftAnimator.class),
    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
    SlideOutDown(SlideOutDownAnimator.class),

    ZoomIn(ZoomInAnimator.class),
    ZoomInDown(ZoomInDownAnimator.class),
    ZoomInLeft(ZoomInLeftAnimator.class),
    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),

    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
    ZoomOutLeft(ZoomOutLeftAnimator.class),
    ZoomOutRight(ZoomOutRightAnimator.class),
    ZoomOutUp(ZoomOutUpAnimator.class);



    private Class animatorClazz;

    private Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
