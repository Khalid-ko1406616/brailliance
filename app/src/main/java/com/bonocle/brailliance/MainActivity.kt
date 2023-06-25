package com.bonocle.brailliance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.bonocle.brailliance.view.theme.BraillianceTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BraillianceTheme {
                  Surface(
                    color = MaterialTheme.colors.background
                ) {
                      MainScreen()
                }
            }
        }
    }
}
