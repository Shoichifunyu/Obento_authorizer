package com.example.myscheduler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.Credentials
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_main.*


class LoginActivity : AppCompatActivity() {

    //各種入力フォームを保持するインスタンス
    private lateinit var username: EditText//ユーザ名(Eメールアドレス)入力用テキストボックス
    private lateinit var password: EditText//パスワード入力用テキストボックス
    private lateinit var loginButton: Button//ログインボタン
    private lateinit var createUserButton: Button//新規ユーザ作成ボタン

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //入力フォームインスタンスの生成
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        createUserButton = findViewById(R.id.button_create)
        //ボタンを押したときの処理
        loginButton.setOnClickListener { login(false) }//ログインホタン
        createUserButton.setOnClickListener { login(true) }//新規ユーザ作成ボタン
    }

    override fun onBackPressed() {
        //戻るボタンでメイン画面に戻れないようにする(メイン画面に戻るにはログインが必須)
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess() {
        //ログインに成功したら、メイン画面に戻る
        // successful login ends this activity, bringing the user back to the main activity
        finish()
    }

    private fun onLoginFailed(errorMsg: String) {
        //ログインに失敗したら、ログに書き込んだ上でメッセージ表示
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        //ユーザ名とパスワードとラジオボタンが空欄でないことを確認
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else -> true
    }

    //
    // ログインボタンを押したときの処理
    //@param[createUser]:trueなら新規ユーザ作成、falseなら通常のログイン
    //
    // handle user authentication (login) and account creation
    private fun login(createUser: Boolean) {
        if (!validateCredentials()) {
            onLoginFailed("Invalid username or password or user genre is unchecked")
            return
        }

        //処理中はボタンを押せないようにする
        // while this operation completes, disable the buttons to login or create a new account
        createUserButton.isEnabled = false
        loginButton.isEnabled = false

        var username = this.username.text.toString()
        var password = this.password.text.toString()

        if (createUser) {//新規ユーザ作成のとき
            // ユーザ名(E-mailアドレス)＋パスワードでユーザ作成実行
            // register a user using the Realm App we created in the TaskTracker class
            taskApp.emailPassword.registerUserAsync(username, password) {
                // re-enable the buttons after user registration completes
                createUserButton.isEnabled = true
                loginButton.isEnabled = true
            }
        } else {//通常ログインのとき
            val creds = Credentials.emailPassword(username, password)
            println(creds)
            taskApp.loginAsync(creds) {
                // re-enable the buttons after
                loginButton.isEnabled = true
                createUserButton.isEnabled = true
                if (!it.isSuccess) {//ログイン失敗時は、メッセージを表示
                    onLoginFailed(it.error.message ?: "An error occurred.")
                } else {
                    //成功時は、メイン画面に戻る
                    onLoginSuccess()
                }
            }
        }
    }
}

