package com.example.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BangersFont
import com.example.ui.theme.ComicDarkBg
import com.example.ui.theme.MarvelGold
import com.example.ui.theme.MarvelRed

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewCompanionScreen(
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler {
    onBack()
  }

  val context = LocalContext.current
  val webView = remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
      settings.allowFileAccess = true
      webViewClient = WebViewClient()
      webChromeClient = WebChromeClient()
      loadUrl("file:///android_asset/marvel_multiverse_companion.html")
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ComicDarkBg)
      .testTag("webview_companion_screen")
  ) {
    TopAppBar(
      title = {
        Text(
          text = "WEB COMPANION (HTML/JS)",
          fontFamily = BangersFont,
          fontSize = 20.sp,
          color = MarvelGold
        )
      },
      navigationIcon = {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("webview_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }
      },
      actions = {
        IconButton(
          onClick = { webView.reload() },
          modifier = Modifier.testTag("webview_reload_button")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reload",
            tint = MarvelGold
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = ComicDarkBg
      )
    )

    AndroidView(
      factory = { webView },
      modifier = Modifier
        .fillMaxSize()
        .testTag("embedded_companion_webview")
    )
  }
}
