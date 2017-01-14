package ua.matvienko_apps.controlyourbudget.classes;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import ua.matvienko_apps.controlyourbudget.R;

/**
 * Created by Alexandr on 10/01/2017.
 */

public class Piggy {

    private ImageView piggyBackground;
    private Context context;

    private CustomAnimationDrawable piggyJoyAnimation;
    private CustomAnimationDrawable piggyCryAnimation;
    private CustomAnimationDrawable piggyAddMoneyAnimation;
    private AnimationDrawable piggyBlinkingAnimation;

    public Piggy(ImageView piggyBackground, final Context context) {
        this.context = context;

        this.piggyBackground = piggyBackground;
        this.piggyBackground.setBackgroundResource(R.drawable.piggy_pattern);


        new Thread() {
            public void run() {
                piggyJoyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_joy_animation)) {
                    @Override
                    public void onAnimationFinish() {
                        piggyJoyAnimation.stop();
                        startBlinking();
                    }
                };
                piggyCryAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_cry_animation)) {
                    @Override
                    public void onAnimationFinish() {
                        piggyCryAnimation.stop();
                        startBlinking();
                    }
                };
                piggyAddMoneyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_add_money_animation)) {
                    @Override
                    public void onAnimationFinish() {
                        piggyAddMoneyAnimation.stop();
                        startJoy();
                    }
                };
            }
        }.start();


        piggyBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlayingAnimation())
                    startCry();

            }
        });

        piggyBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isPlayingAnimation())
                    startJoy();
                return true;
            }
        });

        startBlinking();

    }


    public void startBlinking() {
        piggyBackground.setBackgroundResource(R.drawable.piggy_blinking_animation);
        piggyBlinkingAnimation = (AnimationDrawable) piggyBackground.getBackground();
        piggyBlinkingAnimation.start();
    }

    public void startCry() {
        piggyBackground.setBackground(piggyCryAnimation);
        piggyBlinkingAnimation.stop();
        piggyCryAnimation.start();
    }

    public void startJoy() {
        piggyBackground.setBackground(piggyJoyAnimation);
        piggyBlinkingAnimation.stop();
        piggyJoyAnimation.start();

        new Thread() {
            public void run() {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.piggy_joy_sound);
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0.4f, 0.4f);
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }

            }
        }.start();
    }

    public void startAddingMoney() {
        piggyBackground.setBackground(piggyAddMoneyAnimation);
        piggyBlinkingAnimation.stop();
        piggyAddMoneyAnimation.start();
    }

    public boolean isPlayingAnimation() {
        return (piggyAddMoneyAnimation.isRunning() || piggyJoyAnimation.isRunning() || piggyCryAnimation.isRunning());

    }
}
