package com.idol.prank.call.chat.video.activities.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.idol.prank.call.chat.video.R;
import com.idol.prank.call.chat.video.activities.Home;
import com.idol.prank.call.chat.video.utils.Constant;

public class LiveChat extends AppCompatActivity {

    private EditText messageEditText;
    Button backButton;
    private int retry = 0;
    private LinearLayout chatHistoryLinearLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_layout);
        backButton= findViewById(R.id.back_button);

        messageEditText = findViewById(R.id.question_text_input_edit_text);
        chatHistoryLinearLayout = findViewById(R.id.chat_history_linear_layout);
        Button sendButton = findViewById(R.id.submit_question_button);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(LiveChat.this , Home.class));

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEditText.getText().toString();

                if (!TextUtils.isEmpty(message)) {
                    String response = AutoBot.getResponse(message);
                    addMessageToChat(message, response); // call updated method with message and response arguments
                    messageEditText.setText("");
                }
            }
        });

    }


    private void addMessageToChat(String message, String response) {
        // Create TextView for user message
        TextView userTextView = new TextView(this);
        userTextView.setText(message);
        userTextView.setTextSize(16);
        userTextView.setTextColor(Color.parseColor("#FFFFFF")); // set user message text color
        userTextView.setPadding(15, 15, 15, 15);

        userTextView.setBackgroundResource(R.drawable.bot_message_bg); // set user message background color and rounded border

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(7, 7, 7, 7);
        userTextView.setLayoutParams(layoutParams);

        userTextView.setGravity(Gravity.START);
        // Create TextView for bot response
        TextView botTextView = new TextView(this);
        botTextView.setText(response);
        botTextView.setTextSize(16);
        botTextView.setTextColor(Color.parseColor("#FFFFFF")); // set bot response text color
        botTextView.setPadding(15, 15, 15, 15);;
        botTextView.setBackgroundResource(R.drawable.user_message_bg);
        botTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        // set bot response background color and rounded border
        botTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutParams.setMargins(7, 7, 7, 7);
        botTextView.setLayoutParams(layoutParams);
        botTextView.setGravity(Gravity.END);

        // Create LinearLayout for the message and response
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the TextViews to the LinearLayout
        messageLayout.addView(userTextView);
        messageLayout.addView(botTextView);

        // Add the LinearLayout to the chat history LinearLayout
        chatHistoryLinearLayout.addView(messageLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constant.isNetworkAvailable(LiveChat.this)) {
            ProgressDialog progress = new ProgressDialog(LiveChat.this);
            progress.setTitle("Alert");
            progress.setMessage("Please wait...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        progress.dismiss();
                    }
                }
            }, 4000);
        }
    }

    private void BackPress(){
        startActivity(new Intent(this,Home.class));
        finish();
    }

    @Override
    public void onBackPressed() {

            startActivity(new Intent(LiveChat.this , Home.class));
    }

}
