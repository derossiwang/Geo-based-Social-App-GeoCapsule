package com.example.group_w01_07_3.features.discover;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.group_w01_07_3.R;
import com.example.group_w01_07_3.util.MessageUtil;
import com.example.group_w01_07_3.util.RecordAudioUtil;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Display opened Geo-capsule and stop placeholder shimmer effect
 */
public class Display extends AppCompatActivity {

    //APP View
    private CoordinatorLayout coordinatorLayout;
    private TextView username;
    private ImageView profile;
    private TextView date;
    private TextView privacy;
    private Toolbar mToolbar;
    private ImageView img;
    private TextView title;
    private TextView content;
    private ImageButton play;

    // Content of opened Geo-capsule
    private JSONObject capsuleInfo;
    private static final String TAG = "Display Activity";
    private int private_status;
    private MediaPlayer mediaPlayer;
    private String capsuleTitle;
    private String capsuleContent;
    private String imagelink;
    private String audiolink;
    private Boolean startPlay;
    private String name;
    private String avater_link;
    private String open_date;
    private RecordAudioUtil media;

    //Shimmer Place holder Section
    private ShimmerFrameLayout shimmerImage, shimmerAvatar, shimmerVoice;

    /**
     * Initialize display activity,read the information of capsule need to display, received from
     * discover page
     *
     * @param savedInstanceState the data it most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mToolbar = findViewById(R.id.display_history_capsule_back_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Opened Capsule");

        //navigate back to account page.
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extra_information = getIntent().getExtras();
        String extra = getIntent().getStringExtra("capsule");
        Log.d("The intent information", "onCreate: " + extra);

        initView();

        if (extra != null) {
            try {
                JSONObject capsuleInfo = new JSONObject(extra);
                display(capsuleInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "There is problem when opening the capsule", Toast.LENGTH_LONG);
        }
    }

    /**
     * Initialise required layout view
     */
    private void initView() {
        coordinatorLayout = findViewById(R.id.display_history_mega_layout);

        privacy = (TextView) findViewById(R.id.display_detail_capsule_private_public_tag);
        img = (ImageView) findViewById(R.id.display_detail_image);
        title = (TextView) findViewById(R.id.display_detail_title);
        content = (TextView) findViewById(R.id.display_detail_content);
        play = (ImageButton) findViewById(R.id.display_audio_play);
        username = (TextView) findViewById(R.id.display_detail_username);
        profile = (ImageView) findViewById(R.id.display_detail_capsule_original_user_avatar);
        date = (TextView) findViewById(R.id.display_detail_date);
        shimmerImage = findViewById(R.id.display_detail_shimmer_image);
        shimmerAvatar = findViewById(R.id.display_detail_shimmer_avatar);
        shimmerVoice = findViewById(R.id.display_detail_shimmer_voice);
    }

    /**
     * Reads information from intent about content to display,display the information including
     * title, content, date of open the capsule, optional image, optional audio, avater of user,
     * name of user
     *
     * @param capsuleInfo information need to display, store in an json object
     * @throws JSONException possible exception on information reading
     */
    private void display(JSONObject capsuleInfo) throws JSONException {
        private_status = capsuleInfo.getInt("cpermission");
        capsuleTitle = capsuleInfo.getString("ctitle");
        capsuleContent = capsuleInfo.getString("ccontent");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        open_date = sdf.format(Calendar.getInstance().getTime());

        imagelink = capsuleInfo.getString("cimage");
        Log.d(TAG, "display: " + "the new image Link" + imagelink);
        audiolink = capsuleInfo.getString("caudio");
        name = capsuleInfo.getString("cusr");
        avater_link = capsuleInfo.getString("cavatar");

        if (!this.isDestroyed()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    date.setText("Opened at: " + open_date);
                    title.setText(capsuleTitle);
                    content.setText(capsuleContent);
                    username.setText(name);
                    //check the privacy status of capsule
                    if (private_status == 1) {
                        privacy.setText("Public Geo-Capsule");
                    } else {
                        privacy.setText("Your Private Geo-Capsule");
                    }

                    if (audiolink != "null") {
                        loadVoice();
                    } else {
                        //stop shimmer effect, set audio button to be invisible
                        shimmerVoice.stopShimmer();
                        shimmerVoice.setVisibility(View.GONE);
                    }
                    //if there is image, load image to image view,otherwise, use place holder image
                    if (imagelink != "null") {
                        loadImage();
                    } else {
                        shimmerImage.stopShimmer();
                        shimmerImage.setVisibility(View.GONE);
                        img.requestLayout();
                        img.setMinimumHeight(48);
                        img.setVisibility(View.VISIBLE);
                    }

                    //if there is avatar link, load it to corresponding place, otherwise, use place
                    //holder.
                    if (avater_link != "null") {
                        loadAvatar();
                    } else {
                        shimmerAvatar.stopShimmer();
                        shimmerAvatar.setVisibility(View.GONE);
                        profile.setVisibility(View.VISIBLE);
                        profile.setImageResource(R.drawable.avatar_sample);
                    }

                }
            });
        }
    }

    /**
     * Load image of the Geo-capsule, using Glide, reinforced by auto loading once internet restored
     */
    private void loadImage() {
        //Must use  this tree observer to load content image
        img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ensure we call this only once
                img.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                //use Glide to load image, once successful loaded, turn off shimmer and display image
                Glide.with(Display.this)
                        .load(imagelink)
                        .apply(new RequestOptions().override(img.getWidth(), 0))
                        .listener(new RequestListener<Drawable>() {
                            /**
                             * If the image could not be loaded, display the internet connection error
                             * to user
                             *
                             * @param e exception containing information about why the request failed
                             * @param model model we were trying to load when the exception occurred
                             * @param target  target we were trying to load the image into
                             * @param isFirstResource true if this exception is for the first resource to load.
                             * @return
                             */
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                MessageUtil.displaySnackbar(coordinatorLayout,
                                        "Failed to load the capsule image. Please check internet connection.",
                                        Snackbar.LENGTH_LONG);
                                return false;
                            }

                            /**
                             * Remove shimmer effect if the image is loaded successfully.
                             *
                             * @param resource resource that was loaded for the target.
                             * @param model specific model that was used to load the image.
                             * @param target target the model was loaded into.
                             * @param dataSource the resource was loaded from.
                             * @param isFirstResource true if this is the first resource to in this load to be loaded into the target
                             * @return
                             */
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                shimmerImage.stopShimmer();
                                shimmerImage.setVisibility(View.GONE);
                                img.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(img);
            }
        });
    }

    /**
     * load avatar photo to image view to display, handle failure condition,remove shimmer effect
     * if loaded successfully.
     */
    private void loadAvatar() {
        //avatar view has fix size, so no need to use viewtree
        Glide.with(this)
                .load(avater_link)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        MessageUtil.displaySnackbar(coordinatorLayout,
                                "Failed to load Geo-capsule creator avatar. Please check internet connection.",
                                Snackbar.LENGTH_LONG);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        shimmerAvatar.stopShimmer();
                        shimmerAvatar.setVisibility(View.GONE);
                        profile.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(profile);
    }

    /**
     * load audio to display,handle the situation if audio could not be loaded due to internet failure.
     */
    private void loadVoice() {
        mediaPlayer = new MediaPlayer();
        //when audio is loaded successfully, remove the shimmer effect and set audio button to be
        //visible
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                shimmerVoice.stopShimmer();
                shimmerVoice.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });
        startPlay = true;
        //set listener to listener to user's click on audio play button
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startPlay) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
                startPlay = !startPlay;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer m) {
                startPlay = !startPlay;
            }
        });

        try {
            mediaPlayer.setDataSource(audiolink);
            mediaPlayer.prepareAsync();
            mediaPlayer.setLooping(false);
            //handle the internet loss condition, notify the user about Internet connection failure.
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                //handle media player lose network connection
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    if (i == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                        MessageUtil.displaySnackbar(coordinatorLayout,
                                "Failed to Load audio. Please check internet connection.",
                                Snackbar.LENGTH_LONG);
                    }
                    return false;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Back to the discover capsule page.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.pop_in);
        //stop the audio play, if the user
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * stop audio playback if activity has been finished
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * stop audio playback if current page is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

}