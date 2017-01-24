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
    private CustomAnimationDrawable piggyGetMoneyAnimation;
    private AnimationDrawable piggyBlinkingAnimation;

    public Piggy(ImageView piggyBackground, final Context context) {
        this.context = context;

        this.piggyBackground = piggyBackground;
        this.piggyBackground.setBackgroundResource(R.drawable.piggy_pattern);


        piggyBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startCry();
            }
        });

        piggyBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
        if (!isPlayingAnimation()) {
            piggyCryAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_cry_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyCryAnimation.stop();
                    piggyCryAnimation = null;
                    startBlinking();
                }
            };
            piggyBackground.setBackground(piggyCryAnimation);
            piggyBlinkingAnimation.stop();
            piggyCryAnimation.start();

            new Thread() {
                public void run() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.piggy_cry_sound);
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.3f, 0.3f);
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }

                }
            }.start();

        }
    }
    public void startJoy() {
        if (!isPlayingAnimation()) {
            piggyJoyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_joy_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyJoyAnimation.stop();
                    piggyJoyAnimation = null;
                    startBlinking();
                }
            };
            piggyBackground.setBackground(piggyJoyAnimation);
            piggyBlinkingAnimation.stop();
            piggyJoyAnimation.start();

            new Thread() {
                public void run() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.piggy_joy_sound);
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.6f, 0.6f);
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }

                }
            }.start();
        }
    }
    public void startAddingMoney() {
        if (!isPlayingAnimation()) {
            piggyAddMoneyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_add_money_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyAddMoneyAnimation.stop();
                    piggyAddMoneyAnimation = null;
                    startJoy();
                }
            };
            piggyBackground.setBackground(piggyAddMoneyAnimation);
            piggyBlinkingAnimation.stop();
            piggyAddMoneyAnimation.start();

            new Thread() {
                public void run() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.adding_money_sound);
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.6f, 0.6f);
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }
                }
            }.start();
        }
    }
    public void startGettingMoney() {
        if (!isPlayingAnimation()) {
            piggyGetMoneyAnimation = new CustomAnimationDrawable((AnimationDrawable) ContextCompat.getDrawable(context, R.drawable.piggy_get_money_animation)) {
                @Override
                public void onAnimationFinish() {
                    piggyGetMoneyAnimation.stop();
                    piggyGetMoneyAnimation = null;
                    startBlinking();
                }
            };
            piggyBackground.setBackground(piggyGetMoneyAnimation);
            piggyBlinkingAnimation.stop();
            piggyGetMoneyAnimation.start();

            new Thread() {
                public void run() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.getting_money_sound);
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.6f, 0.6f);
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }
                }
            }.start();
        }
    }

    public boolean isPlayingAnimation() {
        return piggyJoyAnimation != null || piggyCryAnimation != null || piggyAddMoneyAnimation != null || piggyGetMoneyAnimation != null;
    }
}
