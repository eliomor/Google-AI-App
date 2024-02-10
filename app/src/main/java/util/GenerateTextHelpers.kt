package util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

fun Activity.hideKeyboard() {
    val view: View? = currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun initChat(generativeModel: GenerativeModel, onChatReady: (Chat) -> Unit) {
    MainScope().launch(Dispatchers.Main) {
        val chat = generativeModel.startChat(listOf())
        onChatReady(chat)
    }
}

fun sendMessage(chat: Chat, prompt: String, onUpdate: (String) -> Unit) {
    MainScope().launch(Dispatchers.Main) {
        val response = chat.sendMessage(prompt)
        onUpdate(response.text ?: "")
    }
}
