package com.onespan.android.interview

// MainActivity.kt
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.onespan.android.interview.repository.CatRepository
import com.onespan.android.interview.ui.CatBreed
import com.onespan.android.interview.viewmodel.CatBreedViewModel
import kotlinx.coroutines.Dispatchers


class MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val context = LocalContext.current
            val application = context.applicationContext as MyApplication
            val connectivityChecker = application.connectivityChecker
            val snackbarHostState = remember { SnackbarHostState() }

            val viewModel: CatBreedViewModel = remember {
                CatBreedViewModel(
                    repository = CatRepository(),
                    dispatcher = Dispatchers.IO,
                    connectivityChecker = connectivityChecker
                )
            }

            AndroidIntermediateInterviewTheme {
                CatBreed(viewModel, snackbarHostState)
            }
        }
    }
}