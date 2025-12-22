package com.idol.prank.call.chat.video.activities.fragments;

import java.util.Random;

public class AutoBot {

    private static final String[] RESPONSES = {
            "I'm not sure what you mean. Can you please rephrase your question?",
            "That's an interesting question. Let me think about it...",
            "I'm sorry, I don't have an answer for that.",
            "Why do you ask?",
            "I think the answer is yes.",
            "I think the answer is no.",
            "I'm not sure. Can you provide more information?",
            "That's a great question! Let me look it up...",
            "I'm sorry, I'm not programmed to answer personal questions.",
            "That's outside of my area of expertise. Sorry!",
            "I'm not sure. Can you ask me something else?",
            "Interesting. Can you provide more context?",
            "I'm sorry, I don't understand.",
            "Can you clarify what you mean by that?",
            "I'm not programmed to provide medical or legal advice.",
            "I think you'll find the answer if you do a quick Google search!",
            "I'm sorry, I can't help you with that.",
            "I'm not sure. Let me check my database...",
            "That's a good question. I'm not sure of the answer.",
            "I'm sorry, I didn't understand your question. Can you please rephrase it?"
    };

    public static String getResponse(String input) {
        Random random = new Random();
        int index = random.nextInt(RESPONSES.length);
        return RESPONSES[index];
    }
}


